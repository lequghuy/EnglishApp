package com.example.englishapp.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.DatabaseHelper;
import com.example.englishapp.MainActivity;
import com.example.englishapp.R;

import java.util.List;

public class TopicQuizActivity extends AppCompatActivity {
    private TextView tvQuestion, tvProgress, tvResult;
    private Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnBack;
    private DatabaseHelper dbHelper;
    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huy_activity_quiz);

        tvQuestion = findViewById(R.id.tv_question);
        tvProgress = findViewById(R.id.tv_progress);
        tvResult = findViewById(R.id.tv_result);
        btnAnswer1 = findViewById(R.id.btn_answer1);
        btnAnswer2 = findViewById(R.id.btn_answer2);
        btnAnswer3 = findViewById(R.id.btn_answer3);
        btnAnswer4 = findViewById(R.id.btn_answer4);
        btnBack = findViewById(R.id.btn_back);

        if (tvQuestion == null || tvProgress == null || tvResult == null ||
                btnAnswer1 == null || btnAnswer2 == null || btnAnswer3 == null || btnAnswer4 == null || btnBack == null) {
            Log.e("TopicQuizActivity", "One or more views not found in activity_quiz.xml");
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        String topic = getIntent().getStringExtra("TOPIC");
        if (topic == null) {
            Log.e("TopicQuizActivity", "No topic received from intent");
            tvQuestion.setText("Không nhận được chủ đề!");
            disableButtons();
            btnBack.setVisibility(View.VISIBLE);
            return;
        }

        quizQuestions = dbHelper.getQuizQuestionsByTopic(topic);
        Log.d("TopicQuizActivity", "Topic: " + topic + ", Number of questions: " + (quizQuestions != null ? quizQuestions.size() : 0));

        if (quizQuestions == null || quizQuestions.isEmpty()) {
            Log.w("TopicQuizActivity", "No questions found for topic: " + topic);
            tvQuestion.setText("Không có câu hỏi nào cho chủ đề " + topic + "!");
            disableButtons();
            btnBack.setVisibility(View.VISIBLE);
            return;
        }

        showNextQuestion();

        btnAnswer1.setOnClickListener(v -> checkAnswer(btnAnswer1));
        btnAnswer2.setOnClickListener(v -> checkAnswer(btnAnswer2));
        btnAnswer3.setOnClickListener(v -> checkAnswer(btnAnswer3));
        btnAnswer4.setOnClickListener(v -> checkAnswer(btnAnswer4));
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TopicQuizActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < quizQuestions.size()) {
            QuizQuestion question = quizQuestions.get(currentQuestionIndex);
            tvQuestion.setText(question.getQuestion());
            List<String> answers = question.getAnswers();
            btnAnswer1.setText(answers.get(0));
            btnAnswer2.setText(answers.get(1));
            btnAnswer3.setText(answers.get(2));
            btnAnswer4.setText(answers.get(3));
            tvProgress.setText((currentQuestionIndex + 1) + "/" + quizQuestions.size());
            tvResult.setText("");
            enableButtons();
            btnBack.setVisibility(View.VISIBLE);
        } else {
            showStats();
        }
    }

    private void checkAnswer(Button selectedButton) {
        disableButtons();
        QuizQuestion currentQuestion = quizQuestions.get(currentQuestionIndex);
        String selectedAnswer = selectedButton.getText().toString();
        if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
            selectedButton.setBackgroundResource(R.drawable.button_correct);
            tvResult.setText("Đúng!");
            correctAnswers++;
        } else {
            selectedButton.setBackgroundResource(R.drawable.button_wrong);
            tvResult.setText("Sai! Đáp án đúng là: " + currentQuestion.getCorrectAnswer());
        }

        currentQuestionIndex++;
        new Handler(Looper.getMainLooper()).postDelayed(this::showNextQuestion, 1000);
    }

    private void showStats() {
        setContentView(R.layout.huy_activity_stats);
        TextView tvCorrect = findViewById(R.id.tv_correct);
        TextView tvWrong = findViewById(R.id.tv_wrong);
        TextView tvPercentage = findViewById(R.id.tv_percentage);
        btnBack = findViewById(R.id.btn_back);

        if (tvCorrect == null || tvWrong == null || tvPercentage == null || btnBack == null) {
            Log.e("TopicQuizActivity", "One or more views not found in activity_stats.xml");
            finish();
            return;
        }

        int wrongAnswers = quizQuestions.size() - correctAnswers;
        double percentage = (double) correctAnswers / quizQuestions.size() * 100;

        tvCorrect.setText("Số câu đúng: " + correctAnswers);
        tvWrong.setText("Số câu sai: " + wrongAnswers);
        tvPercentage.setText("Tỷ lệ đúng: " + String.format("%.1f%%", percentage));

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TopicQuizActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void disableButtons() {
        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);
    }

    private void enableButtons() {
        btnAnswer1.setEnabled(true);
        btnAnswer2.setEnabled(true);
        btnAnswer3.setEnabled(true);
        btnAnswer4.setEnabled(true);
        btnAnswer1.setBackgroundResource(R.drawable.button_default);
        btnAnswer2.setBackgroundResource(R.drawable.button_default);
        btnAnswer3.setBackgroundResource(R.drawable.button_default);
        btnAnswer4.setBackgroundResource(R.drawable.button_default);
    }
}