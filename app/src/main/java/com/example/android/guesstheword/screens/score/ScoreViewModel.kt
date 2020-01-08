package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {

    private var _finalScore = MutableLiveData<Int>()
    val finalScore: LiveData<Int>
        get() = _finalScore

    private var _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain


    init {
        Log.i("ScoreViewModel", "Final score is $finalScore")
        _finalScore.value = finalScore
        _eventPlayAgain.value = false
    }

    fun setPlayAgain() {
        _eventPlayAgain.value = true
    }

    fun setPlayEventComplete() {
        _eventPlayAgain.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ScoreViewModel", "ScoreViewModel destroyed!")
    }
}