package com.example.vpplan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RemoteViews
import android.widget.SearchView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vpplan.packag.MyAdapter
import com.example.vpplan.packag.MyDBManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        initSearchView()

        val a = findViewById<FloatingActionButton>(R.id.fbNew)
        a.setColorFilter(Color.argb(255, 255, 255, 255));
        Uvedomlenie()
    }
    private fun Uvedomlenie(){
        val sdf = SimpleDateFormat("dd/M/yyyy ")//hh:mm:ss
        val currentDate = sdf.format(Date())

        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("$currentDate : Привет! Появились новые дела? ")
            .setContentText("Записывай новые дела в приложении и получай информацию о оставшемся количестве дней.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
    override fun onResume() {
        super.onResume()
        myDBManager.openDb()
        fillAdapter()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()

    }
    fun OnClickNew(view: View) {
        val i = Intent(this, MainActivity2::class.java)
        startActivity(i)
    }
    fun init(){
        val i = findViewById<RecyclerView>(R.id.rcView)
        i.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(i)
        i.adapter = myAdapter
    }
    private fun initSearchView(){
        val sv = findViewById<SearchView>(R.id.searchView)
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val list = myDBManager.readDbData(newText!!)
                myAdapter.upDateAdapter(list)
                return true
            }
        }) // Сортировка при нажатии на поиск и при изменениии внутри поиска
    }
    fun fillAdapter(){
        val NE: TextView = findViewById(R.id.tvNoElements)
        val list = myDBManager.readDbData("")
        myAdapter.upDateAdapter(list)

        if(list.size > 0){
            NE.visibility = View.GONE
        } else {NE.visibility = View.VISIBLE}
    }
    private fun getSwapMg():ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDBManager)
            }
        })
    }
}


