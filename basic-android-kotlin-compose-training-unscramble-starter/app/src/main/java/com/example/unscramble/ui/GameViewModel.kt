package com.example.unscramble.ui

import com.example.unscramble.data.allWords
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.update

data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    var userGuess by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    private fun pickRandomWordAndShuffle(): String {
        do {
            currentWord = allWords.random()
        } while (usedWords.contains(currentWord))
        usedWords.add(currentWord)
        return shuffleWord(currentWord)
    }

    private fun shuffleWord(word: String): String {
        val shuffled = word.toCharArray().apply { shuffle() }
        return if (String(shuffled) != word) String(shuffled) else shuffleWord(word)
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess() {

        if (userGuess.equals(currentWord, ignoreCase = true)) {
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        // Reset user guess
        updateUserGuess("")
        }
    }
}

