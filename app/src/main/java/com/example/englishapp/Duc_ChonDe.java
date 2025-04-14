package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

public class Duc_ChonDe extends AppCompatActivity {

    LinearLayout danhSachDe;
    ImageView nutQuayLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duc_activity_chon_de_qz);

        danhSachDe = findViewById(R.id.danhSachDe);
        nutQuayLai = findViewById(R.id.nutQuayLai);

        // Quay về màn hình chính
        nutQuayLai.setOnClickListener(view -> finish());

        // Tải danh sách đề từ JSON
        loadQuestionsFromJSON();
    }

    private void loadQuestionsFromJSON() {
        try {
            // Đọc dữ liệu JSON từ assets
            String jsonString = readJSONFromAsset();
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonString, Map.class);

            // Lấy tất cả các đề trong dữ liệu JSON
            for (int i = 1; i <= data.size(); i++) {
                // Tạo Button cho mỗi đề
                Button nutDe = new Button(this);
                nutDe.setText("Đề " + i);
                nutDe.setAllCaps(false);
                nutDe.setTextSize(18);
                nutDe.setBackgroundResource(R.drawable.duc_bg_card);
                nutDe.setPadding(24, 24, 24, 24);

                LinearLayout.LayoutParams thamSo = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                thamSo.setMargins(0, 20, 0, 0);
                nutDe.setLayoutParams(thamSo);

                final int deSo = i;

                nutDe.setOnClickListener(v -> {
                    // Khi chọn đề, chuyển đến màn hình câu hỏi với số đề
                    Intent chuyen = new Intent(Duc_ChonDe.this, Duc_BaiKT.class);
                    chuyen.putExtra("deSo", deSo);
                    startActivity(chuyen);
                });

                // Thêm button vào danh sách
                danhSachDe.addView(nutDe);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải dữ liệu từ JSON", Toast.LENGTH_SHORT).show();
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
}
