package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class Duc_ThongKeActivity extends AppCompatActivity {

    TextView txtDiem, txtTiLe, txtCauSai, txtCauChuaChon;
    Button btnXemLai, btnLamDeKhac;

    // Khai báo biến toàn cục để dùng trong lambda
    int tongSoCau, soCauDung;
    ArrayList<Integer> cauSai, cauChuaChon;
    ArrayList<String> dapAnDung, dapAnNguoiChon, cauHoiList;
    List<Integer> userAnswers;
    List<Integer> correctAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duc_activity_thong_ke);

        // Ánh xạ view
        txtDiem = findViewById(R.id.txtDiem);
        txtTiLe = findViewById(R.id.txtTiLe);
        txtCauSai = findViewById(R.id.txtCauSai);
        txtCauChuaChon = findViewById(R.id.txtCauChuaChon);
        btnXemLai = findViewById(R.id.btnXemLai);
        btnLamDeKhac = findViewById(R.id.btnLamDeKhac);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        tongSoCau = intent.getIntExtra("tongSoCau", 0);
        soCauDung = intent.getIntExtra("soCauDung", 0);
        cauSai = intent.getIntegerArrayListExtra("cauSai");
        cauChuaChon = intent.getIntegerArrayListExtra("cauChuaChon");
        dapAnDung = intent.getStringArrayListExtra("dapAnDung");
        dapAnNguoiChon = intent.getStringArrayListExtra("dapAnNguoiChon");
        cauHoiList = intent.getStringArrayListExtra("cauHoiList");
        userAnswers = intent.getIntegerArrayListExtra("userAnswers");
        correctAnswers = intent.getIntegerArrayListExtra("correctAnswers");

        // Kiểm tra null để tránh crash
        if (cauSai == null) cauSai = new ArrayList<>();
        if (cauChuaChon == null) cauChuaChon = new ArrayList<>();
        if (dapAnDung == null) dapAnDung = new ArrayList<>();
        if (dapAnNguoiChon == null) dapAnNguoiChon = new ArrayList<>();
        if (cauHoiList == null) cauHoiList = new ArrayList<>();
        if (userAnswers == null) userAnswers = new ArrayList<>();
        if (correctAnswers == null) correctAnswers = new ArrayList<>();

        // Hiển thị dữ liệu
        txtDiem.setText("Điểm: " + soCauDung + "/" + tongSoCau);
        int tile = (tongSoCau > 0) ? (soCauDung * 100 / tongSoCau) : 0;
        txtTiLe.setText("Tỷ lệ đúng: " + tile + "%");
        txtCauSai.setText("Số câu sai: " + cauSai.size());
        txtCauChuaChon.setText("Số câu chưa chọn: " + cauChuaChon.size());

        // Nút xem lại bài
        btnXemLai.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(Duc_ThongKeActivity.this, Duc_BaiKT.class);
            reviewIntent.putExtra("isReviewMode", true);
            reviewIntent.putExtra("tongSoCau", tongSoCau);
            reviewIntent.putExtra("soCauDung", soCauDung);
            reviewIntent.putIntegerArrayListExtra("cauSai", cauSai);
            reviewIntent.putIntegerArrayListExtra("cauChuaChon", cauChuaChon);
            reviewIntent.putStringArrayListExtra("dapAnDung", dapAnDung);
            reviewIntent.putStringArrayListExtra("dapAnNguoiChon", dapAnNguoiChon);
            reviewIntent.putStringArrayListExtra("cauHoiList", cauHoiList);
            reviewIntent.putIntegerArrayListExtra("userAnswers", new ArrayList<>(userAnswers));
            reviewIntent.putIntegerArrayListExtra("correctAnswers", new ArrayList<>(correctAnswers));
            reviewIntent.putExtra("luaChonList", getIntent().getSerializableExtra("luaChonList"));
            startActivity(reviewIntent);
            finish();
        });

        // Sự kiện nút Làm đề khác
        btnLamDeKhac.setOnClickListener(v -> {
            Intent intent1 = new Intent(Duc_ThongKeActivity.this, Duc_ChonDe.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            finish();
        });
    }
}
