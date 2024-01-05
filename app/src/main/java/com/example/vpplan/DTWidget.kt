package com.example.vpplan

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.text.ParseException
import java.time.LocalDate
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class DTWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created


    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.d_t_widget)
    views.setTextViewText(R.id.Wtext, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}



