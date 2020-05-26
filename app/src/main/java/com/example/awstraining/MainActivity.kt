package com.example.awstraining

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val LOADING_DELAY: Long = 3000
    }

    private var mDelayHandler: Handler? = null

    internal class ddRunnable : Runnable {
        override fun run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)

            Thread.currentThread()
        }
    }


    internal val mRunnable: Runnable = Runnable {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND)
        var intent: Intent?
        intent = Intent(this, ExamListActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDelayHandler = Handler(Looper.getMainLooper())
        mDelayHandler!!.postDelayed(mRunnable, LOADING_DELAY)
    }

    override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }


//    fun buOBClick(v: View) {
//        var intent = Intent(this, GameActivity::class.java)
//        startActivity(intent)
//    }


}
