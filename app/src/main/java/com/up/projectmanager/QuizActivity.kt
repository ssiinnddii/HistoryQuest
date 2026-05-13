package com.up.projectmanager

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.up.projectmanager.data.Question
import com.up.projectmanager.data.QuizRepository
import com.up.projectmanager.data.ScoreManager
import com.up.projectmanager.databinding.ActivityQuizBinding
import com.up.projectmanager.model.Difficulty
import com.up.projectmanager.model.Subject
import com.up.projectmanager.util.TTSManager

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var scoreManager: ScoreManager
    private lateinit var ttsManager: TTSManager
    private lateinit var questions: List<Question>
    private var currentIndex = 0
    private var score = 0
    private lateinit var subject: Subject
    private lateinit var difficulty: Difficulty
    private var isAnswered = false
    private var hintUsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoreManager = ScoreManager(this)
        ttsManager = TTSManager(this)
        ttsManager.setEnabled(scoreManager.isTtsEnabled())

        subject = Subject.valueOf(intent.getStringExtra(MainActivity.EXTRA_SUBJECT) ?: Subject.ANCIENT.name)
        difficulty = Difficulty.valueOf(scoreManager.getDifficultyPref())

        questions = QuizRepository.getQuestions(subject, difficulty)
        if (questions.isEmpty()) {
            finish()
            return
        }

        showQuestion()

        binding.btnTts.setOnClickListener {
            ttsManager.speak(questions[currentIndex].text)
        }

        binding.btnHint.setOnClickListener { onHintUsed() }

        binding.btnNext.setOnClickListener {
            currentIndex++
            if (currentIndex < questions.size) {
                showQuestion()
            } else {
                finishQuiz()
            }
        }
    }

    private fun showQuestion() {
        isAnswered = false
        hintUsed = false
        val question = questions[currentIndex]

        binding.tvQuestionNumber.text =
            getString(R.string.question_format, currentIndex + 1, questions.size)
        binding.tvScore.text = getString(R.string.score_format, score)
        binding.tvQuestion.text = question.text

        val answerButtons = listOf(
            binding.answer1, binding.answer2, binding.answer3, binding.answer4
        )

        answerButtons.forEachIndexed { index, button ->
            if (index < question.options.size) {
                button.visibility = View.VISIBLE
                button.text = question.options[index]
                button.backgroundTintList = ColorStateList.valueOf(getColor(R.color.card_bg))
                button.strokeColor = ColorStateList.valueOf(getColor(R.color.text_secondary))
                button.setTextColor(getColor(R.color.text_primary))
                button.isEnabled = true
                button.setOnClickListener { onAnswerSelected(index) }
            } else {
                button.visibility = View.GONE
            }
        }

        binding.btnHint.isEnabled = true
        binding.btnHint.visibility = View.VISIBLE
        binding.btnNext.visibility = View.GONE
        binding.answersScroll.scrollTo(0, 0)
    }

    private fun onHintUsed() {
        if (isAnswered || hintUsed) return
        hintUsed = true

        val question = questions[currentIndex]
        binding.btnHint.isEnabled = false

        AlertDialog.Builder(this)
            .setTitle(R.string.hint)
            .setMessage(question.hint)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun onAnswerSelected(index: Int) {
        if (isAnswered) return
        isAnswered = true

        val question = questions[currentIndex]
        val answerButtons = listOf(
            binding.answer1, binding.answer2, binding.answer3, binding.answer4
        )

        answerButtons.forEach { it.isEnabled = false }

        if (index == question.correctIndex) {
            if (!hintUsed) score++
            answerButtons[index].backgroundTintList = ColorStateList.valueOf(getColor(R.color.correct_green))
            answerButtons[index].strokeColor = ColorStateList.valueOf(getColor(R.color.correct_green))
            answerButtons[index].setTextColor(getColor(R.color.white))
        } else {
            answerButtons[index].backgroundTintList = ColorStateList.valueOf(getColor(R.color.wrong_red))
            answerButtons[index].strokeColor = ColorStateList.valueOf(getColor(R.color.wrong_red))
            answerButtons[index].setTextColor(getColor(R.color.white))
            answerButtons[question.correctIndex].backgroundTintList = ColorStateList.valueOf(getColor(R.color.correct_green))
            answerButtons[question.correctIndex].strokeColor = ColorStateList.valueOf(getColor(R.color.correct_green))
            answerButtons[question.correctIndex].setTextColor(getColor(R.color.white))
        }

        binding.tvScore.text = getString(R.string.score_format, score)

        if (currentIndex == questions.size - 1) {
            binding.btnNext.text = getString(R.string.home)
            binding.btnNext.contentDescription = getString(R.string.home)
        } else {
            binding.btnNext.text = getString(R.string.next)
        }
        binding.btnNext.visibility = View.VISIBLE
    }

    private fun finishQuiz() {
        scoreManager.saveScore(subject, difficulty, score)
        val intent = android.content.Intent(this, ResultActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_SCORE, score)
            putExtra(MainActivity.EXTRA_TOTAL, questions.size)
            putExtra(MainActivity.EXTRA_SUBJECT, subject.name)
            putExtra(MainActivity.EXTRA_DIFFICULTY, difficulty.name)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        ttsManager.shutdown()
        super.onDestroy()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Leave Quiz?")
            .setMessage("Your progress will be lost.")
            .setPositiveButton("Leave") { _, _ ->
                ttsManager.shutdown()
                super.onBackPressed()
            }
            .setNegativeButton("Stay", null)
            .show()
    }
}
