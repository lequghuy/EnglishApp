package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.englishapp.vocabulary.VocabularyActivity;

public class MainActivity extends AppCompatActivity {

    private CardView cardVocabulary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardVocabulary = findViewById(R.id.card_vocabulary);

        if (cardVocabulary != null) {
            cardVocabulary.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
                startActivity(intent);
            });
        } else {
            android.util.Log.e("MainActivity", "CardView card_vocabulary not found");
        }
    }
}