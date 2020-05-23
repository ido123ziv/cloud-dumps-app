package com.example.awstraining

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AnswerViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
    inflater.inflate(R.layout.question_list_item, parent, false)
) {
    private var mButton: Button? = null

    init {
        mButton = itemView.findViewById(R.id.button5)
    }

    fun bind(answer: Answer, cb: (View) -> Unit) {
        mButton?.text = answer.toString()

        mButton?.setOnClickListener(cb)

        mButton?.setBackgroundColor(answer.backgroundColor)
    }
}