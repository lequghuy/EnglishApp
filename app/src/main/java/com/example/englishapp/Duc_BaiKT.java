package com.example.englishapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Duc_BaiKT extends AppCompatActivity {

    private TextView txtChuDe, txtSoCau, txtCauHoi;
    private LinearLayout chonCauHoi, txtLuaChon;
    private Button btnXacNhan;

    private List<Duc_QuizQuestion> questionList;
    private int currentIndex = 0;
    private int selectedAnswerIndex = -1;
    private int score = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>();
    private boolean isFinished = false;
    private boolean userSelectedByNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duc_activity_quiz);

        txtChuDe = findViewById(R.id.txtChuDe);
        txtSoCau = findViewById(R.id.txtSoCau);
        txtCauHoi = findViewById(R.id.txtCauHoi);
        chonCauHoi = findViewById(R.id.chonCauHoi);
        txtLuaChon = findViewById(R.id.txtLuaChon);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        txtChuDe.setText("Làm bài kiểm tra");
        btnXacNhan.setText("Xác nhận");

        loadQuestions();
        setupQuestionNumberCircles();
        showQuestion(currentIndex);

        btnXacNhan.setOnClickListener(view -> {
            if (isFinished) return;
            userSelectedByNumber = true;

            if (userAnswers.size() < questionList.size()) {
                Toast.makeText(this, "Bạn chưa làm hết tất cả các câu hỏi!", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn nộp bài không?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        score = 0;
                        for (int i = 0; i < questionList.size(); i++) {
                            int correct = questionList.get(i).getCorrectAnswerIndex();
                            int user = userAnswers.getOrDefault(i, -1);
                            if (user == correct) score++;
                        }

                        isFinished = true;
                        btnXacNhan.setEnabled(false);
                        highlightFinalResult();

                        new AlertDialog.Builder(this)
                                .setTitle("Hoàn thành")
                                .setMessage("Bạn đã hoàn thành! Điểm: " + score + "/" + questionList.size())
                                .setPositiveButton("OK", (dialog1, which1) -> finish())
                                .show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void highlightFinalResult() {
        for (int i = 0; i < questionList.size(); i++) {
            TextView numberView = (TextView) chonCauHoi.getChildAt(i);
            int userAnswer = userAnswers.getOrDefault(i, -1);
            int correctAnswer = questionList.get(i).getCorrectAnswerIndex();

            if (userAnswer == correctAnswer) {
                numberView.setBackgroundResource(R.drawable.duc_bg_correct);
            } else {
                numberView.setBackgroundResource(R.drawable.duc_bg_wrong);
            }
        }
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();
        try {
            String jsonString = readJSONFromAsset();
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonString, Map.class);
            int deSo = getIntent().getIntExtra("deSo", 1);
            Map<String, Object> de = (Map<String, Object>) data.get("Đề " + deSo);
            ArrayList<Map<String, String>> cauHoiList = (ArrayList<Map<String, String>>) de.get("cauHoi");

            for (Map<String, String> cauHoiData : cauHoiList) {
                String noiDung = cauHoiData.get("noiDungCauHoi");
                String luaChon_1 = cauHoiData.get("luaChon_1");
                String luaChon_2 = cauHoiData.get("luaChon_2");
                String luaChon_3 = cauHoiData.get("luaChon_3");
                String luaChon_4 = cauHoiData.get("luaChon_4");
                String dapAnDung = cauHoiData.get("dapAnDung");

                Duc_QuizQuestion cauHoi = new Duc_QuizQuestion(noiDung,
                        new String[]{luaChon_1, luaChon_2, luaChon_3, luaChon_4},
                        Arrays.asList(luaChon_1, luaChon_2, luaChon_3, luaChon_4).indexOf(dapAnDung));
                questionList.add(cauHoi);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi đọc dữ liệu từ JSON", Toast.LENGTH_SHORT).show();
            Log.e("Duc_BaiKT", "Lỗi đọc JSON: ", e);
        }
    }

    private String readJSONFromAsset() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("de_thi.json")));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void showQuestion(int index) {
        Duc_QuizQuestion question = questionList.get(index);
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
            radio.setBackgroundResource(R.drawable.duc_bg_card);

            radio.setOnClickListener(view -> {
                selectedAnswerIndex = finalI;
                userAnswers.put(currentIndex, selectedAnswerIndex);
                highlightSelectedAnswer(finalI);
                setupQuestionNumberCircles();

                // Nếu không phải do người dùng tự chọn số => tự động chuyển tiếp
                if (!userSelectedByNumber && !isFinished) {
                    if (currentIndex < questionList.size() - 1) {
                        currentIndex++;
                        selectedAnswerIndex = userAnswers.getOrDefault(currentIndex, -1);
                        showQuestion(currentIndex);
                    }
                }
            });
            txtLuaChon.addView(radio);
        }

        if (userAnswers.containsKey(index)) {
            highlightSelectedAnswer(userAnswers.get(index));
            selectedAnswerIndex = userAnswers.get(index);
        }

        userSelectedByNumber = false;
        setupQuestionNumberCircles();
    }

    private void highlightSelectedAnswer(int selectedIndex) {
        for (int i = 0; i < txtLuaChon.getChildCount(); i++) {
            RadioButton radio = (RadioButton) txtLuaChon.getChildAt(i);
            if (i == selectedIndex) {
                radio.setBackgroundResource(R.drawable.duc_bg_selected);
            } else {
                radio.setBackgroundResource(R.drawable.duc_bg_card);
            }
        }
    }

    private void setupQuestionNumberCircles() {
        chonCauHoi.removeAllViews();
        for (int i = 0; i < questionList.size(); i++) {
            TextView number = new TextView(this);
            number.setText(String.valueOf(i + 1));
            number.setPadding(24, 16, 24, 16);
            number.setTextColor(getResources().getColor(android.R.color.black));
            number.setTextSize(14);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            number.setLayoutParams(params);

            if (userAnswers.containsKey(i)) {
                number.setBackgroundResource(R.drawable.duc_bg_done);
            } else {
                number.setBackgroundResource(R.drawable.duc_bg_card);
            }

            final int questionIndex = i;
            number.setOnClickListener(v -> {
                currentIndex = questionIndex;
                selectedAnswerIndex = userAnswers.getOrDefault(currentIndex, -1);
                userSelectedByNumber = true;
                showQuestion(currentIndex);
            });

            chonCauHoi.addView(number);
        }
    }
}
