package com.example.englishapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class BaiKT extends AppCompatActivity {

    private TextView txtChuDe, txtSoCau, txtCauHoi;
    private LinearLayout chonCauHoi, txtLuaChon;
    private Button btnXacNhan;

    private List<QuizQuestion> questionList;
    private int currentIndex = 0;
    private int selectedAnswerIndex = -1;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz); // dùng đúng tên XML bạn đã chỉnh

        // Ánh xạ các view mới
        txtChuDe = findViewById(R.id.txtChuDe);
        txtSoCau = findViewById(R.id.txtSoCau);
        txtCauHoi = findViewById(R.id.txtCauHoi);
        chonCauHoi = findViewById(R.id.chonCauHoi);
        txtLuaChon = findViewById(R.id.txtLuaChon);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        // Thiết lập tiêu đề
        txtChuDe.setText("Làm bài kiểm tra");
        btnXacNhan.setText("Xác nhận");

        // Tải dữ liệu
        loadQuestions();
        setupQuestionNumberCircles();
        showQuestion(currentIndex);

        // Bắt sự kiện xác nhận
        btnXacNhan.setOnClickListener(view -> {
            if (selectedAnswerIndex == -1) {
                Toast.makeText(this, "Vui lòng chọn một đáp án!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedAnswerIndex == questionList.get(currentIndex).getCorrectAnswerIndex()) {
                score++;
            }

            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                selectedAnswerIndex = -1;
                showQuestion(currentIndex);
            } else {
                Toast.makeText(this, "Bạn đã hoàn thành! Điểm: " + score + "/" + questionList.size(), Toast.LENGTH_LONG).show();
                // TODO: Hiển thị kết quả hoặc chuyển trang
            }
        });
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();
        questionList.add(new QuizQuestion("Where is the nearest bus stop?",
                new String[]{"Near the supermarket", "At 3 o’clock", "By the city hall", "To the airport"}, 0));
        questionList.add(new QuizQuestion("What time is it?",
                new String[]{"It’s blue", "At 7 o’clock", "My name is John", "See you"}, 1));
        questionList.add(new QuizQuestion("How are you?",
                new String[]{"Fine, thank you", "To the left", "Near the market", "Tomorrow"}, 0));
    }

    private void showQuestion(int index) {
        QuizQuestion question = questionList.get(index);
        txtSoCau.setText("Câu " + (index + 1) + "/" + questionList.size());
        txtCauHoi.setText(question.getQuestion());

        txtLuaChon.removeAllViews();
        String[] options = question.getOptions();
        for (int i = 0; i < options.length; i++) {
            int finalI = i;
            RadioButton radio = new RadioButton(this);
            radio.setText((char) ('A' + i) + ". " + options[i]);
            radio.setTextSize(16);
            radio.setPadding(8, 16, 8, 16);
            radio.setButtonDrawable(null);
            radio.setBackgroundResource(R.drawable.bg_card);
            radio.setOnClickListener(view -> selectedAnswerIndex = finalI);
            txtLuaChon.addView(radio);
        }
    }

    private void setupQuestionNumberCircles() {
        chonCauHoi.removeAllViews();
        for (int i = 0; i < questionList.size(); i++) {
            TextView number = new TextView(this);
            number.setText(String.valueOf(i + 1));
            number.setPadding(24, 16, 24, 16);
            number.setBackgroundResource(R.drawable.bg_card);
            number.setTextColor(getResources().getColor(android.R.color.black));
            number.setTextSize(14);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            number.setLayoutParams(params);

            chonCauHoi.addView(number);
        }
    }
}
