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

package org.equeim.tremotesf.ui.utils

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.widget.ProgressBar

import androidx.core.content.res.use

import org.equeim.tremotesf.R
import org.equeim.tremotesf.data.rpc.Rpc
import org.equeim.tremotesf.ui.ForegroundService
import org.equeim.tremotesf.ui.NavigationActivity
import org.equeim.tremotesf.utils.Logger


object Utils : Logger {
    fun shutdownApp(context: Context, stopService: Boolean = true) {
        info("Utils.shutdownApp()")
        NavigationActivity.finishAllActivities()
        Rpc.disconnectOnShutdown()
        if (stopService) {
            ForegroundService.stop(context)
        }
    }

    private fun calculateSize(bytes: Long): Pair<Double, Int> {
        var unit = 0
        var size = bytes.toDouble()
        while (size >= 1024 && unit < 8) {
            size /= 1024
            unit++
        }
        return Pair(size, unit)
    }

    fun formatByteSize(context: Context, bytes: Long): String {
        val (size, unit) = calculateSize(bytes)
        val numberString = DecimalFormats.generic.format(size)
        return context.resources.getStringArray(R.array.size_units)[unit].format(numberString)
    }

    fun formatByteSpeed(context: Context, bytes: Long): String {
        val (size, unit) = calculateSize(bytes)
        val numberString = DecimalFormats.generic.format(size)
        return context.resources.getStringArray(R.array.speed_units)[unit].format(numberString)
    }

    fun formatDuration(context: Context, seconds: Int): String {
        if (seconds < 0) {
            return "\u221E"
        }

        var dseconds = seconds

        val days = dseconds / 86400
        dseconds %= 86400
        val hours = dseconds / 3600
        dseconds %= 3600
        val minutes = dseconds / 60
        dseconds %= 60

        if (days > 0) {
            return context.getString(R.string.duration_days, days, hours)
        }

        if (hours > 0) {
            return context.getString(R.string.duration_hours, hours, minutes)
        }

        if (minutes > 0) {
            return context.getString(R.string.duration_minutes, minutes, dseconds)
        }

        return context.getString(R.string.duration_seconds, dseconds)
    }

    fun setProgressBarColor(progressBar: ProgressBar) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val ta = progressBar.context.obtainStyledAttributes(intArrayOf(R.attr.colorSecondary))
            ta.use {
                progressBar.progressDrawable.colorFilter = PorterDuffColorFilter(ta.getColor(0, 0), PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

    fun shareTorrents(magnetLinks: List<String>, context: Context) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, magnetLinks.joinToString("\n"))
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_torrent)))
        } else {
            warn("Failed to share torrent, intent $intent did not resolve activity")
        }
    }
}

