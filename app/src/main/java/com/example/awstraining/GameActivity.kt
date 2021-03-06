package com.example.awstraining

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class GameActivity : AppCompatActivity() {
    var listOfQuestions = arrayListOf<Question>() // all the questions from the JSON
    var myList = arrayListOf<Question>() // the questions we will use
    var lastQ = 0 // the last questions that was displayed
    var nextQ = 0
    var listOfCorrect = arrayListOf<Int>() // ?
    private var max = 10 //num of questions
    private var score = 0 //player score
    var cond = false // determinates if it was the first incorrect or not
    var firstTry = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val bundle: Bundle? = intent.extras
        bundle?.let {
            max = it.getInt("NUM_OF_QUESTIONS")
        }
        listOfQuestions = getDataFromJSON()
        myList = pickQuestions(max)
        // Question2.text= listOfQuestions[0].subject
        nextQuestion()
        Question2.setOnClickListener { nextQuestion() }
    }
    /* logic */
    fun getButtonFromID(i : Int): Button { //converts id to button
        when(i){
            0 -> return bu1
            1 -> return bu2
            2 -> return bu3
            3 -> return bu4
        }
        return bu1
    }
    protected fun GameClickButton(v: View) { // when a button is clicked
        //Toast.makeText(this,"Exception1",Toast.LENGTH_LONG).show()
        val buSelected = v as Button
        var cellID = 0
        when(buSelected.id){
            R.id.bu1 -> cellID = 0
            R.id.bu2 -> cellID = 1
            R.id.bu3 -> cellID = 2
            R.id.bu4 -> cellID = 3
        }
        playGame(cellID,buSelected)
    }
    fun playGame(cellID : Int, buSelected : Button){
        checkAndPrintCorrectIn(cellID,buSelected)
    }
    fun nextQuestion() {
        if (nextQ < myList.size){
            lastQ = nextQ
            cleanMyMess(myList[nextQ])
            nextQ +=1
            firstTry = true
        }
        else
            printExit()
    }
    fun checkAndPrintCorrectIn(cellID : Int, buSelected : Button){
        var question = myList[lastQ]
        if (question.isCorrect(cellID))
        {
            if (firstTry){
                listOfCorrect.add(lastQ)
                score += 1
            }
            printCorrect(buSelected)
        }
        else{
            firstTry = false
            printInCorrect(buSelected)
            if (!cond)
                cond = true
            else
            {
                val cID = question.correctID
                val b = getButtonFromID(cID)
                printCorrect(b)
                cond = false
            }
        }
    }
    /*Graphic*/
    fun printFirst(){ //place holder
        Question2.text = "Press Me to Begin"
        bu1.text = "Press Up to begin"
        bu2.text = "Press Up to begin"
        bu3.text = "Press Up to begin"
        bu4.text = "Press Up to begin"
    }
    fun printExit(){ //prints the exit of the screen
        var x = myList.size
        Question2.text = "Your Score is $score/$x" //+ listOfCorrect
        bu1.text ="Exit"
        bu1.setOnClickListener {
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        bu2.visibility = View.GONE
        bu3.visibility = View.GONE
        bu4.visibility = View.GONE
    }
    fun cleanMyMess(q :Question){ // print next question nicely
        printQuestion(q)
        bu1.setBackgroundColor(resources.getColor(R.color.button2))
        bu2.setBackgroundColor(resources.getColor(R.color.button2))
        bu3.setBackgroundColor(resources.getColor(R.color.button2))
        bu4.setBackgroundColor(resources.getColor(R.color.button2))
    }
    fun printCorrect( buSelected : Button){
        buSelected.setBackgroundColor(Color.GREEN)
    }
    fun printInCorrect(buSelected: Button){
        buSelected.setBackgroundColor(Color.RED)
    }
    fun printQuestion(q: Question){//print the question and answers
//        arthur = true
        Question2.text = "Question # ${lastQ+1}\n" + q.question
        var listofa = q.ListOfAnswers
        bu1.text = listofa[0].answer
        bu2.text = listofa[1].answer
        bu3.text = listofa[2].answer
        bu4.text = listofa[3].answer
    }
    /* DAL */
    fun pickQuestions(num :Int): ArrayList<Question>{ //picks random $num questions
        var num1 = num -1
        val rnd = Random()
        var loq = listOfQuestions.shuffled(rnd)
        var newList = arrayListOf<Question>()
        for (i in 0..num1){
            val q : Question =loq[i]
            q.shuffleMe()
            newList.add(q)
        }
        return newList
    }

    fun getDataFromJSON() : ArrayList<Question>{// gets the questions into a list from the JSON
        var loq = arrayListOf<Question>()
        var json : String
        try {
            var iS : InputStream = assets.open("aws.json")
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
            Toast.makeText(this,"Exception", Toast.LENGTH_LONG).show()
            bu1.text = e.toString()
        }
        return loq
    }
    fun getQuestionFromJsonObject(jsonobj : JSONObject) : Question { //build a question object from JSON
        val name = jsonobj.getString("Question")
        //  bu4.text = name
        //  val catr = jsonobj.getString("QuestionSubject")
        val correct = jsonobj.getInt("Answer")-1
//        bu4.text = correct.toString()
        var l = jsonobj.getJSONArray("Answers")
        var listofa = arrayListOf<Answer>()
        for (i in 0..l.length() -1){
            val s = l[i].toString()
            bu3.text = s
            val a= Answer(s)
            listofa.add(a)
        }
        //var listofab = listofa.shuffled()
        return Question(name,listofa,correct,"")
    }

}