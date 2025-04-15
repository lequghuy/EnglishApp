package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.englishapp.Grammar.GrammarTopicsActivity;

public class MainActivity extends AppCompatActivity {

    CardView cardQuiz;
    CardView cardGrammar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phuonganh_activity_main);

        addView();
        addEvents();
    }

    // Ánh xạ các View từ layout
    private void addView() {
        cardQuiz = findViewById(R.id.card_quiz);
        cardGrammar = findViewById(R.id.card_grammar);
    }

    // Thiết lập sự kiện
    private void addEvents() {
        cardQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Duc_ChonDe.class);
                startActivity(intent);
            }
        });

        cardGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GrammarTopicsActivity.class);
                startActivity(intent);
            }
        });
    }


    public void onClickQuiz(View view) {
        Intent intent = new Intent(this, Duc_ChonDe.class);
        startActivity(intent);
    }
}
