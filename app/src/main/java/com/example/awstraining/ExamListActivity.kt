package com.example.awstraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_exam_list.*
import kotlinx.android.synthetic.main.activity_main.*

class ExamListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_list)

        buttonFive.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 5)
            startActivity(intent)
        }

        buttonTen.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 10)
            startActivity(intent)
        }

        buttonFifteen.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 15)
            startActivity(intent)
        }

        buttonTwenty.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 20)
            startActivity(intent)
        }
        buttonThirty.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 30)
            startActivity(intent)
        }


        buttonFifty.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            // Go to activity
            intent.putExtra("NUM_OF_QUESTIONS", 50)
            startActivity(intent)
        }
    }

}
