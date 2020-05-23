package com.example.awstraining

class Question (var q : String, var la : List<Answer>, var corrects : List<Answer>, var subject : String) {

    private fun isAnsCorrect(a : Answer) : Boolean {
        return corrects.contains(a)
    }
    fun isCorrect(i : Int) : Boolean{
        return isAnsCorrect(la[i])
    }
//    private fun shuffleListOfAnswers(la : ArrayList<Answer>) : ArrayList<Answer>{
//        var listOfRandoms = la.shuffled()
//        var newList = arrayListOf<Answer>()
//        var counter = 0
//        for (i in listOfRandoms){
//            if (i == correctAnswer)
//                changeIDForCorrect(counter)
//            newList.add(i)
//            counter += 1
//        }
//        return newList
//    }
//    private fun changeIDForCorrect(c : Int){
//        this.correctID = c
////    }
//    fun shuffleMe(){
//        this.ListOfAnswers = shuffleListOfAnswers(this.ListOfAnswers)
//    }
}