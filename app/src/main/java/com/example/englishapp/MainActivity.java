package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.englishapp.vocabulary.VocabularyActivity;
import com.example.englishapp.Grammar.GrammarTopicsActivity;

public class MainActivity extends AppCompatActivity {

    private CardView cardQuiz;
    private CardView cardGrammar;
    private CardView cardVocabulary;

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
        cardVocabulary = findViewById(R.id.card_vocabulary);
    }

    // Thiết lập sự kiện
    private void addEvents() {
        if (cardQuiz != null) {
            cardQuiz.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, Duc_ChonDe.class);
                startActivity(intent);
            });
        } else {
            android.util.Log.e("MainActivity", "CardView card_quiz not found");
        }

        if (cardGrammar != null) {
            cardGrammar.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, GrammarTopicsActivity.class);
                startActivity(intent);
            });
        } else {
            android.util.Log.e("MainActivity", "CardView card_grammar not found");
        }

        if (cardVocabulary != null) {
            cardVocabulary.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
                startActivity(intent);
            });
        } else {
            android.util.Log.e("MainActivity", "CardView card_vocabulary not found");
        }
    }

    public void onClickQuiz(View view) {
        Intent intent = new Intent(this, Duc_ChonDe.class);
        startActivity(intent);
    }
}