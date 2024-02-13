package com.example.vpplan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vpplan.packag.MyAdapter
import com.example.vpplan.packag.MyDBManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    val myDBManager = MyDBManager(this)
    val myAdapter = MyAdapter(ArrayList(),this)
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initSearchView()

        startBackgroundWork()

        val a = findViewById<FloatingActionButton>(R.id.fbNew)
        a.setColorFilter(Color.argb(255, 255, 255, 255));
    }

    private fun startBackgroundWork() {
        val  myWorkRequest = PeriodicWorkRequestBuilder<MyWorker>(30, TimeUnit.MINUTES, 25, TimeUnit.MINUTES).build()
        /*val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .addTag("Uvedomlenie1")
            .setInitialDelay(10,TimeUnit.SECONDS)
            .build()*/
        WorkManager.getInstance(applicationContext).enqueue(myWorkRequest)
    }
    override fun onResume() {
        super.onResume()
        myDBManager.openDb()
        fillAdapter("")
        /*fillAdapter()*/
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
                fillAdapter(newText!!)
                /*val list = myDBManager.readDbData(newText!!)
                myAdapter.upDateAdapter(list)*/
                return true
            }
        }) // Сортировка при нажатии на поиск и при изменениии внутри поиска
    }
    private fun fillAdapter(text:String){
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val NE: TextView = findViewById(R.id.tvNoElements)
            val list = myDBManager.readDbData(text)
            myAdapter.upDateAdapter(list)

            if(list.size > 0){
                NE.visibility = View.GONE
            } else {NE.visibility = View.VISIBLE}
        }

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

class DemoWorker(
    private val appContext: Context,
    params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        delay(5000) //simulate background task
        Log.d("DemoWorker", "do work done!")

        return Result.success()
    }
}

class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    /*val sdf = SimpleDateFormat("dd/M/yyyy ")//hh:mm:ss
    val currentDate = sdf.format(Date())*/
    override fun doWork(): Result {
        // Выполните фоновую работу здесь, например, загрузку данных из сети или обработку данных

        // Отправка уведомления
        sendNotification("Прошло 30 минут. VPDТаймер.")

        return Result.success()
    }

    private fun sendNotification(message: String) {
        // Создание канала уведомлений
        createNotificationChannel()

        // Создание уведомления
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Background Work")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        // Отправка уведомления
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Background Work",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "background_work_channel"
        private const val NOTIFICATION_ID = 1
    }
}
