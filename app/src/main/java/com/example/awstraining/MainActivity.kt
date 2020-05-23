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

        awsButton.setOnClickListener {
            if (QuestionsInput.text.toString() == "") {
                Snackbar.make(it, "נא להזין את כמות השאלות", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            } else {
                val intent = Intent(this, GameActivity::class.java)

                // TODO pass parameters
                val n = QuestionsInput.text.toString().toInt()
                intent.putExtra("NUM_OF_QUESTIONS", n)

                // Go to activity
                startActivity(intent)
            }
        }
    }

    fun buOBClick(v: View) {
        var intent = Intent(this, GameActivity::class.java)
        startActivity(intent)


    }

}
