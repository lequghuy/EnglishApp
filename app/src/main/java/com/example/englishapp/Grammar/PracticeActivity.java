package com.example.englishapp.Grammar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.R;

import java.util.ArrayList;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    TextView txtPracticeContent;
    Button btnNext, btnPrevious, btnBackToDetail;

    RadioGroup answerGroup;
    RadioButton optionA, optionB, optionC, optionD;


    List<PracticeItem> practiceItems = new ArrayList<>();

    int currentIndex = 0;
    String topic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phuonganh_activity_practice);

        txtPracticeContent = findViewById(R.id.txtPracticeContent);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        btnBackToDetail = findViewById(R.id.btnBackToDetail);
        btnBackToDetail.setOnClickListener(v -> {
            finish(); // đơn giản là quay lại màn trước đó
        });

        answerGroup = findViewById(R.id.answerGroup);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);

        topic = getIntent().getStringExtra("topic");
        setupPracticeItems(topic);
        showPractice();

        btnNext.setOnClickListener(v -> {
            if (currentIndex < practiceItems.size() - 1) {
                currentIndex++;
                showPractice();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showPractice();
            }
        });

        answerGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) return;

            PracticeItem current = practiceItems.get(currentIndex);
            int selectedIndex = -1;

            if (checkedId == R.id.optionA) selectedIndex = 0;
            else if (checkedId == R.id.optionB) selectedIndex = 1;
            else if (checkedId == R.id.optionC) selectedIndex = 2;
            else if (checkedId == R.id.optionD) selectedIndex = 3;

            highlightAnswer(selectedIndex); // Đổi màu đáp án

            if (selectedIndex == current.correctIndex) {
                Toast.makeText(this, "✅ Chính xác!", Toast.LENGTH_SHORT).show();
                btnNext.setEnabled(true); // Tự bật nút Next
            } else {
                Toast.makeText(this, "❌ Sai rồi! Đáp án đúng là: " +
                        current.options[current.correctIndex], Toast.LENGTH_SHORT).show();
            }

            btnNext.setEnabled(true);
        });

    }

    private void setupPracticeItems(String topic) {
        switch (topic) {
            case "Thì hiện tại đơn":
                practiceItems.add(new PracticeItem(
                        "I ___ (go) to school every day.",
                        new String[]{"go", "goes", "went", "gone"},
                        0));
                practiceItems.add(new PracticeItem(
                        "She ___ (watch) TV in the evening.",
                        new String[]{"watch", "watched", "watches", "watching"},
                        2));
                practiceItems.add(new PracticeItem(
                        "Do they ___ (like) pizza?",
                        new String[]{"likes", "like", "liked", "liking"},
                        1));
                break;

            case "Thì quá khứ đơn":
                practiceItems.add(new PracticeItem(
                        "I ___ (visit) my grandma last week.",
                        new String[]{"visiting", "visited", "visits", "visit"},
                        1));
                practiceItems.add(new PracticeItem(
                        "He ___ (not go) to school yesterday.",
                        new String[]{"did not go", "does not go", "gone", "go"},
                        0));
                break;

            case "Câu điều kiện":
                practiceItems.add(new PracticeItem(
                        "If it rains, I ___ (stay) at home.",
                        new String[]{"will stay", "would stay", "stay", "stayed"},
                        0));
                practiceItems.add(new PracticeItem(
                        "If I were you, I ___ (not do) that.",
                        new String[]{"won't do", "don't do", "would not do", "didn't do"},
                        2));
                break;

            case "Bị động":
                practiceItems.add(new PracticeItem(
                        "The book ___ (write) by a famous author.",
                        new String[]{"was written", "is wrote", "written", "writes"},
                        0));
                practiceItems.add(new PracticeItem(
                        "The cake ___ (make) every morning.",
                        new String[]{"was made", "is made", "makes", "made"},
                        1));
                break;

            default:
                practiceItems.add(new PracticeItem(
                        "Không có nội dung luyện tập.",
                        new String[]{"", "", "", ""},
                        0));
        }
    }

    private void showPractice() {
        PracticeItem current = practiceItems.get(currentIndex);
        txtPracticeContent.setText(current.question);

        optionA.setText("A. " + current.options[0]);
        optionB.setText("B. " + current.options[1]);
        optionC.setText("C. " + current.options[2]);
        optionD.setText("D. " + current.options[3]);

        answerGroup.clearCheck(); // reset

        // Reset màu nền
        optionA.setBackgroundColor(Color.TRANSPARENT);
        optionB.setBackgroundColor(Color.TRANSPARENT);
        optionC.setBackgroundColor(Color.TRANSPARENT);
        optionD.setBackgroundColor(Color.TRANSPARENT);

        enableOptions(true);
        btnNext.setEnabled(false); // Ẩn nút Next cho đến khi chọn xong

        btnPrevious.setEnabled(currentIndex > 0);
        btnNext.setEnabled(false); // chỉ bật khi đúng
    }


    private void enableOptions(boolean enabled) {
        optionA.setEnabled(enabled);
        optionB.setEnabled(enabled);
        optionC.setEnabled(enabled);
        optionD.setEnabled(enabled);
    }

    private void highlightAnswer(int selectedIndex) {
        PracticeItem current = practiceItems.get(currentIndex);

        // Reset màu
        optionA.setBackgroundColor(Color.TRANSPARENT);
        optionB.setBackgroundColor(Color.TRANSPARENT);
        optionC.setBackgroundColor(Color.TRANSPARENT);
        optionD.setBackgroundColor(Color.TRANSPARENT);

        // Highlight đáp án đúng
        RadioButton correctBtn = getRadioButtonByIndex(current.correctIndex);
        correctBtn.setBackgroundColor(Color.parseColor("#A5D6A7")); // Màu xanh lá nhạt

        // Nếu chọn sai thì tô màu đỏ
        if (selectedIndex != current.correctIndex) {
            RadioButton selectedBtn = getRadioButtonByIndex(selectedIndex);
            selectedBtn.setBackgroundColor(Color.parseColor("#EF9A9A")); // Màu đỏ nhạt
        }
    }

    private RadioButton getRadioButtonByIndex(int index) {
        switch (index) {
            case 0: return optionA;
            case 1: return optionB;
            case 2: return optionC;
            case 3: return optionD;
            default: return null;
        }
    }

}