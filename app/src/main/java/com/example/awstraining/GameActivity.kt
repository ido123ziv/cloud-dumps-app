package com.example.awstraining

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awstraining.Suppliers.listOfQuestions
//import com.example.awstraining.Suppliers.saveListOfIncorrect
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    private lateinit var receiver: BroadcastReceiver

    //    lateinit var listOfQuestions: List<Question> // all the questions from the JSON
    lateinit var myList: List<Question> // the questions we will use
    lateinit var selectedAnswers: MutableList<Answer>
    var current_question_index: Int = 0
    private var num_of_questions = 10 //num of questions
    private var score = 0 //player score
    var listOfCorrect = mutableListOf<Question>() // ?
    var listOfInCorrect : MutableList<Question> = mutableListOf()
    var isFirstTry : Boolean = true

    var cond = false // determinates if it was the first incorrect or not
    var lastQ = 0 // the last questions that was displayed
    var nextQ = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val bundle: Bundle? = intent.extras
        bundle?.let {
            num_of_questions = it.getInt("NUM_OF_QUESTIONS")
        }
        score = 0

        Question2.setOnClickListener {
            val currentQuestion = myList[current_question_index]

            if (currentQuestion.corrects.size == selectedAnswers.size) {
                if (current_question_index < myList.size - 1) {
                    next_Question()
                }
            }
        }
        myList = listOf()
