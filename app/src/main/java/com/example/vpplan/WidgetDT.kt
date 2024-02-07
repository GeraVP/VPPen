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
    val dataList = a.readWidge()


    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentDate = Calendar.getInstance()
    val userDate = Calendar.getInstance()
    userDate.time = inputFormat.parse(dataList[1].valueDT)

    val difference = userDate.timeInMillis - currentDate.timeInMillis
    val daysLeft = difference / (24 * 60 * 60 * 1000)
    val daysLef = daysLeft.toInt()

    val demo = a.mai()
    val demo2 = a.IDElSort()

    if(dataList.isNotEmpty()){
        views.setTextViewText(R.id.widtv1, dataList[demo2].title.toString())
        views.setTextViewText(R.id.widtv2,demo)//daysLef.toString()
    }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}