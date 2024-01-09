package com.example.vpplan.packag
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class MyDBManager(val context: Context) {
    val myDBHelp = MyDBHelp(context) // Будет открывать БД
    var db: SQLiteDatabase? = null

    fun openDb()
    {
        db = myDBHelp.writableDatabase // Чтобы бд открылась для записи и считывания
    }
    fun insertToDb(title: String,content:String,uri:String)
    {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE,title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI,uri)
        }
        db?.insert(MyDbNameClass.TABLE_NAME,null,values)
    }
    fun readDbData(): ArrayList<ListItem>{
        val dataList = ArrayList<ListItem>() // Создал массив
        val cursor = db?.query(MyDbNameClass.TABLE_NAME,null,null,null,null,null,null)

        while (cursor?.moveToNext()!!)
        {
            val dataT = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_TITLE))
            val dataContent = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_CONTENT))
            val dataUri = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
            var item = ListItem()
            item.title = dataT
            item.desc = dataContent
            item.uri = dataUri
            dataList.add(item)
            //val dataText = cursor?.getString(cursor.getColumnIndex(MyDbNameClss.COLUMN_NAME_TITLE))
        } // СЧИТЫВАНИЕ ТОЛЬКО ЗАГОЛОВКА

        // cursor - спец класс для считывания данных
        cursor.close()
        return dataList // Возвращаю его
    } // Возвращает массив типа string
    fun closeDb(){
        myDBHelp.close()
    }
}