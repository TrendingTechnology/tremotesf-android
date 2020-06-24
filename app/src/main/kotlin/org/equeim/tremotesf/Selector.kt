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

package org.equeim.tremotesf

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView


private const val BUNDLE_KEY = "org.equeim.tremotesf.Selector"

abstract class Selector<ItemType, IdType>(private val activity: AppCompatActivity,
                                                      private val actionModeCallback: ActionModeCallback<ItemType>,
                                                      private val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
                                                      var items: Collection<ItemType>,
                                                      private val idFromItem: (ItemType) -> IdType,
                                                      private val titleStringId: Int) {
    var actionMode: ActionMode? = null
    val selectedItems = mutableListOf<ItemType>()
    protected val selectedIds = mutableListOf<IdType>()

    var hasHeaderItem = false

    val selectedCount: Int
        get() {
            return selectedItems.size
        }

    val hasSelection: Boolean
        get() {
            return selectedItems.isNotEmpty()
        }

    init {
        actionModeCallback.selector = this
    }

    fun isSelected(item: ItemType): Boolean {
        return selectedItems.contains(item)
    }

    fun toggleSelection(item: ItemType, position: Int) {
        val index = selectedItems.indexOf(item)
        if (index == -1) {
            selectedItems.add(item)
            selectedIds.add(idFromItem(item))
        } else {
            selectedItems.removeAt(index)
            selectedIds.removeAt(index)

            if (selectedItems.isEmpty()) {
                actionMode?.finish()
            }
        }
        adapter.notifyItemChanged(position)
        updateActionMode()
    }

    fun selectAll() {
        if (selectedCount == items.size) {
            return
        }

        selectedItems.clear()
        selectedIds.clear()

        for (item in items) {
            selectedItems.add(item)
            selectedIds.add(idFromItem(item))
        }

        if (hasHeaderItem) {
            adapter.notifyItemRangeChanged(1, items.size)
        } else {
            adapter.notifyItemRangeChanged(0, items.size)
        }

        updateActionMode()
    }

    fun clearSelection() {
        if (selectedItems.isEmpty()) {
            return
        }

        selectedItems.clear()
        selectedIds.clear()

        if (hasHeaderItem) {
            adapter.notifyItemRangeChanged(1, items.size)
        } else {
            adapter.notifyItemRangeChanged(0, items.size)
        }
    }

    fun clearRemovedItems() {
        var i = 0
        while (i < selectedItems.size) {
            if (items.contains(selectedItems[i])) {
                i++
            } else {
                selectedItems.removeAt(i)
                selectedIds.removeAt(i)
            }
        }
        updateActionMode()
    }

    fun startActionMode() {
        actionMode = activity.startSupportActionMode(actionModeCallback)
        updateActionMode()
    }

    private fun updateActionMode() {
        actionMode?.let { actionMode ->
            actionMode.title = activity.resources.getQuantityString(titleStringId,
                                                                    selectedCount,
                                                                    selectedCount)
            actionMode.invalidate()
        }
    }

    fun saveInstanceState(outState: Bundle) {
        if (actionMode != null) {
            putIdsToBundle(outState)
        }
    }

    fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY)) {
            @Suppress("UNCHECKED_CAST")
            val ids = getIdsFromBundle(savedInstanceState)
            for (item in items) {
                val id = idFromItem(item)
                if (ids.contains(id)) {
                    selectedItems.add(item)
                    selectedIds.add(id)
                }
            }
            startActionMode()
        }
    }

    abstract fun putIdsToBundle(bundle: Bundle)
    abstract fun getIdsFromBundle(bundle: Bundle): List<IdType>

    abstract class ViewHolder<T>(protected val selector: Selector<T, *>,
                                       itemView: View) : RecyclerView.ViewHolder(itemView),
                                                         View.OnClickListener,
                                                         View.OnLongClickListener {
        abstract var item: T
        private val selectedBackground: View = itemView.findViewById(R.id.selected_background_view)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(view: View) {
            if (selector.actionMode != null) {
                selector.toggleSelection(item, adapterPosition)
            }
        }

        override fun onLongClick(view: View): Boolean {
            if (selector.actionMode != null) {
                return false
            }
            selector.toggleSelection(item, adapterPosition)
            selector.startActionMode()
            return true
        }

        fun updateSelectedBackground() {
            selectedBackground.isActivated = selector.isSelected(item)
        }
    }

    abstract class ActionModeCallback<T> : ActionMode.Callback {
        lateinit var selector: Selector<T, *>

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            selector.actionMode = null
            selector.clearSelection()
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            if (item.itemId == R.id.select_all) {
                selector.selectAll()
                return true
            }
            return false
        }
    }

    interface ActionModeActivity {
        val actionMode: ActionMode?
    }
}

class IntSelector<ItemType>(activity: AppCompatActivity,
                            actionModeCallback: ActionModeCallback<ItemType>,
                            adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
                            items: Collection<ItemType>,
                            idFromItem: (ItemType) -> Int,
                            titleStringId: Int) : Selector<ItemType, Int>(activity, actionModeCallback, adapter, items, idFromItem, titleStringId) {
    override fun getIdsFromBundle(bundle: Bundle): List<Int> {
        return bundle.getIntArray(BUNDLE_KEY)?.toList() ?: emptyList()
    }

    override fun putIdsToBundle(bundle: Bundle) {
        bundle.putIntArray(BUNDLE_KEY, selectedIds.toIntArray())
    }
}

class StringSelector<ItemType>(activity: AppCompatActivity,
                               actionModeCallback: ActionModeCallback<ItemType>,
                               adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
                               items: Collection<ItemType>,
                               idFromItem: (ItemType) -> String,
                               titleStringId: Int) : Selector<ItemType, String>(activity, actionModeCallback, adapter, items, idFromItem, titleStringId) {
    override fun getIdsFromBundle(bundle: Bundle): List<String> {
        return bundle.getStringArray(BUNDLE_KEY)?.toList() ?: emptyList()
    }

    override fun putIdsToBundle(bundle: Bundle) {
        bundle.putStringArray(BUNDLE_KEY, selectedIds.toTypedArray())
    }
}