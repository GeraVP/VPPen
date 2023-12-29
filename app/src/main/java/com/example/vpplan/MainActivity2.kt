package com.example.vpplan

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.vpplan.R.id.edTitle
import com.example.vpplan.R.id.imMyImage
import com.example.vpplan.R.id.mainImageLay
import com.example.vpplan.packag.MyDBManager

class MainActivity2 : AppCompatActivity() {
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val myDBManager = MyDBManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

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
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    } // Функция открытия галереи
    fun onClickSave(view: View) {
        val title = findViewById<EditText>(R.id.edTitle)
        val desc = findViewById<EditText>(R.id.edDesc)
        if(title.text.toString() != "" && desc.text.toString() != "")
        {
            myDBManager.insertToDb(title.text.toString(),desc.text.toString(),tempImageUri) // Добавление в БД
        }
    } // Функция сохранения
}