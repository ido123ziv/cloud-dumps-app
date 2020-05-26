package com.example.awstraining

import android.graphics.Color

class Answer(a : String, var backgroundColor: Int = Color.WHITE) {
    val answer: String = a

    override fun toString(): String {
        return this.answer
    }
}