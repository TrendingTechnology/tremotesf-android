/*
 * Copyright (C) 2017-2020 Alexey Rochev <equeim@gmail.com>
 *
 * This file is part of Tremotesf.
 *
 * Tremotesf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Tremotesf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.equeim.tremotesf.ui.addtorrent

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.trimmedLength
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

import org.equeim.libtremotesf.StringMap
import org.equeim.tremotesf.R
import org.equeim.tremotesf.data.TorrentFilesTree
import org.equeim.tremotesf.databinding.AddTorrentFileFilesFragmentBinding
import org.equeim.tremotesf.databinding.AddTorrentFileFragmentBinding
import org.equeim.tremotesf.databinding.AddTorrentFileInfoFragmentBinding
import org.equeim.tremotesf.databinding.DownloadDirectoryEditBinding
import org.equeim.tremotesf.databinding.LocalTorrentFileListItemBinding
import org.equeim.tremotesf.data.rpc.Rpc
import org.equeim.tremotesf.data.rpc.RpcConnectionState
import org.equeim.tremotesf.ui.BaseTorrentFilesAdapter
import org.equeim.tremotesf.ui.NavigationActivity
import org.equeim.tremotesf.ui.SelectionTracker
import org.equeim.tremotesf.ui.TorrentFileRenameDialogFragment
import org.equeim.tremotesf.ui.utils.ArrayDropdownAdapter
import org.equeim.tremotesf.ui.utils.Utils
import org.equeim.tremotesf.ui.utils.findFragment
import org.equeim.tremotesf.ui.utils.hideKeyboard
import org.equeim.tremotesf.ui.utils.showSnackbar
import org.equeim.tremotesf.ui.utils.viewBinding
import org.equeim.tremotesf.utils.collectWhenStarted


class AddTorrentFileFragment : AddTorrentFragment(R.layout.add_torrent_file_fragment,
                                                  R.string.add_torrent_file,
                                                  R.menu.add_torrent_fragment_menu), TorrentFileRenameDialogFragment.PrimaryFragment {
    companion object {
        fun setupDownloadDirectoryEdit(binding: DownloadDirectoryEditBinding,
                                       fragment: Fragment,
                                       savedInstanceState: Bundle?): AddTorrentDirectoriesAdapter {
            val downloadDirectoryEdit = binding.downloadDirectoryEdit
            val downloadDirectoryLayout = binding.downloadDirectoryLayout
            downloadDirectoryEdit.doAfterTextChanged {
                val path = it?.trim()
                when {
                    path.isNullOrEmpty() -> {
                        downloadDirectoryLayout.helperText = null
                    }
                    Rpc.serverSettings.canShowFreeSpaceForPath() -> {
                        Rpc.nativeInstance.getFreeSpaceForPath(path.toString())
                    }
                    Rpc.serverSettings.downloadDirectory?.contentEquals(path) == true -> {
                        Rpc.nativeInstance.getDownloadDirFreeSpace()
                    }
                    else -> {
                        downloadDirectoryLayout.helperText = null
                    }
                }
            }

            if (savedInstanceState == null) {
                downloadDirectoryEdit.setText(Rpc.serverSettings.downloadDirectory)
            }

            val directoriesAdapter = AddTorrentDirectoriesAdapter(downloadDirectoryEdit, savedInstanceState)
            downloadDirectoryEdit.setAdapter(directoriesAdapter)

            Rpc.gotDownloadDirFreeSpaceEvents.collectWhenStarted(fragment.viewLifecycleOwner) { bytes ->
                val text = downloadDirectoryEdit.text?.trim()
                if (!text.isNullOrEmpty() && Rpc.serverSettings.downloadDirectory?.contentEquals(text) == true) {
                    downloadDirectoryLayout.helperText = fragment.getString(R.string.free_space, Utils.formatByteSize(fragment.requireContext(), bytes))
                }
            }

            Rpc.gotFreeSpaceForPathEvents.collectWhenStarted(fragment.viewLifecycleOwner) { (path, success, bytes) ->
                val text = downloadDirectoryEdit.text?.trim()
                if (!text.isNullOrEmpty() && path.contentEquals(text)) {
                    downloadDirectoryLayout.helperText = if (success) {
                        fragment.getString(R.string.free_space, Utils.formatByteSize(fragment.requireContext(), bytes))
                    } else {
                        fragment.getString(R.string.free_space_error)
                    }
                }
            }

            return directoriesAdapter
        }
    }

    private val args: AddTorrentFileFragmentArgs by navArgs()

    private val binding by viewBinding(AddTorrentFileFragmentBinding::bind)

    private var doneMenuItem: MenuItem? = null
    private var pagerAdapter: PagerAdapter? = null
    private var backPressedCallback: OnBackPressedCallback? = null
    private var snackbar: Snackbar? = null

    val model: AddTorrentFileModel by viewModels<AddTorrentFileModelImpl>()

    private var done = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (args.uri.scheme == ContentResolver.SCHEME_FILE &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {
            model.onRequestPermissionResult(true)
            model.load(args.uri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        model.onRequestPermissionResult(grantResults.first() == PackageManager.PERMISSION_GRANTED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = PagerAdapter(this)
        this.pagerAdapter = pagerAdapter
        binding.pager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.setText(PagerAdapter.getTitle(position))
        }.attach()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (done ||
                    binding.pager.currentItem != PagerAdapter.Tab.Files.ordinal || !model.filesTree.navigateUp()) {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var previousPage = -1

            override fun onPageSelected(position: Int) {
                if (previousPage != -1) {
                    findFragment<FilesFragment>()?.adapter?.selectionTracker?.clearSelection()
                    hideKeyboard()
                }
                previousPage = position
            }
        })

        model.viewUpdateData.collectWhenStarted(viewLifecycleOwner, ::updateView)
    }

    override fun onDestroyView() {
        doneMenuItem = null
        pagerAdapter = null
        backPressedCallback = null
        snackbar = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_torrent_fragment_menu, menu)
    }

    override fun onToolbarMenuItemClicked(menuItem: MenuItem): Boolean {
        if (menuItem.itemId != R.id.done) {
            return false
        }
        val infoFragment = findFragment<InfoFragment>()
        if (infoFragment?.check() == true) {
            val priorities = model.getFilePriorities()
            Rpc.nativeInstance.addTorrentFile(model.detachFd(),
                                              infoFragment.binding.downloadDirectoryLayout.downloadDirectoryEdit.text.toString(),
                                              priorities.unwantedFiles.toIntArray(),
                                              priorities.highPriorityFiles.toIntArray(),
                                              priorities.lowPriorityFiles.toIntArray(),
                                              StringMap().apply { putAll(model.renamedFiles) },
                                              priorityItemEnums[priorityItems.indexOf(infoFragment.binding.priorityView.text.toString())],
                                              infoFragment.binding.startDownloadingCheckBox.isChecked)
            infoFragment.directoriesAdapter?.save()
            done = true
            activity?.onBackPressed()
            return true
        }
        return false
    }

    override fun onRenameFile(torrentId: Int, filePath: String, newName: String) {
        model.renamedFiles[filePath] = newName
        model.filesTree.renameFile(filePath, newName)
    }

    private fun updateView(viewUpdateData: AddTorrentFileModel.ViewUpdateData) {
        val (parserStatus, rpcStatus, hasStoragePermission) = viewUpdateData

        with(binding) {
            if (rpcStatus.isConnected && parserStatus == AddTorrentFileModel.ParserStatus.Loaded) {
                this@AddTorrentFileFragment.toolbar?.apply {
                    (layoutParams as AppBarLayout.LayoutParams).scrollFlags =
                            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP or
                                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    subtitle = model.torrentName
                }
                doneMenuItem?.isVisible = true

                tabLayout.visibility = View.VISIBLE
                pager.visibility = View.VISIBLE

                placeholderLayout.visibility = View.GONE
            } else {
                placeholder.text = if (!hasStoragePermission) {
                    getString(R.string.storage_permission_error)
                } else {
                    when (parserStatus) {
                        AddTorrentFileModel.ParserStatus.Loading -> getString(R.string.loading)
                        AddTorrentFileModel.ParserStatus.FileIsTooLarge -> getString(R.string.file_is_too_large)
                        AddTorrentFileModel.ParserStatus.ReadingError -> getString(R.string.file_reading_error)
                        AddTorrentFileModel.ParserStatus.ParsingError -> getString(R.string.file_parsing_error)
                        AddTorrentFileModel.ParserStatus.Loaded -> rpcStatus.statusString
                        else -> null
                    }
                }

                progressBar.visibility = if (parserStatus == AddTorrentFileModel.ParserStatus.Loading ||
                        (rpcStatus.connectionState == RpcConnectionState.Connecting && parserStatus == AddTorrentFileModel.ParserStatus.Loaded)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                placeholderLayout.visibility = View.VISIBLE

                this@AddTorrentFileFragment.toolbar?.apply {
                    (layoutParams as AppBarLayout.LayoutParams).scrollFlags = 0
                    subtitle = null
                }
                doneMenuItem?.isVisible = false

                hideKeyboard()

                tabLayout.visibility = View.GONE
                pager.visibility = View.GONE
                pager.currentItem = 0
                placeholder.visibility = View.VISIBLE

                if (parserStatus == AddTorrentFileModel.ParserStatus.Loaded) {
                    when (rpcStatus.connectionState) {
                        RpcConnectionState.Disconnected -> {
                            snackbar = requireView().showSnackbar("", Snackbar.LENGTH_INDEFINITE, R.string.connect) {
                                snackbar = null
                                Rpc.nativeInstance.connect()
                            }
                        }
                        RpcConnectionState.Connecting -> {
                            snackbar?.dismiss()
                            snackbar = null
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

    class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        companion object {
            private val tabs = Tab.values()

            @StringRes
            fun getTitle(position: Int): Int {
                return when (tabs[position]) {
                    Tab.Info -> R.string.information
                    Tab.Files -> R.string.files
                }
            }
        }

        enum class Tab {
            Info,
            Files
        }

        override fun getItemCount() = tabs.size

        override fun createFragment(position: Int): Fragment {
            return when (tabs[position]) {
                Tab.Info -> InfoFragment()
                Tab.Files -> FilesFragment()
            }
        }
    }

    class InfoFragment : Fragment(R.layout.add_torrent_file_info_fragment) {
        val binding by viewBinding(AddTorrentFileInfoFragmentBinding::bind)
        var directoriesAdapter: AddTorrentDirectoriesAdapter? = null

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            with(binding) {
                priorityView.setText(R.string.normal_priority)
                priorityView.setAdapter(ArrayDropdownAdapter((requireParentFragment() as AddTorrentFileFragment).priorityItems))

                directoriesAdapter = setupDownloadDirectoryEdit(downloadDirectoryLayout, this@InfoFragment, savedInstanceState)

                startDownloadingCheckBox.isChecked = Rpc.serverSettings.startAddedTorrents
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            directoriesAdapter?.saveInstanceState(outState)
        }

        fun check(): Boolean {
            val ret: Boolean
            with(binding.downloadDirectoryLayout) {
                downloadDirectoryLayout.error = if (downloadDirectoryEdit.text.trimmedLength() == 0) {
                    ret = false
                    getString(R.string.empty_field_error)
                } else {
                    ret = true
                    null
                }
            }
            return ret
        }
    }

    class FilesFragment : Fragment(R.layout.add_torrent_file_files_fragment) {
        private val mainFragment: AddTorrentFileFragment
            get() = requireParentFragment() as AddTorrentFileFragment

        var adapter: Adapter? = null
            private set

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val model = mainFragment.model

            val adapter = Adapter(model.filesTree, this, requireActivity() as NavigationActivity)
            this.adapter = adapter

            AddTorrentFileFilesFragmentBinding.bind(view).filesView.apply {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(activity)
                addItemDecoration(DividerItemDecoration(activity,
                                                        DividerItemDecoration.VERTICAL))
                (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            }

            model.filesTree.items.collectWhenStarted(viewLifecycleOwner) {
                adapter.update(it) {
                    if (model.parserStatus.value == AddTorrentFileModel.ParserStatus.Loaded) {
                        adapter.selectionTracker.restoreInstanceState()
                    }
                }
            }
        }

        override fun onDestroyView() {
            adapter = null
            super.onDestroyView()
        }

        class Adapter(filesTree: TorrentFilesTree,
                      fragment: Fragment,
                      private val activity: NavigationActivity) : BaseTorrentFilesAdapter(filesTree, fragment) {
            override fun onCreateViewHolder(parent: ViewGroup,
                                            viewType: Int): RecyclerView.ViewHolder {
                if (viewType == TYPE_ITEM) {
                    return ItemHolder(this,
                                      selectionTracker,
                                      LocalTorrentFileListItemBinding.inflate(LayoutInflater.from(parent.context),
                                                                              parent,
                                                                              false))
                }
                return super.onCreateViewHolder(parent, viewType)
            }

            override fun navigateToRenameDialog(path: String, name: String) {
                activity.navigate(AddTorrentFileFragmentDirections.torrentFileRenameDialogFragment(path, name))
            }

            private class ItemHolder(private val adapter: Adapter,
                                     selectionTracker: SelectionTracker<Int>,
                                     val binding: LocalTorrentFileListItemBinding) : BaseItemHolder(adapter, selectionTracker, binding.root) {
                override fun update() {
                    super.update()
                    val context = binding.sizeTextView.context
                    binding.sizeTextView.text =
                            Utils.formatByteSize(context, adapter.getItem(adapterPosition)!!.size)
                }
            }
        }
    }
}

