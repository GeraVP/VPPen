package com.example.vpplan

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.vpplan.packag.MyDBManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Implementation of App Widget functionality.
 */
class WidgetDT : AppWidgetProvider() {
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
    val views = RemoteViews(context.packageName, R.layout.widget_d_t)

    val a = MyDBManager(context)
    val readwidget = a.readWidge()

    val demo = a.readWidge()
    val demo2 = a.IDminDT()
    val demo3 = a.dateMinRes()

    if(readwidget.isNotEmpty()){
        views.setTextViewText(R.id.widtv1, "Осталось: ${demo3.toString()} д.")
        views.setTextViewText(R.id.widtv2,demo[demo2].title)
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}