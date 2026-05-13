package com.up.projectmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.up.projectmanager.databinding.ActivityMainBinding
import com.up.projectmanager.model.Subject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardAncient.setOnClickListener {
            startQuiz(Subject.ANCIENT)
        }

        binding.cardMedieval.setOnClickListener {
            startQuiz(Subject.MEDIEVAL)
        }

        binding.cardModern.setOnClickListener {
            startQuiz(Subject.MODERN)
        }

        binding.btnLeaderboard.setOnClickListener {
            startActivity(Intent(this, LeaderboardActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun startQuiz(subject: Subject) {
        val intent = Intent(this, QuizActivity::class.java).apply {
            putExtra(EXTRA_SUBJECT, subject.name)
        }
        startActivity(intent)
    }

    companion object {
        const val EXTRA_SUBJECT = "EXTRA_SUBJECT"
        const val EXTRA_SCORE = "EXTRA_SCORE"
        const val EXTRA_TOTAL = "EXTRA_TOTAL"
        const val EXTRA_DIFFICULTY = "EXTRA_DIFFICULTY"
    }
}
