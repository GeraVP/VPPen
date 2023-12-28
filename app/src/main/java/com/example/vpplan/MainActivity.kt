package com.example.vpplan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.vpplan.packag.MyDBManager



class MainActivity : AppCompatActivity() {
    val myDBManager = MyDBManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onResume() {
        super.onResume()
        myDBManager.openDb()



        }

    fun OnClickNew(view: View) {
        val i = Intent(this,MainActivity2::class.java)
        startActivity(i)
    }
    override fun onDestroy() {
        super.onDestroy()
        myDBManager.closeDb()

    }
}
