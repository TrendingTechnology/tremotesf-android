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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.equeim.tremotesf.R
import org.equeim.tremotesf.data.TorrentFilesTree
import org.equeim.tremotesf.databinding.TorrentFileListItemBinding
import org.equeim.tremotesf.ui.BaseTorrentFilesAdapter
import org.equeim.tremotesf.ui.SelectionTracker
import org.equeim.tremotesf.ui.TorrentFileRenameDialogFragment
import org.equeim.tremotesf.ui.utils.DecimalFormats
import org.equeim.tremotesf.ui.utils.Utils
import org.equeim.tremotesf.ui.utils.safeNavigate


class TorrentFilesAdapter(filesTree: TorrentFilesTree,
                          private val fragment: TorrentFilesFragment) : BaseTorrentFilesAdapter(filesTree, fragment) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val binding = TorrentFileListItemBinding.inflate(LayoutInflater.from(parent.context),
                                                             parent,
                                                             false)
            Utils.setProgressBarColor(binding.progressBar)
            return ItemHolder(this, selectionTracker, binding)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun navigateToRenameDialog(path: String, name: String) {
        val torrent = fragment.torrent ?: return
        fragment.findNavController().safeNavigate(TorrentPropertiesFragmentDirections
                                                          .torrentFileRenameDialogFragment(path, name, torrent.id))
    }

    private class ItemHolder(private val adapter: TorrentFilesAdapter,
                             selectionTracker: SelectionTracker<Int>,
                             val binding: TorrentFileListItemBinding) : BaseItemHolder(adapter, selectionTracker, binding.root) {
        override fun update() {
            super.update()
            val item = adapter.getItem(adapterPosition)!!
            with(binding) {
                progressBar.progress = (item.progress * 100).toInt()
                val context = progressBar.context
                progressTextView.text = context.getString(R.string.completed_string,
                                                          Utils.formatByteSize(context,
                                                                               item.completedSize),
                                                          Utils.formatByteSize(context,
                                                                               item.size),
                                                          DecimalFormats.generic.format(item.progress * 100))
            }
        }
    }
}