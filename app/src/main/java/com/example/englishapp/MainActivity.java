package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.englishapp.Grammar.GrammarTopicsActivity;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tìm CardView theo ID
        CardView cardGrammar = findViewById(R.id.card_grammar);

        // Gán sự kiện click vào CardView
        cardGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang GrammarActivity
                Intent intent = new Intent(MainActivity.this, GrammarTopicsActivity.class);
                startActivity(intent); // Bắt đầu GrammarActivity
            }
        });
    }
}