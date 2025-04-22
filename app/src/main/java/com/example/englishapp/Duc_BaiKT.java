package com.example.englishapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.*;

public class Duc_BaiKT extends AppCompatActivity {

    private TextView txtChuDe, txtSoCau, txtCauHoi;
    private LinearLayout chonCauHoi, txtLuaChon;
    private Button btnXacNhan;
    private MaterialButton btnLui, btnTien;
    private List<Duc_QuizQuestion> questionList;
    private int currentIndex = 0;
    private int selectedAnswerIndex = -1;
    private int score = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>();
    private boolean isFinished = false;
    private boolean userSelectedByNumber = false;
    private boolean isReviewMode = false;
    private List<Integer> userAnswersList;
    private List<Integer> correctAnswersList;
    private TextView timerText;
    private CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 30 * 1000; // 30 phút
    ArrayList<String[]> luaChonList = new ArrayList<>();


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
        btnLui = findViewById(R.id.btnLui);
        btnTien = findViewById(R.id.btnTien);

        txtChuDe.setText("Làm bài kiểm tra");

        // bấm giờ
        timerText = findViewById(R.id.timerText);
        // Nhận intent và kiểm tra chế độ xem lại
        Intent intent = getIntent();
        isReviewMode = intent.getBooleanExtra("isReviewMode", false);

// bấm giờ
        timerText = findViewById(R.id.timerText);
        if (isReviewMode) {
            // Nếu là chế độ xem lại thì ẩn đồng hồ
            timerText.setVisibility(View.GONE);
        } else {
            // Nếu là làm bài, hiện cảnh báo và bắt đầu đếm giờ
            new AlertDialog.Builder(this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn có 30 phút để làm bài. Bấm OK để bắt đầu.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        startTimer(); // Bắt đầu đếm giờ
                    })
                    .show();
        }

        if (isReviewMode) {
            userAnswersList = intent.getIntegerArrayListExtra("userAnswers");
            correctAnswersList = intent.getIntegerArrayListExtra("correctAnswers");
            ArrayList<String> cauHoiList = intent.getStringArrayListExtra("cauHoiList");
            ArrayList<String> dapAnDung = intent.getStringArrayListExtra("dapAnDung");
            ArrayList<String> dapAnNguoiChon = intent.getStringArrayListExtra("dapAnNguoiChon");
            ArrayList<Integer> cauSai = intent.getIntegerArrayListExtra("cauSai");
            int tongSoCau = intent.getIntExtra("tongSoCau", 0);
            int soCauDung = intent.getIntExtra("soCauDung", 0);

            if (userAnswersList == null || correctAnswersList == null) {
                Log.e("Duc_BaiKT", "Dữ liệu xem lại không hợp lệ!");
                finish();
                return;
            }

            btnXacNhan.setText("Quay lại trang thống kê");
            btnXacNhan.setOnClickListener(v -> {
                Intent backToThongKe = new Intent(Duc_BaiKT.this, Duc_ThongKeActivity.class);

                // Dùng chính intent hiện tại để lấy dữ liệu, không gọi lại getIntent() liên tục
                Intent currentIntent = getIntent();

                backToThongKe.putExtra("tongSoCau", currentIntent.getIntExtra("tongSoCau", 0));
                backToThongKe.putExtra("soCauDung", currentIntent.getIntExtra("soCauDung", 0));
                backToThongKe.putIntegerArrayListExtra("cauSai", currentIntent.getIntegerArrayListExtra("cauSai"));
                backToThongKe.putIntegerArrayListExtra("cauChuaChon", currentIntent.getIntegerArrayListExtra("cauChuaChon"));
                backToThongKe.putStringArrayListExtra("dapAnDung", currentIntent.getStringArrayListExtra("dapAnDung"));
                backToThongKe.putStringArrayListExtra("dapAnNguoiChon", currentIntent.getStringArrayListExtra("dapAnNguoiChon"));
                backToThongKe.putStringArrayListExtra("cauHoiList", currentIntent.getStringArrayListExtra("cauHoiList"));
                backToThongKe.putIntegerArrayListExtra("userAnswers", new ArrayList<>(userAnswersList));
                backToThongKe.putIntegerArrayListExtra("correctAnswers", new ArrayList<>(correctAnswersList));

                startActivity(backToThongKe);
                finish();
            });


        } else {
            btnXacNhan.setText("Nộp bài");
            btnXacNhan.setOnClickListener(view -> {
                if (isFinished) return;

                userSelectedByNumber = true;
                int answeredCount = userAnswers.size();
                int totalCount = questionList.size();
                String message;

                if (answeredCount < totalCount) {
                    message = "Bạn mới làm được " + answeredCount + "/" + totalCount + " câu hỏi. Bạn có muốn nộp bài không?";
                } else {
                    message = "Bạn chắc chắn muốn nộp bài?";
                }

                new AlertDialog.Builder(Duc_BaiKT.this)
                        .setTitle("Xác nhận nộp bài")
                        .setMessage(message)
                        .setPositiveButton("Yes", (dialog, which) -> submitResult())
                        .setNegativeButton("No", null)
                        .show();
            });

        }

        if (isReviewMode) {
            // Nhận dữ liệu từ Intent
            ArrayList<String> cauHoiList = intent.getStringArrayListExtra("cauHoiList");
            ArrayList<Integer> correctAnswersList = intent.getIntegerArrayListExtra("correctAnswers");
            Serializable serializable = intent.getSerializableExtra("luaChonList");

            // Kiểm tra và ép kiểu cho luaChonList
            List<String[]> luaChonList;
            if (serializable instanceof String[][]) {
                String[][] luaChonArray = (String[][]) serializable;
                luaChonList = Arrays.asList(luaChonArray);
            } else if (serializable instanceof ArrayList) {
                luaChonList = (ArrayList<String[]>) serializable;
            } else {
                Log.e("Duc_BaiKT", "Không thể đọc luaChonList từ Intent!");
                finish();
                return;
            }

            // Tạo danh sách câu hỏi từ dữ liệu truyền vào
            questionList = new ArrayList<>();
            for (int i = 0; i < cauHoiList.size(); i++) {
                String cauHoi = cauHoiList.get(i);
                String[] luaChon = luaChonList.get(i); // Lấy mảng lựa chọn cho câu hỏi thứ i
                int correctIndex = correctAnswersList.get(i);

                questionList.add(new Duc_QuizQuestion(cauHoi, luaChon, correctIndex));
            }

            setupQuestionNumberCircles();
            showQuestion(currentIndex);
        } else {
            // Làm bài bình thường
            loadQuestions();
            setupQuestionNumberCircles();
            showQuestion(currentIndex);
        }


        btnLui.setOnClickListener(view -> {
            if (currentIndex > 0) {
                currentIndex--;
                selectedAnswerIndex = userAnswers.getOrDefault(currentIndex, -1);
                showQuestion(currentIndex);
            }
        });

        btnTien.setOnClickListener(view -> {
            if (currentIndex < questionList.size() - 1) {
                currentIndex++;
                selectedAnswerIndex = userAnswers.getOrDefault(currentIndex, -1);
                showQuestion(currentIndex);
            }
        });
    }

    private void submitResult() {
        ArrayList<String[]> luaChonList = new ArrayList<>();
        score = 0;
        ArrayList<Integer> cauSai = new ArrayList<>();
        ArrayList<Integer> cauChuaChon = new ArrayList<>();
        ArrayList<String> dapAnDung = new ArrayList<>();
        ArrayList<String> dapAnNguoiChon = new ArrayList<>();
        ArrayList<String> cauHoiList = new ArrayList<>();
        ArrayList<Integer> userAnswersForReview = new ArrayList<>();
        ArrayList<Integer> correctAnswersForReview = new ArrayList<>();


        for (int i = 0; i < questionList.size(); i++) {
            Duc_QuizQuestion question = questionList.get(i);
            luaChonList.add(question.getOptions());
            int correct = question.getCorrectAnswerIndex();
            int user = userAnswers.getOrDefault(i, -1);

            if (user == correct) {
                score++;
            } else {
                cauSai.add(i);
            }

            if (user == -1) {
                cauChuaChon.add(i);
            }

            cauHoiList.add(question.getQuestion());
            dapAnDung.add(question.getOptions()[correct]);
            dapAnNguoiChon.add(user != -1 ? question.getOptions()[user] : "(Không chọn)");
            userAnswersForReview.add(user);
            correctAnswersForReview.add(correct);
        }

        isFinished = true;
        btnXacNhan.setEnabled(false);
        if (countDownTimer != null) countDownTimer.cancel(); // ❗ hủy timer nếu còn

        highlightFinalResult();

        Intent intentThongKe = new Intent(Duc_BaiKT.this, Duc_ThongKeActivity.class);
        intentThongKe.putExtra("tongSoCau", questionList.size());
        intentThongKe.putExtra("soCauDung", score);
        intentThongKe.putIntegerArrayListExtra("cauSai", cauSai);
        intentThongKe.putIntegerArrayListExtra("cauChuaChon", cauChuaChon);
        intentThongKe.putStringArrayListExtra("dapAnDung", dapAnDung);
        intentThongKe.putStringArrayListExtra("dapAnNguoiChon", dapAnNguoiChon);
        intentThongKe.putStringArrayListExtra("cauHoiList", cauHoiList);
        intentThongKe.putIntegerArrayListExtra("userAnswers", userAnswersForReview);
        intentThongKe.putIntegerArrayListExtra("correctAnswers", correctAnswersForReview);
        intentThongKe.putExtra("luaChonList", luaChonList.toArray(new String[0][]));
        startActivity(intentThongKe);
        finish();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(TOTAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerText.setText(time);
            }

            @Override
            public void onFinish() {
                if (!isFinished) {
                    autoSubmit();
                }
            }
        };
        countDownTimer.start();
    }

    private void autoSubmit() {
        runOnUiThread(() -> {
            if (isFinished) return;

            new AlertDialog.Builder(Duc_BaiKT.this)
                    .setTitle("Hết giờ")
                    .setMessage("Thời gian làm bài đã kết thúc. Bấm OK để xem kết quả.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> submitResult())
                    .show();
        });
    }



    private ArrayList<Integer> getUnansweredQuestions(List<Integer> userAnswersList) {
        ArrayList<Integer> unanswered = new ArrayList<>();
        for (int i = 0; i < userAnswersList.size(); i++) {
            if (userAnswersList.get(i) == -1) {
                unanswered.add(i);
            }
        }
        return unanswered;
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

            if (!isReviewMode) {
                radio.setOnClickListener(view -> {
                    selectedAnswerIndex = finalI;
                    userAnswers.put(currentIndex, selectedAnswerIndex);
                    highlightSelectedAnswer(finalI);
                    setupQuestionNumberCircles();
                });
            } else {
                radio.setEnabled(false); // Vô hiệu hóa trong chế độ xem lại
            }
            txtLuaChon.addView(radio);
        }

        if (isReviewMode) {
            int userAnswer = userAnswersList.get(index);
            int correctAnswer = correctAnswersList.get(index);

            // Điều chỉnh kích thước biểu tượng
            int iconSize = (int) (16 * getResources().getDisplayMetrics().density); // 16dp
            Drawable checkIcon = ContextCompat.getDrawable(this, R.drawable.duc_ic_check);
            Drawable wrongIcon = ContextCompat.getDrawable(this, R.drawable.duc_ic_wrong);
            if (checkIcon != null) {
                checkIcon.setBounds(0, 0, iconSize, iconSize);
            }
            if (wrongIcon != null) {
                wrongIcon.setBounds(0, 0, iconSize, iconSize);
            }

            for (int i = 0; i < txtLuaChon.getChildCount(); i++) {
                RadioButton radio = (RadioButton) txtLuaChon.getChildAt(i);
                if (i == correctAnswer) {
                    radio.setBackgroundResource(R.drawable.duc_bg_correct); // Màu xanh lá cho đáp án đúng
                    radio.setCompoundDrawables(null, null, checkIcon, null); // Dấu tích
                } else if (userAnswer != -1 && i == userAnswer && userAnswer != correctAnswer) {
                    radio.setBackgroundResource(R.drawable.duc_bg_wrong); // Màu đỏ cho đáp án sai
                    radio.setCompoundDrawables(null, null, wrongIcon, null); // Dấu x
                } else if (userAnswer == -1 && i != correctAnswer) {
                    // Đánh dấu x cho các lựa chọn sai nếu chưa chọn đáp án
                    radio.setBackgroundResource(R.drawable.duc_bg_wrong);
                    radio.setCompoundDrawables(null, null, wrongIcon, null);
                } else {
                    radio.setBackgroundResource(R.drawable.duc_bg_card); // Giữ nguyên nền mặc định
                }
            }
        } else {
            if (userAnswers.containsKey(index)) {
                highlightSelectedAnswer(userAnswers.get(index));
                selectedAnswerIndex = userAnswers.get(index);
            }
        }

        userSelectedByNumber = false;
        setupQuestionNumberCircles();

        btnLui.setEnabled(currentIndex > 0);
        btnTien.setEnabled(currentIndex < questionList.size() - 1);
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

            if (isReviewMode) {
                int userAnswer = userAnswersList.get(i);
                int correctAnswer = correctAnswersList.get(i);
                if (userAnswer == correctAnswer) {
                    number.setBackgroundResource(R.drawable.duc_bg_correct);
                } else {
                    number.setBackgroundResource(R.drawable.duc_bg_wrong);
                }
            } else {
                if (userAnswers.containsKey(i)) {
                    number.setBackgroundResource(R.drawable.duc_bg_done);
                } else {
                    number.setBackgroundResource(R.drawable.duc_bg_card);
                }
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