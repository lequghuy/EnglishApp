package com.example.englishapp.Grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.R;

public class QuizActivity extends AppCompatActivity {
    TextView txtQuestion;
    RadioGroup radioGroup;
    RadioButton radioOption1, radioOption2, radioOption3, radioOption4;
    Button btnNext;

    int currentQuestion = 0;
    int score = 0;

    String[] questions = {
            "Câu nào sau đây là thì hiện tại đơn?",
            "Câu nào sau đây là thì quá khứ đơn?"
    };

    String[][] options = {
            {"She works every day.", "She is working now.", "She was working.", "She has worked."},
            {"He visits Hanoi every year.", "He is visiting Hanoi.", "He visited Hanoi last year.", "He has visited Hanoi."}
    };

    int[] answers = {0, 2}; // chỉ số đúng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtQuestion = findViewById(R.id.txtQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        radioOption1 = findViewById(R.id.radioOption1);
        radioOption2 = findViewById(R.id.radioOption2);
        radioOption3 = findViewById(R.id.radioOption3);
        radioOption4 = findViewById(R.id.radioOption4);
        btnNext = findViewById(R.id.btnNext);

        loadQuestion();

        btnNext.setOnClickListener(v -> {
            int selected = radioGroup.getCheckedRadioButtonId();
            if (selected == -1) {
                Toast.makeText(this, "Vui lòng chọn 1 đáp án", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedIndex = radioGroup.indexOfChild(findViewById(selected));
            if (selectedIndex == answers[currentQuestion]) {
                score++;
            }

            currentQuestion++;
            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("total", questions.length);
                startActivity(intent);
                finish();
            }
        });
    }

    void loadQuestion() {
        txtQuestion.setText(questions[currentQuestion]);
        radioOption1.setText(options[currentQuestion][0]);
        radioOption2.setText(options[currentQuestion][1]);
        radioOption3.setText(options[currentQuestion][2]);
        radioOption4.setText(options[currentQuestion][3]);
        radioGroup.clearCheck();
    }
}
