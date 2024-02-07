package com.example.vpplan

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.ButtonBarLayout
import com.example.vpplan.R.id.edTitle
import com.example.vpplan.R.id.imMyImage
import com.example.vpplan.R.id.mainImageLay
import com.example.vpplan.packag.MyDBManager
import com.example.vpplan.packag.MyIntentConstants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity2 : AppCompatActivity() {
    var id = 0
    var isEditState = false
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val myDBManager = MyDBManager(this)

    var date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        getMyIntents()

        val calendarView = findViewById<CalendarView>(R.id.calendarView1)
        calendarView.setOnDateChangeListener{_, year, month, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${month + 1}/${year}"
            date = selectedDate.toString()
        }
    }
    override fun onResume() {
        super.onResume()
        myDBManager.openDb()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val im = findViewById<ImageView>(R.id.imMyImage)
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == imageRequestCode){
            im.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!,Intent.FLAG_GRANT_READ_URI_PERMISSION) // !!!!
        }
    }
    fun onClickAddImage(view: View) {
        val yourElement = findViewById<View>(R.id.mainImageLay)
        if (yourElement.visibility !== view.visibility) {
            yourElement.visibility = View.VISIBLE
        } else
        {
            yourElement.visibility = View.GONE
            Toast.makeText(this, "Закрыто окно выбора изображения", Toast.LENGTH_SHORT).show()
        }
    }
    fun onClickChooseImage(view: View) {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    } // Функция открытия галереи ССЫЛКА НЕ ВРЕМЕННАЯ
    fun onClickSave(view: View) {
        val title = findViewById<EditText>(R.id.edTitle)

        if(title.text.toString() != "" && date.toString() != "")
        {
            if(isEditState){
                myDBManager.UpdateItem(title.text.toString(),date,tempImageUri,id) // Обновления в БД
                }else{
            myDBManager.insertToDb(title.text.toString(),date,tempImageUri) // Добавление в БД
                }
            finish() // Закрытие activity
        }
    }

    fun OnEditEnable(view: View){
        val title = findViewById<EditText>(R.id.edTitle)
        val desc = findViewById<CalendarView>(R.id.calendarView1)
        val fbedi = findViewById<FloatingActionButton>(R.id.fbedit)
        title.isEnabled = true
        desc.isEnabled = true
        fbedi.visibility = View.GONE

    } // Функция при нажатии на элемент
    fun getMyIntents(){
        val title = findViewById<EditText>(R.id.edTitle)
        val desc = findViewById<CalendarView>(R.id.calendarView1)
        val fbedit = findViewById<FloatingActionButton>(R.id.fbedit)

        val yourElement = findViewById<View>(R.id.mainImageLay)
        val image = findViewById<ImageView>(R.id.imMyImage)
        val buttadd = findViewById<FloatingActionButton>(R.id.flb)

        val i = intent
        if(i != null){
            if(i.getStringExtra(MyIntentConstants.I_title) != null){ // Ничего нет
                buttadd.visibility = View.GONE
                title.setText(i.getStringExtra(MyIntentConstants.I_title)) // Заполнение поля title из бд

                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val userDate = Calendar.getInstance()
                userDate.time = inputFormat.parse(i.getStringExtra(MyIntentConstants.I_desc))
                desc.date = userDate.timeInMillis // Автоматический выбор даты в calendarview из бд

                isEditState = true // Если в бд не пусто, то переменная bool  становится true

                title.isEnabled = false // Запрет на изменение

                fbedit.visibility = View.VISIBLE // Включаем кнопку изменения

                id = i.getIntExtra((MyIntentConstants.I_ID),0)
                if(i.getStringExtra(MyIntentConstants.I_URI) != "empty"){
                    yourElement.visibility = View.VISIBLE
                    image.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI)))
                }
            } else { // Если в бд ничего нет (title пустой), то кнопка изменения пропадает.
                fbedit.visibility = View.GONE
            }
        }
    }
}