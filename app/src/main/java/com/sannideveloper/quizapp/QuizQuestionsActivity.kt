package com.sannideveloper.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), OnClickListener {

    private var mUserName: String? = null

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0

    private var progressBar: ProgressBar? = null
    private var textViewProgress: TextView? = null
    private var textViewQuestion: TextView? = null
    private var image: ImageView? = null

    private var textViewOptionOne: TextView? = null
    private var textViewOptionTwo: TextView? = null
    private var textViewOptionThree: TextView? = null
    private var textViewOptionFour: TextView? = null
    private var btnSubmit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        progressBar = findViewById(R.id.progressBar)
        textViewProgress = findViewById(R.id.textProgress)
        textViewQuestion = findViewById(R.id.question)
        image = findViewById(R.id.ivImage)

        textViewOptionOne = findViewById(R.id.btnAnswer1)
        textViewOptionTwo = findViewById(R.id.btnAnswer2)
        textViewOptionThree = findViewById(R.id.btnAnswer3)
        textViewOptionFour = findViewById(R.id.btnAnswer4)
        btnSubmit = findViewById(R.id.submitButton)

        //these setOnClickListeners use the protocol (Swift) OnClickListener to work
        textViewOptionOne?.setOnClickListener(this)
        textViewOptionTwo?.setOnClickListener(this)
        textViewOptionThree?.setOnClickListener(this)
        textViewOptionFour?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

    }

    private fun setQuestion() {

        defaultOptionsView()
        val question: Question = mQuestionsList!![mCurrentPosition - 1]
        image?.setImageResource(question.image)
        progressBar?.progress = mCurrentPosition
        textViewProgress?.text = "$mCurrentPosition / ${progressBar?.max}"
        textViewQuestion?.text = question.question
        textViewOptionOne?.text = question.optionOne
        textViewOptionTwo?.text = question.optionTwo
        textViewOptionThree?.text = question.optionThree
        textViewOptionFour?.text = question.optionFour

        if (mCurrentPosition == mQuestionsList!!.size) {
            btnSubmit?.text = "FINISH"
        } else {
            btnSubmit?.text = "SUBMIT"
        }
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        textViewOptionOne?.let {
            options.add(0, it)
        }

        textViewOptionTwo?.let {
            options.add(1, it)
        }

        textViewOptionThree?.let {
            options.add(2, it)
        }

        textViewOptionFour?.let {
            options.add(3, it)
        }

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }



    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }


    //this function came from the protocol (Swift Language) OnClickListener
    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnAnswer1 -> {
                textViewOptionOne?.let {
                    selectedOptionView(it, 1)
                }
            }

            R.id.btnAnswer2 -> {
                textViewOptionTwo?.let {
                    selectedOptionView(it, 2)
                }
            }

            R.id.btnAnswer3 -> {
                textViewOptionThree?.let {
                    selectedOptionView(it, 3)
                }
            }

            R.id.btnAnswer4 -> {
                textViewOptionFour?.let {
                    selectedOptionView(it, 4)
                }
            }

            R.id.submitButton -> {
                //TODO "implement btn submit"
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWER, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList?.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                        answerView(question!!.correctAnswer, R.drawable.correct_option_border_bg)

                    } else {
                        mCorrectAnswers++
                        answerView(mSelectedOptionPosition, R.drawable.correct_option_border_bg)
                    }

                    if (mCurrentPosition == mQuestionsList!!.size) {
                        btnSubmit?.text = "FINISH"
                    } else {
                        btnSubmit?.text = "GO TO NEXT QUESTION"
                    }

                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private  fun answerView(answer: Int, drawableView: Int) {
        when(answer) {
            1 -> {
                textViewOptionOne?.background = ContextCompat.getDrawable(this, drawableView)
            }

            2 -> {
                textViewOptionOne?.background = ContextCompat.getDrawable(this, drawableView)
            }

            3 -> {
                textViewOptionOne?.background = ContextCompat.getDrawable(this, drawableView)
            }

            4 -> {
                textViewOptionOne?.background = ContextCompat.getDrawable(this, drawableView)
            }


        }
    }

}