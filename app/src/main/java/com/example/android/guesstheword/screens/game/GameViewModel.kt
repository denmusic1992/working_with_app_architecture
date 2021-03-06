package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current word
    private var _word = MutableLiveData<String>()

    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private var _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private var timer: CountDownTimer

    private var _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private var _buzz = MutableLiveData<BuzzType>()
    val buzz: LiveData<BuzzType>
        get() = _buzz

    init {
        Log.i("GameViewModel", "GameViewModel created!")
        _score.value = 0
        _eventGameFinish.value = false

        timer = object : CountDownTimer(COUNTDOWN, SECOND) {
            override fun onFinish() {
                _buzz.value = BuzzType.GAME_OVER
                _eventGameFinish.value = true
            }

            override fun onTick(p0: Long) {
                _currentTime.value = p0 / SECOND
            }

        }.start()

        resetList()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _buzz.value = BuzzType.PANIC
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _buzz.value = BuzzType.CORRECT
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    fun onBuzzFinish() {
        _buzz.value = BuzzType.NO_BUZZ
    }

    companion object {
        // Time to over
        const val DONE = 0L

        // Time of second
        const val SECOND = 1000L

        // Time to start
        const val COUNTDOWN = 20000L

        private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
        private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
        private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
        private val NO_BUZZ_PATTERN = longArrayOf(0)
    }


    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }
}