package com.example.vpplan

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RemoteViews
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vpplan.packag.MyAdapter
import com.example.vpplan.packag.MyDBManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    val myDBManager = MyDBManager(this)
    val myAdapter = MyAdapter(ArrayList(),this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onResume() {
        super.onResume()
        myDBManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()

    } // Оба важны

    fun OnClickNew(view: View) {
        val i = Intent(this, MainActivity2::class.java)
        startActivity(i)
    }
    fun init(){
        val i = findViewById<RecyclerView>(R.id.rcView)
        i.layoutManager = LinearLayoutManager(this) // Элементы будут распологаться по вертикали
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(i)// Соединяем их вместе
        i.adapter = myAdapter
    }

    fun fillAdapter(){
        val NE: TextView = findViewById(R.id.tvNoElements)
        val list = myDBManager.readDbData()
        myAdapter.upDateAdapter(list)

        if(list.size > 0){
            NE.visibility = View.GONE
        } else {NE.visibility = View.VISIBLE}
    } // Обновление  для resume
    private fun getSwapMg():ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            } // Переаскиваем элемент Не нужно поэтому мы отправляем false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDBManager)

            } // ДЕлаем swipe
        })
    }
}


