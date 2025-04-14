package com.example.englishapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Duc_BaiKT extends AppCompatActivity {

    private TextView txtChuDe, txtSoCau, txtCauHoi;
    private LinearLayout chonCauHoi, txtLuaChon;
    private Button btnXacNhan;

    private List<Duc_QuizQuestion> questionList;
    private int currentIndex = 0;
    private int selectedAnswerIndex = -1;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duc_activity_quiz); // Dùng đúng tên XML bạn đã chỉnh

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

        // Tải dữ liệu câu hỏi từ file JSON
        loadQuestions();
        setupQuestionNumberCircles();
        showQuestion(currentIndex);

        // Bắt sự kiện xác nhận
        btnXacNhan.setOnClickListener(view -> {
            if (selectedAnswerIndex == -1) {
                Toast.makeText(Duc_BaiKT.this, "Vui lòng chọn một đáp án!", Toast.LENGTH_SHORT).show();  // sửa lại Toast
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
                Toast.makeText(Duc_BaiKT.this, "Bạn đã hoàn thành! Điểm: " + score + "/" + questionList.size(), Toast.LENGTH_LONG).show();
                // TODO: Hiển thị kết quả hoặc chuyển trang
            }
        });
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();
        try {
            // Đọc dữ liệu JSON từ assets
            String jsonString = readJSONFromAsset();
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonString, Map.class);

            // Lấy đề từ Intent (giả sử bạn truyền số đề qua)
            int deSo = getIntent().getIntExtra("deSo", 1);
            Map<String, Object> de = (Map<String, Object>) data.get("Đề " + deSo);
            ArrayList<Map<String, String>> cauHoiList = (ArrayList<Map<String, String>>) de.get("cauHoi");

            // Duyệt qua danh sách câu hỏi và thêm vào questionList
            for (Map<String, String> cauHoiData : cauHoiList) {
                String noiDung = cauHoiData.get("noiDungCauHoi");
                String luaChon_1 = cauHoiData.get("luaChon_1");
                String luaChon_2 = cauHoiData.get("luaChon_2");
                String luaChon_3 = cauHoiData.get("luaChon_3");
                String luaChon_4 = cauHoiData.get("luaChon_4");
                String dapAnDung = cauHoiData.get("dapAnDung");

                // Tạo đối tượng câu hỏi và thêm vào danh sách
                Duc_QuizQuestion cauHoi = new Duc_QuizQuestion(noiDung, new String[]{luaChon_1, luaChon_2, luaChon_3, luaChon_4}, Arrays.asList(luaChon_1, luaChon_2, luaChon_3, luaChon_4).indexOf(dapAnDung));
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
            // Đọc file JSON từ thư mục assets
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
            RadioButton radio = new RadioButton(Duc_BaiKT.this);  // Chắc chắn sử dụng đúng context
            radio.setText((char) ('A' + i) + ". " + options[i]);
            radio.setTextSize(16);
            radio.setPadding(8, 16, 8, 16);
            radio.setButtonDrawable(null);
            radio.setBackgroundResource(R.drawable.duc_bg_card);
            radio.setOnClickListener(view -> selectedAnswerIndex = finalI);
            txtLuaChon.addView(radio);
        }
    }

    private void setupQuestionNumberCircles() {
        chonCauHoi.removeAllViews();
        for (int i = 0; i < questionList.size(); i++) {
            TextView number = new TextView(Duc_BaiKT.this);  // Chắc chắn sử dụng đúng context
            number.setText(String.valueOf(i + 1));
            number.setPadding(24, 16, 24, 16);
            number.setBackgroundResource(R.drawable.duc_bg_card);
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
