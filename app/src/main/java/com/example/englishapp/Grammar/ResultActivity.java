package com.example.englishapp.Grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.MainActivity;
import com.example.englishapp.R;

public class ResultActivity extends AppCompatActivity {

    TextView txtResult, txtScore;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phuonganh_activity_result);

        txtResult = findViewById(R.id.txtResult);
        txtScore = findViewById(R.id.txtScore);
        btnFinish = findViewById(R.id.btnFinish);

        // Nhận dữ liệu từ QuizActivity
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);

        // Hiển thị kết quả
        txtScore.setText("Bạn trả lời đúng " + score + " / " + total + " câu");

        // Nút quay lại menu chính
        btnFinish.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Xoá ngăn xếp về MainActivity
            startActivity(intent);
            finish();
        });
    }
}
