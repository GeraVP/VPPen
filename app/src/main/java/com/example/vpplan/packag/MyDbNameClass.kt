package com.example.vpplan.packag

import android.provider.BaseColumns

object MyDbNameClass: BaseColumns { // Класс типа Object.У него уже есть переменная id. Здесь хранятся все названия
    const val TABLE_NAME = "my_table"  // 0 title content
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_IMAGE_URI = "uri"

    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "MyLessonDb.db"

    const val CT = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT,$COLUMN_NAME_CONTENT TEXT,$COLUMN_NAME_IMAGE_URI TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}