package com.up.projectmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.up.projectmanager.data.ScoreManager
import com.up.projectmanager.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var scoreManager: ScoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        scoreManager = ScoreManager(this)

        setupTtsToggle()
        setupSoundToggle()
        setupDifficulty()
        setupResetButton()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupTtsToggle() {
        binding.switchTts.isChecked = scoreManager.isTtsEnabled()
        binding.switchTts.setOnCheckedChangeListener { _, isChecked ->
            scoreManager.setTtsEnabled(isChecked)
        }
    }

    private fun setupSoundToggle() {
        binding.switchSound.isChecked = scoreManager.isSoundEnabled()
        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            scoreManager.setSoundEnabled(isChecked)
        }
    }

    private fun setupDifficulty() {
        val currentDiff = scoreManager.getDifficultyPref()
        when (currentDiff) {
            "EASY" -> binding.radioEasy.isChecked = true
            "MEDIUM" -> binding.radioMedium.isChecked = true
            "HARD" -> binding.radioHard.isChecked = true
        }

        binding.radioGroupDifficulty.setOnCheckedChangeListener { _, checkedId ->
            val diff = when (checkedId) {
                R.id.radio_easy -> "EASY"
                R.id.radio_medium -> "MEDIUM"
                else -> "HARD"
            }
            scoreManager.setDifficultyPref(diff)
        }
    }

    private fun setupResetButton() {
        binding.btnReset.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.reset_scores)
                .setMessage(R.string.reset_confirm)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    scoreManager.resetAllScores()
                    Toast.makeText(this, R.string.reset_done, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(android.R.string.no, null)
                .show()
        }
    }
}
