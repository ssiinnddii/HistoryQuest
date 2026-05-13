package com.up.projectmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.up.projectmanager.data.ScoreManager
import com.up.projectmanager.databinding.ActivityLeaderboardBinding
import com.up.projectmanager.model.Difficulty
import com.up.projectmanager.model.Subject

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var scoreManager: ScoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoreManager = ScoreManager(this)

        binding.btnTabAncient.setOnClickListener { showScores(Subject.ANCIENT) }
        binding.btnTabMedieval.setOnClickListener { showScores(Subject.MEDIEVAL) }
        binding.btnTabModern.setOnClickListener { showScores(Subject.MODERN) }

        showScores(Subject.ANCIENT)
    }

    private fun showScores(subject: Subject) {
        val easyScore = scoreManager.getHighScore(subject, Difficulty.EASY)
        val mediumScore = scoreManager.getHighScore(subject, Difficulty.MEDIUM)
        val hardScore = scoreManager.getHighScore(subject, Difficulty.HARD)

        binding.tvEasyScore.text = "${getString(R.string.easy)}: $easyScore/5"
        binding.tvMediumScore.text = "${getString(R.string.medium)}: $mediumScore/5"
        binding.tvHardScore.text = "${getString(R.string.hard)}: $hardScore/5"
    }
}
