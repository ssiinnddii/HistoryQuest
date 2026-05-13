package com.up.projectmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.up.projectmanager.databinding.ActivityResultBinding
import com.up.projectmanager.model.Subject

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var score = 0
    private var total = 0
    private lateinit var subject: Subject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        score = intent.getIntExtra(MainActivity.EXTRA_SCORE, 0)
        total = intent.getIntExtra(MainActivity.EXTRA_TOTAL, 5)
        subject = Subject.valueOf(
            intent.getStringExtra(MainActivity.EXTRA_SUBJECT) ?: Subject.ANCIENT.name
        )

        displayResults()
        setupButtons()
    }

    private fun displayResults() {
        val percentage = score.toFloat() / total * 100
        val stars = when {
            percentage >= 90 -> 3
            percentage >= 60 -> 2
            else -> 1
        }
        updateStars(stars)

        binding.tvScore.text = getString(R.string.score_result, score, total)
        binding.tvSubject.text = "${subject.displayName} Quiz"

        val message = when (stars) {
            3 -> getString(R.string.message_amazing)
            2 -> getString(R.string.message_good)
            else -> getString(R.string.message_try_again)
        }
        binding.tvMessage.text = message
    }

    private fun updateStars(count: Int) {
        val stars = listOf(binding.star1, binding.star2, binding.star3)
        stars.forEachIndexed { index, view ->
            view.setImageResource(
                if (index < count) R.drawable.bg_star_filled
                else R.drawable.bg_star_empty
            )
        }
    }

    private fun setupButtons() {
        binding.btnPlayAgain.setOnClickListener {
            finish()
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
            finish()
        }

        binding.btnShare.setOnClickListener {
            val shareText = getString(
                R.string.share_text, score, total, subject.displayName
            )
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }
    }
}
