package com.up.projectmanager.data

import android.content.Context
import android.content.SharedPreferences
import com.up.projectmanager.model.Difficulty
import com.up.projectmanager.model.Subject

class ScoreManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveScore(subject: Subject, difficulty: Difficulty, score: Int) {
        val key = highScoreKey(subject, difficulty)
        val currentBest = prefs.getInt(key, 0)
        if (score > currentBest) {
            prefs.edit()
                .putInt(key, score)
                .putLong(highScoreDateKey(subject, difficulty), System.currentTimeMillis())
                .apply()
        }
    }

    fun getHighScore(subject: Subject, difficulty: Difficulty): Int {
        return prefs.getInt(highScoreKey(subject, difficulty), 0)
    }

    fun getHighScoreDate(subject: Subject, difficulty: Difficulty): Long {
        return prefs.getLong(highScoreDateKey(subject, difficulty), 0L)
    }

    fun isTtsEnabled(): Boolean = prefs.getBoolean(PREF_TTS_ENABLED, true)

    fun setTtsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PREF_TTS_ENABLED, enabled).apply()
    }

    fun getDifficultyPref(): String {
        return prefs.getString(PREF_DIFFICULTY, Difficulty.EASY.name) ?: Difficulty.EASY.name
    }

    fun setDifficultyPref(diff: String) {
        prefs.edit().putString(PREF_DIFFICULTY, diff).apply()
    }

    fun isSoundEnabled(): Boolean = prefs.getBoolean(PREF_SOUND_ENABLED, true)

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(PREF_SOUND_ENABLED, enabled).apply()
    }

    fun resetAllScores() {
        val editor = prefs.edit()
        for (subject in Subject.values()) {
            for (difficulty in Difficulty.values()) {
                editor.remove(highScoreKey(subject, difficulty))
                editor.remove(highScoreDateKey(subject, difficulty))
            }
        }
        editor.apply()
    }

    private fun highScoreKey(subject: Subject, difficulty: Difficulty): String {
        return "high_score_${subject.name}_${difficulty.name}"
    }

    private fun highScoreDateKey(subject: Subject, difficulty: Difficulty): String {
        return "high_score_date_${subject.name}_${difficulty.name}"
    }

    companion object {
        private const val PREFS_NAME = "brainquest_prefs"
        private const val PREF_TTS_ENABLED = "tts_enabled"
        private const val PREF_DIFFICULTY = "difficulty_pref"
        private const val PREF_SOUND_ENABLED = "sound_enabled"
    }
}
