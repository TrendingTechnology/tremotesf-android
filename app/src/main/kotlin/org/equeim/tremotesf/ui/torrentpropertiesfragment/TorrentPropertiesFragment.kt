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

package org.equeim.tremotesf.ui.torrentpropertiesfragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.FloatingWindow
import androidx.navigation.NavDestination
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

import org.equeim.libtremotesf.TorrentData
import org.equeim.tremotesf.R
import org.equeim.tremotesf.databinding.TorrentPropertiesFragmentBinding
import org.equeim.tremotesf.data.rpc.Rpc
import org.equeim.tremotesf.data.rpc.RpcConnectionState
import org.equeim.tremotesf.data.rpc.Torrent
import org.equeim.tremotesf.ui.NavigationFragment
import org.equeim.tremotesf.ui.TorrentFileRenameDialogFragment
import org.equeim.tremotesf.ui.addNavigationBarBottomPadding
import org.equeim.tremotesf.ui.utils.Utils
import org.equeim.tremotesf.ui.utils.findFragment
import org.equeim.tremotesf.ui.utils.hideKeyboard
import org.equeim.tremotesf.ui.utils.popDialog
import org.equeim.tremotesf.ui.utils.showSnackbar
import org.equeim.tremotesf.ui.utils.viewBinding
import org.equeim.tremotesf.utils.collectWhenStarted


