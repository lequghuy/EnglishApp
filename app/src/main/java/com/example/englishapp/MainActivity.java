package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView cardVocabulary, cardGrammar, cardQuiz, cardStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardVocabulary = findViewById(R.id.card_vocabulary);
        cardGrammar = findViewById(R.id.card_grammar);
        cardQuiz = findViewById(R.id.card_quiz);
        cardStats = findViewById(R.id.card_stats);

        if (cardVocabulary != null) {
            cardVocabulary.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
                startActivity(intent);
            });
        } else {
            android.util.Log.e("MainActivity", "CardView card_vocabulary not found");
        }

        if (cardGrammar != null) {
            cardGrammar.setOnClickListener(v -> android.util.Log.d("MainActivity", "Grammar card clicked"));
        }

        if (cardQuiz != null) {
            cardQuiz.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            });
        } else {
            android.util.Log.e("MainActivity", "CardView card_quiz not found");
        }

        if (cardStats != null) {
            cardStats.setOnClickListener(v -> android.util.Log.d("MainActivity", "Stats card clicked"));
        }
    }
}