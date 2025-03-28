package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView tvQuestion, tvProgress, tvResult;
    private Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnBack;
    private DatabaseHelper dbHelper;
    private List<QuizQuestion> quizQuestions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private boolean isQuizFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tv_question);
        tvProgress = findViewById(R.id.tv_progress);
        tvResult = findViewById(R.id.tv_result);
        btnAnswer1 = findViewById(R.id.btn_answer1);
        btnAnswer2 = findViewById(R.id.btn_answer2);
        btnAnswer3 = findViewById(R.id.btn_answer3);
        btnAnswer4 = findViewById(R.id.btn_answer4);
        btnBack = findViewById(R.id.btn_back);

        dbHelper = new DatabaseHelper(this);
        quizQuestions = dbHelper.getAllQuizQuestions();

        if (quizQuestions.isEmpty()) {
            tvQuestion.setText("Không có câu hỏi nào!");
            disableButtons();
            return;
        }

        showNextQuestion();

        btnAnswer1.setOnClickListener(v -> checkAnswer(btnAnswer1));
        btnAnswer2.setOnClickListener(v -> checkAnswer(btnAnswer2));
        btnAnswer3.setOnClickListener(v -> checkAnswer(btnAnswer3));
        btnAnswer4.setOnClickListener(v -> checkAnswer(btnAnswer4));
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
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
        // Sử dụng Handler với Looper để tránh deprecated API
        new Handler(Looper.getMainLooper()).postDelayed(this::showNextQuestion, 1000);
    }

    private void showStats() {
        isQuizFinished = true;
        setContentView(R.layout.activity_stats);
        TextView tvCorrect = findViewById(R.id.tv_correct);
        TextView tvWrong = findViewById(R.id.tv_wrong);
        TextView tvPercentage = findViewById(R.id.tv_percentage);
        btnBack = findViewById(R.id.btn_back);

        int wrongAnswers = quizQuestions.size() - correctAnswers;
        double percentage = (double) correctAnswers / quizQuestions.size() * 100;

        tvCorrect.setText("Số câu đúng: " + correctAnswers);
        tvWrong.setText("Số câu sai: " + wrongAnswers);
        tvPercentage.setText("Tỷ lệ đúng: " + String.format("%.1f%%", percentage));

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
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