class TorrentPropertiesFragment : NavigationFragment(R.layout.torrent_properties_fragment,
                                                     0,
                                                     R.menu.torrent_properties_fragment_menu), TorrentFileRenameDialogFragment.PrimaryFragment {
    val args: TorrentPropertiesFragmentArgs by navArgs()

    var torrent: Torrent? = null
        private set

    val binding by viewBinding(TorrentPropertiesFragmentBinding::bind)

    private var menu: Menu? = null
    private var startMenuItem: MenuItem? = null
    private var pauseMenuItem: MenuItem? = null

    private var snackbar: Snackbar? = null

    private var pagerAdapter: PagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar?.title = args.name
        setupToolbarMenu()

        val pagerAdapter = PagerAdapter(this)
        this.pagerAdapter = pagerAdapter
        binding.pager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.setText(PagerAdapter.getTitle(position))
        }.attach()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (binding.pager.currentItem != PagerAdapter.Tab.Files.ordinal ||
                    findFragment<TorrentFilesFragment>()?.model?.filesTree?.navigateUp() != true) {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var previousPage = -1

            override fun onPageSelected(position: Int) {
                if (previousPage != -1) {
                    requiredActivity.apply {
                        actionMode?.finish()
                        hideKeyboard()
                    }
                }
                if (position == PagerAdapter.Tab.Trackers.ordinal) {
                    binding.fab.show()
                } else {
                    binding.fab.hide()
                }
                previousPage = position
            }
        })

        binding.fab.setOnClickListener {
            navigate(TorrentPropertiesFragmentDirections.editTrackerDialogFragment())
        }

        Rpc.torrents.collectWhenStarted(viewLifecycleOwner, ::updateTorrent)

        Rpc.status.collectWhenStarted(viewLifecycleOwner, ::onRpcStatusChanged)
    }

    override fun onDestroyView() {
        menu = null
        startMenuItem = null
        pauseMenuItem = null
        snackbar = null

        pagerAdapter = null

        super.onDestroyView()
    }

    override fun onNavigatedFrom(newDestination: NavDestination) {
        if (newDestination !is FloatingWindow) {
            for (fragment in childFragmentManager.fragments) {
                (fragment as? PagerFragment)?.onNavigatedFrom()
            }
        }
    }

    private fun setupToolbarMenu() {
        toolbar?.menu?.let { menu ->
            this.menu = menu
            startMenuItem = menu.findItem(R.id.start)
            pauseMenuItem = menu.findItem(R.id.pause)
        }
    }

    override fun onToolbarMenuItemClicked(menuItem: MenuItem): Boolean {
        torrent?.let { torrent ->
            when (menuItem.itemId) {
                R.id.start -> Rpc.nativeInstance.startTorrents(intArrayOf(torrent.id))
                R.id.pause -> Rpc.nativeInstance.pauseTorrents(intArrayOf(torrent.id))
                R.id.check -> Rpc.nativeInstance.checkTorrents(intArrayOf(torrent.id))
                R.id.reannounce -> Rpc.nativeInstance.reannounceTorrents(intArrayOf(torrent.id))
                R.id.set_location -> navigate(TorrentPropertiesFragmentDirections.torrentSetLocationDialogFragment(intArrayOf(torrent.id), torrent.downloadDirectory))
                R.id.rename ->
                    navigate(TorrentPropertiesFragmentDirections.torrentFileRenameDialogFragment(torrent.name, torrent.name, torrent.id))
                R.id.remove -> navigate(TorrentPropertiesFragmentDirections.removeTorrentDialogFragment(intArrayOf(torrent.id), true))
                R.id.share -> Utils.shareTorrents(listOf(torrent.data.magnetLink), requireContext())
                else -> return false
            }
        }
        return true
    }

    override fun onRenameFile(torrentId: Int, filePath: String, newName: String) {
        Rpc.nativeInstance.renameTorrentFile(torrentId, filePath, newName)
    }

    private fun onRpcStatusChanged(status: Rpc.Status) {
        with(binding) {
            when (status.connectionState) {
                RpcConnectionState.Disconnected -> {
                    snackbar = requireView().showSnackbar("", Snackbar.LENGTH_INDEFINITE, R.string.connect) {
                        snackbar = null
                        Rpc.nativeInstance.connect()
                    }
                    placeholder.text = status.statusString
                }
                RpcConnectionState.Connecting -> {
                    snackbar?.dismiss()
                    snackbar = null
                    placeholder.text = getString(R.string.connecting)
                }
                RpcConnectionState.Connected -> {
                    if (torrent == null) {
                        placeholder.text = getString(R.string.torrent_not_found)
                    }
                }
            }

            progressBar.visibility = if (status.connectionState == RpcConnectionState.Connecting) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun updateTorrent(torrents: List<Torrent>) {
        val newTorrent = torrents.find { it.hashString == args.hash }
        if (newTorrent != torrent) {
            val oldTorrent = torrent
            torrent = newTorrent

            if (newTorrent == null || oldTorrent == null) {
                if (newTorrent == null) {
                    if (Rpc.isConnected.value) {
                        binding.placeholder.text = getString(R.string.torrent_removed)
                    }
                    navController.popDialog()
                }

                updatePlaceholderVisibility()
                updateMenu()
            }

            torrent?.let { torrent ->
                toolbar?.title = torrent.name
            }

            for (fragment in childFragmentManager.fragments) {
                (fragment as? PagerFragment)?.update()
            }
        }
    }

    private fun updateMenu() {
        val menu = this.menu ?: return
        if (torrent == null) {
            toolbar?.hideOverflowMenu()
            menu.setGroupVisible(0, false)
        } else {
            menu.setGroupVisible(0, true)
            val paused = when (torrent?.status) {
                TorrentData.Status.Paused,
                TorrentData.Status.Errored -> true
                else -> false
            }
            if (paused) {
                pauseMenuItem
            } else {
                startMenuItem
            }?.isVisible = false
        }
    }

    private fun updatePlaceholderVisibility() {
        with(binding) {
            if (torrent != null) {
                (toolbar.layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags =
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                                AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP or
                                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                tabLayout.visibility = View.VISIBLE
                pager.visibility = View.VISIBLE
                placeholderLayout.visibility = View.GONE
            } else {
                (toolbar.layoutParams as AppBarLayout.LayoutParams?)?.scrollFlags = 0
                tabLayout.visibility = View.GONE
                pager.visibility = View.GONE
                pager.currentItem = 0
                placeholderLayout.visibility = View.VISIBLE
            }
        }
    }

    class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        companion object {
            private val tabs = Tab.values()

            @StringRes
            fun getTitle(position: Int): Int {
                return when (tabs[position]) {
                    Tab.Details -> R.string.details
                    Tab.Files -> R.string.files
                    Tab.Trackers -> R.string.trackers
                    Tab.Peers -> R.string.peers
                    Tab.Limits -> R.string.limits
                }
            }
        }

        enum class Tab {
            Details,
            Files,
            Trackers,
            Peers,
            Limits
        }

        override fun getItemCount(): Int {
            return tabs.size
        }

        override fun createFragment(position: Int): Fragment {
            return when (tabs[position]) {
                Tab.Details -> TorrentDetailsFragment()
                Tab.Files -> TorrentFilesFragment()
                Tab.Trackers -> TrackersFragment()
                Tab.Peers -> PeersFragment()
                Tab.Limits -> TorrentLimitsFragment()
            }
        }
    }

    abstract class PagerFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
        open fun update() = Unit
        open fun onNavigatedFrom() = Unit

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            addNavigationBarBottomPadding(true)
        }
    }
}