//        listOfQuestions = getDataFromJSON(assets, applicationContext).shuffled()
        var setMyList = listOfQuestions.shuffled().take(num_of_questions)
        myList = copyListEntirely(setMyList)
        // print first question
        current_question_index = 0
        print_question(myList[current_question_index])
        selectedAnswers = mutableListOf()

        receiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(p0: Context?, p1: Intent?) {
                recycler_view_Answers.requestLayout()
                val currentQuestion = myList[current_question_index]
                val answerText = p1?.getStringExtra("answer")
//                isFirstTry = true
                val answer = currentQuestion.la.find { a -> a.answer == answerText }
                // Question2.text = listOfQuestions[1].q

                if (isCorrect(answer!!)) {
                    answer.backgroundColor = Color.GREEN

                    selectedAnswers.add(answer)
                    // if all the answers were selected
                    recycler_view_Answers.apply {
                        layoutManager = LinearLayoutManager(this@GameActivity)
                        adapter = AnswerAdapter(currentQuestion.la)
                    }
                    if (currentQuestion.corrects.size == selectedAnswers.size) {
                        if (!isAlreadyWrong(currentQuestion)) {
                            listOfCorrect.add(currentQuestion)
                        }
                        if (current_question_index < myList.size - 1) {
                            InvokeWithDelay(500) {
                                next_Question()
                            }.execute("")
                        } else {
                            val res = "${listOfCorrect.size}/$num_of_questions"
                            recycler_view_Answers.apply {
                                layoutManager = LinearLayoutManager(this@GameActivity)
                                adapter = AnswerAdapter(listOf())
                            }
                            if (listOfCorrect.size == num_of_questions) {
                                Question2.text =
                                    "Your score is $res \nTouch here to return to the main menu"
//                                Question2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.aws, 0, 0, 0 )
//                                android:drawableEnd="@mipmap/ic_launcher_foreground"

                                Question2.setOnClickListener {
                                    getBackToPickLength()
                                }
                            } else {
                                Question2.text = "Your score is $res \nTouch here to create a game of incorrect"
                                Question2.setOnClickListener {
                                    createNewListFromIncorrect()
                                }

                            }
                        }
                    }
                } else {
                    if (isFirstTry) {
                        listOfInCorrect.add(currentQuestion)
                    }

                    answer.backgroundColor = Color.RED
                    isFirstTry = false
                    recycler_view_Answers.apply {
                        layoutManager = LinearLayoutManager(this@GameActivity)
                        adapter = AnswerAdapter(currentQuestion.la)
                    }
                    var listOfCorr = currentQuestion.corrects
                    correctAnswerPopUp(getStringOfAllCorrectFromList(listOfCorr))
                }


//                Toast.makeText(applicationContext, "here 1", Toast.LENGTH_LONG)
//                when (p1?.action) {
//                    "custom-message" -> {
//                        Toast.makeText(applicationContext, "here 2", Toast.LENGTH_LONG)
//                    }


            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("custom-message"))
    }
    fun getBackToPickLength(){
//        val intent = Intent(this, ExamListActivity::class.java)
//        startActivity(intent)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        finish()
//        this.onBackPressed()
    }

    override fun onBackPressed() {
        getBackToPickLength()
    }

    fun print_question(question: Question){
        Question2.text = "${current_question_index + 1}: ${question.q}"
        recycler_view_Answers.apply {
            layoutManager = LinearLayoutManager(this@GameActivity)
            adapter = AnswerAdapter(question.la)
        }
    }

    fun next_Question() {
        isFirstTry = true
        current_question_index += 1
        var q : Question = myList[current_question_index]
        selectedAnswers = mutableListOf()
        print_question(q)
    }

    fun isCorrect(a: Answer) : Boolean{
        val currQuestion : Question = myList[current_question_index]
        for (c in currQuestion.corrects){
            if (a.answer == c.answer)
                return true
        }
        return false
    }

    fun isAlreadyWrong(question: Question) : Boolean{
        var isFound = false
        for (icq in listOfInCorrect){
            if (icq.q == question.q)
                return true
        }
        return isFound
    }
    fun copyListEntirely(questionList :List<Question>) : List<Question>{
        var newList : MutableList<Question> = mutableListOf()
        for (q in questionList){
            newList.add(Question(q.q, q.la, q.corrects, q.subject))

            for (a in q.la) {
                a.resetColor()
            }
        }
        return newList
    }

    fun correctAnswerPopUp(answerText : String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Correct Answer is:")
            setMessage(answerText)
            setPositiveButton("", DialogInterface.OnClickListener { dialog, which ->
            })
            show()

        }
    }

    fun getStringOfAllCorrectFromList(listOfCorr : List<Answer>) : String{
        var str = ""
        for (i in listOfCorr){
            str += i.answer + "\n"
        }
        return str
    }

    fun createGameFromIncorrect(){
        myList = listOfInCorrect
        myList = clearList(myList)
        listOfInCorrect = mutableListOf()
        listOfCorrect = mutableListOf()
        num_of_questions = myList.size
        current_question_index = 0
        print_question(myList[current_question_index])
        selectedAnswers = mutableListOf()
        Question2.setOnClickListener {
            if (current_question_index < myList.size - 1)
                next_Question() }
    }

    fun createNewListFromIncorrect() {
        var newList = mutableListOf<Question>()
        for (q in listOfInCorrect){
            var newAns = mutableListOf<Answer>()
            for (a in q.la) {
                newAns.add(Answer(a.answer))
            }
            newList.add(Question(q.q, newAns, q.corrects, q.subject))
        }
        myList = newList

        listOfInCorrect = mutableListOf()
        listOfCorrect = mutableListOf()
        num_of_questions = myList.size
        current_question_index = 0
        print_question(myList[current_question_index])
        selectedAnswers = mutableListOf()
        Question2.setOnClickListener {
            if (current_question_index < myList.size - 1)
                next_Question() }
    }

    fun clearList(listOfQuestion: List<Question>) : List<Question>{
        for (q in listOfQuestion) {
            for (a in q.la){
                a.resetColor()
            }
        }
        return listOfQuestion
    }

    companion object {
        class InvokeWithDelay(private var ms: Int, private var cbOnFinish: () -> Unit) : AsyncTask<String, Unit, String>() {
            override fun onPreExecute() {
            }

            override fun doInBackground(vararg params: String?): String {
                Thread.sleep(ms.toLong())
                return ""
            }

            override fun onPostExecute(result: String?) {
                cbOnFinish.invoke()
            }
        }
    }
}

