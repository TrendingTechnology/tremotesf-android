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
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.equeim.tremotesf.R
import org.equeim.tremotesf.data.FilesystemNavigator
import org.equeim.tremotesf.databinding.FilePickerFragmentBinding
import org.equeim.tremotesf.ui.NavigationFragment
import org.equeim.tremotesf.ui.utils.getStateFlow
import org.equeim.tremotesf.ui.utils.viewBinding
import org.equeim.tremotesf.utils.collectWhenStarted
import java.io.File


class FilePickerFragment : NavigationFragment(R.layout.file_picker_fragment,
                                              R.string.select_file,
                                              R.menu.file_picker_fragment_menu) {
    private val binding by viewBinding(FilePickerFragmentBinding::bind)
    private var adapter: FilePickerAdapter? = null

    private val model by viewModels<FilePickerFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FilePickerAdapter(this, model.navigator)
        this.adapter = adapter

        binding.filesView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            binding.filesView.visibility = View.GONE
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {
            model.navigator.init()
        }

        model.currentDirectory.collectWhenStarted(viewLifecycleOwner) {
            binding.currentDirectoryTextView.text = it.absolutePath
        }

        model.navigator.items.map { it.isEmpty() }.distinctUntilChanged().collectWhenStarted(viewLifecycleOwner) {
            binding.placeholder.text = if (it) {
                getString(R.string.no_files)
            } else {
                null
            }
        }
    }

    override fun onToolbarMenuItemClicked(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.primary_storage) {
            model.navigator.navigateToHome()
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        with(binding) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                filesView.visibility = View.VISIBLE
                model.navigator.init()
            } else {
                placeholder.text = getString(R.string.storage_permission_error)
            }
        }
    }

    fun finish(fileUri: Uri) {
        navigate(FilePickerFragmentDirections.addTorrentFileFragment(fileUri))
    }
}

private class FilePickerAdapter(private val fragment: FilePickerFragment, private val navigator: FilesystemNavigator) : ListAdapter<File?, RecyclerView.ViewHolder>(ItemCallback()) {
    private companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    init {
        navigator.items.collectWhenStarted(fragment.viewLifecycleOwner, ::submitList)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) {
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == TYPE_HEADER) {
            return HeaderHolder(inflater.inflate(R.layout.up_list_item,
                                                 parent,
                                                 false))
        }
        return ItemHolder(inflater.inflate(R.layout.file_picker_fragment_list_item,
                                           parent,
                                           false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_ITEM) {
            holder as ItemHolder

            val file = getItem(position)!!

            holder.file = file
            holder.textView.text = file.name
            holder.iconDrawable.level = if (file.isDirectory) 0 else 1
        }
    }

    private class ItemCallback : DiffUtil.ItemCallback<File?>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return true
        }
    }

    private inner class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { navigator.navigateUp() }
        }
    }

    private inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var file: File
        val textView = itemView as TextView
        val iconDrawable: Drawable = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.compoundDrawables.first()
        } else {
            textView.compoundDrawablesRelative.first()
        }

        init {
            itemView.setOnClickListener {
                if (file.isDirectory) {
                    navigator.navigateDown(file)
                } else {
                    fragment.finish(Uri.fromFile(file))
                }
            }
        }
    }
}

class FilePickerFragmentViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    @Suppress("DEPRECATION")
    val currentDirectory = savedStateHandle.getStateFlow(viewModelScope, "currentDirectory", Environment.getExternalStorageDirectory())

    val navigator = FilesystemNavigator(currentDirectory, viewModelScope)
}