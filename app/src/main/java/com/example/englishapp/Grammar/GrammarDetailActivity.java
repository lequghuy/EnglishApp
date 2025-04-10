package com.example.englishapp.Grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.R;

import java.io.Serializable;

public class GrammarDetailActivity extends AppCompatActivity {
    TextView txtTitle, txtContent;
    Button btnPractice, btnQuiz, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_detail);

        txtTitle = findViewById(R.id.txtTopicTitle);
        txtContent = findViewById(R.id.txtTopicContent);

        String topic = getIntent().getStringExtra("topic");
        txtTitle.setText("Bạn đã chọn chủ đề: " + topic);

        // Ví dụ: nội dung giả định theo chủ đề
        String content = "";

        if (topic != null) {
            txtTitle.setText(topic);

            switch (topic) {
                case "Thì hiện tại đơn":
                    content = "📌 Cấu trúc:\n"
                            + "- Khẳng định: S + V(s/es) + O\n"
                            + "- Phủ định: S + do/does not + V + O\n"
                            + "- Nghi vấn: Do/Does + S + V + O?\n\n"
                            + "📚 Cách dùng:\n"
                            + "- Diễn tả thói quen, sự thật, lịch trình.\n"
                            + "Ví dụ: He plays football every day.\n";
                    break;
                case "Thì quá khứ đơn":
                    content = "📌 Cấu trúc:\n"
                            + "- Khẳng định: S + V2/ed + O\n"
                            + "- Phủ định: S + did not + V + O\n"
                            + "- Nghi vấn: Did + S + V + O?\n\n"
                            + "📚 Cách dùng:\n"
                            + "- Diễn tả hành động đã xảy ra trong quá khứ.\n"
                            + "Ví dụ: I visited Da Nang last summer.\n";
                    break;
                case "Câu điều kiện":
                    content = "📌 Các loại câu điều kiện:\n"
                            + "- Loại 1: If + S + V, S + will + V\n"
                            + "- Loại 2: If + S + V(quá khứ), S + would + V\n"
                            + "- Loại 3: If + S + had + V3, S + would have + V3\n\n"
                            + "📚 Lưu ý:\n"
                            + "- Dùng 'were' cho tất cả chủ ngữ trong loại 2.\n";
                    break;
                case "Bị động":
                    content = "📌 Cấu trúc:\n"
                            + "S + be + V3/ed + (by + O)\n\n"
                            + "📌 Các thì ví dụ:\n"
                            + "- Hiện tại: The cake is made.\n"
                            + "- Quá khứ: The letter was written.\n"
                            + "- Tiếp diễn: The house is being built.\n";
                    break;
                case "Câu gián tiếp":
                    content = "📌 Cấu trúc:\n"
                            + "S + said/told + (that) + mệnh đề\n\n"
                            + "📌 Các thay đổi:\n"
                            + "- I → he/she | this → that | now → then\n"
                            + "- today → that day | tomorrow → the next day\n\n"
                            + "📌 Câu hỏi:\n"
                            + "- Yes/No: if/whether\n"
                            + "- Wh-questions giữ nguyên từ để hỏi\n";
                    break;
                default:
                    content = "Không có nội dung cho chủ đề này.";
            }

            txtContent.setText(content);
            Toast.makeText(this, "Chủ đề: " + topic, Toast.LENGTH_SHORT).show();
        } else {
            txtTitle.setText("Không nhận được chủ đề!");
            txtContent.setText("");
        }

        btnPractice = findViewById(R.id.btnPractice);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnBack = findViewById(R.id.btnBack);

        btnPractice.setOnClickListener(v -> {
            Intent intent = new Intent(this, PracticeActivity.class);
            intent.putExtra("topic", topic);
            startActivity(intent);
        });

        btnQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("topic", topic);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}