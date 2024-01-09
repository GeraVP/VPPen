package com.example.vpplan

import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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

class MainActivity2 : AppCompatActivity() {
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val myDBManager = MyDBManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        getMyIntents()
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
        }
    }
    fun onClickAddImage(view: View) {

        val yourElement = findViewById<View>(R.id.mainImageLay)
        if (yourElement.visibility !== view.visibility) {
            yourElement.visibility = View.VISIBLE
        } else
        {
            yourElement.visibility = View.GONE
            Toast.makeText(this, "Закрыл", Toast.LENGTH_SHORT).show()
        }

    }
    fun onClickChooseImage(view: View) {
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent, imageRequestCode)
    } // Функция открытия галереи ССЫЛКА НЕ ВРЕМЕННАЯ
    fun onClickSave(view: View) {
        val title = findViewById<EditText>(R.id.edTitle)
        val desc = findViewById<EditText>(R.id.edDesc)
        if(title.text.toString() != "" && desc.text.toString() != "")
        {
            myDBManager.insertToDb(title.text.toString(),desc.text.toString(),tempImageUri) // Добавление в БД
            finish() // Закрытие activity
        }
    } // Функция сохранения
    fun getMyIntents(){
        val title = findViewById<EditText>(R.id.edTitle)
        val desc = findViewById<EditText>(R.id.edDesc)
        val yourElement = findViewById<View>(R.id.mainImageLay)
        val image = findViewById<ImageView>(R.id.imMyImage)
        val buttadd = findViewById<FloatingActionButton>(R.id.flb)

        val i = intent
        if(i != null){
            if(i.getStringExtra(MyIntentConstants.I_title) != null){ // Ничего нет
                buttadd.visibility = View.GONE
                title.setText(i.getStringExtra(MyIntentConstants.I_title))
                    desc.setText(i.getStringExtra(MyIntentConstants.I_desc))
                if(i.getStringExtra(MyIntentConstants.I_URI) != "empty"){
                    yourElement.visibility = View.VISIBLE

                    image.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI)))

                }
            }
        }
    }
}