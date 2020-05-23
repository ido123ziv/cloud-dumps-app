package com.example.awstraining

class Question (q : String, la : ArrayList<Answer>, correct : Int, subject : String) {
    val question: String = q
    var correctAnswer : Answer = la[correct]
    var ListOfAnswers: ArrayList<Answer> = la
    val subject: String = subject
    var correctID = correct

    private fun isAnsCorrect(a : Answer) : Boolean{
        return a.answer == correctAnswer.answer
    }
    fun isCorrect(i : Int) : Boolean{
        return isAnsCorrect(ListOfAnswers[i])
    }
    private fun shuffleListOfAnswers(la : ArrayList<Answer>) : ArrayList<Answer>{
        var listOfRandoms = la.shuffled()
        var newList = arrayListOf<Answer>()
        var counter = 0
        for (i in listOfRandoms){
            if (i == correctAnswer)
                changeIDForCorrect(counter)
            newList.add(i)
            counter += 1
        }
        return newList
    }
    private fun changeIDForCorrect(c : Int){
        this.correctID = c
    }
    fun shuffleMe(){
        this.ListOfAnswers = shuffleListOfAnswers(this.ListOfAnswers)
    }
}