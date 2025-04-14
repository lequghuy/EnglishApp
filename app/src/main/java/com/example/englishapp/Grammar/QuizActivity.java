package com.example.englishapp.Grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.R;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView txtQuestion;
    RadioGroup radioGroup;
    RadioButton radioOption1, radioOption2, radioOption3, radioOption4;
    Button btnNext;

    int currentQuestion = 0;
    int score = 0;
    List<Integer> wrongQuestions = new ArrayList<>();

    // Dữ liệu câu hỏi, đáp án
    String[] questions;
    String[][] options;
    int[] answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phuonganh_activity_quiz);

        // Nhận chủ đề từ Intent
        String topic = getIntent().getStringExtra("topic");

        // Ánh xạ view
        txtQuestion = findViewById(R.id.txtQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        radioOption1 = findViewById(R.id.radioOption1);
        radioOption2 = findViewById(R.id.radioOption2);
        radioOption3 = findViewById(R.id.radioOption3);
        radioOption4 = findViewById(R.id.radioOption4);
        btnNext = findViewById(R.id.btnNext);

        // Load câu hỏi theo chủ đề
        loadQuestionsByTopic(topic);

        // Xử lý khi bấm "Next"
        btnNext.setOnClickListener(v -> {
            int selected = radioGroup.getCheckedRadioButtonId();
            if (selected == -1) {
                Toast.makeText(this, "Vui lòng chọn 1 đáp án", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedIndex = radioGroup.indexOfChild(findViewById(selected));
            if (selectedIndex == answers[currentQuestion]) {
                score++;
            } else {
                wrongQuestions.add(currentQuestion);
            }

            currentQuestion++;
            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                showScore(); // ✅ Hiển thị phần trăm trước khi thoát
            }

            if (currentQuestion < questions.length) {
                loadQuestion();
            } else {
                showScore(); // ✅ Hiển thị phần trăm trước khi thoát
            }
        });
    }

    // Hiển thị câu hỏi hiện tại
    void loadQuestion() {
        txtQuestion.setText(questions[currentQuestion]);
        radioOption1.setText(options[currentQuestion][0]);
        radioOption2.setText(options[currentQuestion][1]);
        radioOption3.setText(options[currentQuestion][2]);
        radioOption4.setText(options[currentQuestion][3]);
        radioGroup.clearCheck();
    }

    // Nạp câu hỏi theo chủ đề
    void loadQuestionsByTopic(String topic) {
        if (topic.equals("Thì hiện tại đơn")) {
            questions = new String[]{
                    "Câu nào sau đây là thì hiện tại đơn?",
                    "Chủ ngữ số ít + động từ thế nào?",
                    "Dấu hiệu nhận biết thì hiện tại đơn là?"
            };
            options = new String[][]{
                    {"She works every day.", "She is working now.", "She was working.", "She has worked."},
                    {"Thêm -s/es", "Không thêm gì", "Dùng V-ing", "Dùng quá khứ đơn"},
                    {"always, usually", "last year", "tomorrow", "now"}
            };
            answers = new int[]{0, 0, 0};
        } else if (topic.equals("Thì quá khứ đơn")) {
            questions = new String[]{
                    "Câu nào sau đây là thì quá khứ đơn?",
                    "Dấu hiệu nào dùng cho quá khứ đơn?",
                    "Cấu trúc thì quá khứ đơn là?"
            };
            options = new String[][]{
                    {"He visited Hanoi last year.", "He is visiting Hanoi.", "He visits Hanoi.", "He has visited Hanoi."},
                    {"yesterday", "now", "next week", "always"},
                    {"S + V2/ed", "S + will V", "S + am/is/are + V-ing", "S + have/has + V3"}
            };
            answers = new int[]{0, 0, 0};
        } else if (topic.equals("Câu điều kiện")) {
            questions = new String[]{
                    "Câu điều kiện loại 1 là gì?",
                    "Câu điều kiện loại 2 dùng để nói về điều gì?",
                    "Cấu trúc câu điều kiện loại 3 là gì?"
            };
            options = new String[][]{
                    {"If + S + V, S + will + V", "If + S + V2, S + would + V", "If + S + had + V3, S + would have + V3", "If + S + V-ing, S + will + V"},
                    {"Điều không có thật ở hiện tại", "Thói quen", "Sự thật", "Hành động đang diễn ra"},
                    {"If + S + had + V3, S + would have + V3", "If + S + V, S + will + V", "If + S + V2, S + would + V", "S + V + if + V"}
            };
            answers = new int[]{0, 0, 0};
        } else if (topic.equals("Bị động")) {
            questions = new String[]{
                    "Câu nào sau đây là bị động ở hiện tại đơn?",
                    "Cấu trúc bị động là gì?",
                    "Chuyển câu chủ động sang bị động: 'They build houses' → ?"
            };
            options = new String[][]{
                    {"The cake is made.", "The cake was made.", "The cake has been made.", "The cake will be made."},
                    {"S + be + V3/ed", "S + V2/ed", "S + have/has + V3", "S + will + V"},
                    {"Houses are built", "Houses were build", "Houses was built", "Houses is built"}
            };
            answers = new int[]{0, 0, 0};
        } else if (topic.equals("Câu gián tiếp")) {
            questions = new String[]{
                    "Câu gián tiếp của: 'He said: I am tired' là gì?",
                    "Đổi thì khi chuyển sang câu gián tiếp: 'present simple' → ?",
                    "Câu hỏi Wh-question gián tiếp đúng là?"
            };
            options = new String[][]{
                    {"He said that he was tired.", "He said he is tired.", "He said that I was tired.", "He said he had been tired."},
                    {"past simple", "present continuous", "present perfect", "past perfect"},
                    {"She asked where he was.", "She asked where was he.", "She asked where is he.", "She asked where he is."}
            };
            answers = new int[]{0, 0, 0};
        } else {
            questions = new String[]{"Chủ đề không tồn tại!"};
            options = new String[][]{{"1", "2", "3", "4"}};
            answers = new int[]{0};
        }

        currentQuestion = 0;
        score = 0;
        wrongQuestions.clear();

        loadQuestion();
    }


    // Chuyển mảng 2 chiều thành 1 chiều để gửi qua Intent
    private String[] flattenOptions(String[][] original) {
        List<String> flat = new ArrayList<>();
        for (String[] row : original) {
            for (String s : row) {
                flat.add(s);
            }
        }
        return flat.toArray(new String[0]);
    }

    private void showScore() {
        int total = questions.length;
        int percent = (int) ((score / (float) total) * 100);

        String message = "🎯 Kết quả: " + score + "/" + total + " câu đúng\n" +
                "📊 Bạn đạt: " + percent + "%";

        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành bài kiểm tra")
                .setMessage(message)
                .setPositiveButton("Xem chi tiết", (dialog, which) -> {
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("total", questions.length);
                    intent.putExtra("wrongList", new ArrayList<>(wrongQuestions));
                    intent.putExtra("questions", questions);
                    intent.putExtra("answers", answers);
                    intent.putExtra("options", flattenOptions(options));
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}