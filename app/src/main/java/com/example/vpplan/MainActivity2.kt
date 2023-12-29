package com.example.vpplan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.vpplan.R.id.mainImageLay

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

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
}