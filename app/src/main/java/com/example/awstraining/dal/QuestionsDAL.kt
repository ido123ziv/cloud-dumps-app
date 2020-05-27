package com.example.awstraining.dal

import android.content.Context
import android.content.res.AssetManager
import android.widget.Toast
import com.example.awstraining.Answer
import com.example.awstraining.Question
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

fun getDataFromJSON(assets : AssetManager, context: Context) : ArrayList<Question>{// gets the questions into a list from the JSON
    var loq = arrayListOf<Question>() //list of questions
    var json : String
    try {
        var iS : InputStream = assets.open("JsonFiles/aws.json")
        json = iS.bufferedReader().use { it.readText() }
        //  Question2.text = json
        var jsonArr = JSONArray(json)
        for (i in 0..jsonArr.length() -1)
        {
            var jsonobj = jsonArr.getJSONObject(i)
            val q = getQuestionFromJsonObject(jsonobj)
            // printQuestion(q)
            loq.add(q)
        }
    }
    catch (e : Exception)
    {
        Toast.makeText(context,"Exception", Toast.LENGTH_LONG).show()
    }
    return loq
}
fun getQuestionFromJsonObject(jsonobj : JSONObject) : Question { //build a question object from JSON
    val name = jsonobj.getString("Question")
    //  bu4.text = name
    //  val catr = jsonobj.getString("QuestionSubject")
    var listOfCorrect = jsonobj.getJSONArray("Correct")
    var corrects = arrayListOf<Answer>()
    for (i in 0 until listOfCorrect.length()){
        corrects.add(Answer(listOfCorrect[i].toString()))
    }
//    val correct = jsonobj.getInt("Answer")-1
//        bu4.text = correct.toString()
    var l = jsonobj.getJSONArray("Answers")
    var listofa = arrayListOf<Answer>()
    for (i in 0 until l.length()){
        val s = l[i].toString()
//        bu3.text = s
        val a= Answer(s)
        listofa.add(a)
    }
    //var listofab = listofa.shuffled()
    return Question(name,listofa.shuffled(),corrects,"")
}