//    /* logic */
//    fun getButtonFromID(i : Int): Button { //converts id to button
//        when(i){
//            0 -> return bu1
//            1 -> return bu2
//            2 -> return bu3
//            3 -> return bu4
//        }
//        return bu1
//    }
//    protected fun GameClickButton(v: View) { // when a button is clicked
//        //Toast.makeText(this,"Exception1",Toast.LENGTH_LONG).show()
//        val buSelected = v as Button
//        var cellID = 0
//        when(buSelected.id){
//            R.id.bu1 -> cellID = 0
//            R.id.bu2 -> cellID = 1
//            R.id.bu3 -> cellID = 2
//            R.id.bu4 -> cellID = 3
//        }
//        playGame(cellID,buSelected)
//    }
//    fun playGame(cellID : Int, buSelected : Button){
//        checkAndPrintCorrectIn(cellID,buSelected)
//    }
//    fun nextQuestion() {
//        if (nextQ < myList.size){
//            lastQ = nextQ
//            cleanMyMess(myList[nextQ])
//            nextQ +=1
//            firstTry = true
//        }
//        else
//            printExit()
//    }
//    fun checkAndPrintCorrectIn(cellID : Int, buSelected : Button){
//        var question = myList[lastQ]
//        if (question.isCorrect(cellID))
//        {
//            if (firstTry){
//                listOfCorrect.add(lastQ)
//                score += 1
//            }
//            printCorrect(buSelected)
//        }
//        else{
//            firstTry = false
//            printInCorrect(buSelected)
//            if (!cond)
//                cond = true
//            else
//            {
//                val cID = question.correctID
//                val b = getButtonFromID(cID)
//                printCorrect(b)
//                cond = false
//            }
//        }
//    }
//    /*Graphic*/
//    fun printFirst(){ //place holder
//        Question2.text = "Press Me to Begin"
//        bu1.text = "Press Up to begin"
//        bu2.text = "Press Up to begin"
//        bu3.text = "Press Up to begin"
//        bu4.text = "Press Up to begin"
//    }
//    fun printExit(){ //prints the exit of the screen
//        var x = myList.size
//        Question2.text = "Your Score is $score/$x" //+ listOfCorrect
//        bu1.text ="Exit"
//        bu1.setOnClickListener {
//            var intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//            this.finish()
//        }
//        bu2.visibility = View.GONE
//        bu3.visibility = View.GONE
//        bu4.visibility = View.GONE
//    }
//    fun cleanMyMess(q :Question){ // print next question nicely
//        printQuestion(q)
//        bu1.setBackgroundColor(resources.getColor(R.color.button2))
//        bu2.setBackgroundColor(resources.getColor(R.color.button2))
//        bu3.setBackgroundColor(resources.getColor(R.color.button2))
//        bu4.setBackgroundColor(resources.getColor(R.color.button2))
//    }
//    fun printCorrect( buSelected : Button){
//        buSelected.setBackgroundColor(Color.GREEN)
//    }
//    fun printInCorrect(buSelected: Button){
//        buSelected.setBackgroundColor(Color.RED)
//    }
//    fun printQuestion(q: Question){//print the question and answers
////        arthur = true
//        Question2.text = "Question # ${lastQ+1}\n" + q.question
//        var listofa = q.ListOfAnswers
//        bu1.text = listofa[0].answer
//        bu2.text = listofa[1].answer
//        bu3.text = listofa[2].answer
//        bu4.text = listofa[3].answer
//    }
//    /* DAL */
//    fun pickQuestions(num :Int): ArrayList<Question>{ //picks random $num questions
//        var num1 = num -1
//        val rnd = Random()
//        var loq = listOfQuestions.shuffled(rnd)
//        var newList = arrayListOf<Question>()
//        for (i in 0..num1){
//            val q : Question =loq[i]
//            q.shuffleMe()
//            newList.add(q)
//        }
//        return newList
//    }
//
//    fun getDataFromJSON() : ArrayList<Question>{// gets the questions into a list from the JSON
//        var loq = arrayListOf<Question>()
//        var json : String
//        try {
//            var iS : InputStream = assets.open("aws.json")
//            json = iS.bufferedReader().use { it.readText() }
//            //  Question2.text = json
//            var jsonArr = JSONArray(json)
//            for (i in 0..jsonArr.length() -1)
//            {
//                var jsonobj = jsonArr.getJSONObject(i)
//                val q = getQuestionFromJsonObject(jsonobj)
//                // printQuestion(q)
//                loq.add(q)
//            }
//        }
//        catch (e : Exception)
//        {
//            Toast.makeText(this,"Exception", Toast.LENGTH_LONG).show()
//            bu1.text = e.toString()
//        }
//        return loq
//    }
//    fun getQuestionFromJsonObject(jsonobj : JSONObject) : Question { //build a question object from JSON
//        val name = jsonobj.getString("Question")
//        //  bu4.text = name
//        //  val catr = jsonobj.getString("QuestionSubject")
//        val correct = jsonobj.getInt("Answer")-1
////        bu4.text = correct.toString()
//        var l = jsonobj.getJSONArray("Answers")
//        var listofa = arrayListOf<Answer>()
//        for (i in 0..l.length() -1){
//            val s = l[i].toString()
//            bu3.text = s
//            val a= Answer(s)
//            listofa.add(a)
//        }
//        //var listofab = listofa.shuffled()
//        return Question(name,listofa,correct,"")
//    }
