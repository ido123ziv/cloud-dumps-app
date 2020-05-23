package com.example.awstraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 5)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 15)
            startActivity(intent)
        }

        button3.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 30)
            startActivity(intent)
        }


        button4.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 50)
            startActivity(intent)
        }

    }

//    fun buOBClick(v: View) {
//        var intent = Intent(this, GameActivity::class.java)
//        startActivity(intent)
//    }


}
