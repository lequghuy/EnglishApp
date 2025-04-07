package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView cardQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addView();
    }

    // ánh xạ views từ xml
    private void addView() {
        cardQuiz =(CardView) findViewById(R.id.card_quiz);
    }

    public void onClickQuiz(View view) {
        Intent intent = new Intent(this, BaiKT.class);
        startActivity(intent);
    }
}
