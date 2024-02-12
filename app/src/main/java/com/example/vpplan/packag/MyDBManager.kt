package com.example.vpplan.packag
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.*

class MyDBManager(val context: Context) {
    val myDBHelp = MyDBHelp(context)
    var db: SQLiteDatabase? = null
    fun openDb()
    {
        db = myDBHelp.writableDatabase
    }
    suspend fun insertToDb(title: String,content:String,uri:String) = withContext(Dispatchers.IO)
    {
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE,title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI,uri)
        }
        db?.insert(MyDbNameClass.TABLE_NAME,null,values)
    }
    suspend fun UpdateItem(title:String, content:String, uri:String,id:Int) = withContext(Dispatchers.IO){
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE,title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT,content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI,uri)
        }
        db?.update(MyDbNameClass.TABLE_NAME,values,selection,null)
    }
    suspend fun readDbData(searchText: String): ArrayList<ListItem> = withContext(Dispatchers.IO){
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDbNameClass.COLUMN_NAME_TITLE} like ?"
        val cursor = db?.query(MyDbNameClass.TABLE_NAME,null,selection, arrayOf("%$searchText%"),null,null,null)

        while (cursor?.moveToNext()!!)
        {
            val dataT = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_TITLE))
            val dataContent = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_CONTENT))
            val dataUri = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
            val dataId = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)) // 10.01.24
            var item = ListItem()
            item.title = dataT
            item.desc = dataContent
            item.uri = dataUri
            item.id = dataId
            dataList.add(item)
        }
        cursor.close()
        return@withContext dataList
    }
    fun closeDb(){
        myDBHelp.close()
    }
    fun removeItemFromDb(id: String)
    {
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME,selection,null)
    }


    fun readWidge(): ArrayList<WidgetFile>{
        openDb()
        val dataList = ArrayList<WidgetFile>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME,null,null, null,null,null,null)

        while (cursor?.moveToNext()!!)
        {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_TITLE))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_CONTENT))
            var item = WidgetFile()
            item.title = name
            item.valueDT = date
            dataList.add(item)
        }
        cursor.close()
        closeDb()
        return dataList
    } // Чтение бд и запись их в массив типа WidgetFile с переменными title и valueDT

    fun MinDT():String {
        openDb()
        val a = readWidge()

        val value = a.count()
        val dates = arrayOfNulls<String>(value)

        for(i in 0 until value){
            dates[i] = a[i].valueDT
        }

        dates.sort()

        closeDb()
        return dates[0].toString()
    } // Создание отдельного массива под даты и сортировка их, нахождение минимальной даты
    fun IDminDT ():Int {var idel = 0
        val a = readWidge()
        val value = a.count()

        for(i in 0 until value){
            if(a[i].valueDT.toString() == MinDT().toString()){
                idel = i
            }
        }

        return idel
    } // Поиск среди массива readWidge id элемента с значением найденного минимума
    fun dateMinRes():Int{
        val a = readWidge()
        val b = IDminDT()

        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance()
        val userDate = Calendar.getInstance()
        userDate.time = inputFormat.parse(a[b].valueDT)

        val difference = userDate.timeInMillis - currentDate.timeInMillis
        val daysLeft = difference / (24 * 60 * 60 * 1000)
        val daysLef = daysLeft.toInt()

        return daysLef
    } // Нахождение разницы между днем x и сегоднешним днем
}