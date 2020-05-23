package com.example.awstraining

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView

class AnswerAdapter(private val list: List<Answer>) : RecyclerView.Adapter<AnswerViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        context = parent.context

        val inflater = LayoutInflater.from(parent.context)
        return AnswerViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val answer = list[position]
        holder.bind(answer) {
            val intent = Intent("custom-message")
            intent.putExtra("answer", answer.answer)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }
}