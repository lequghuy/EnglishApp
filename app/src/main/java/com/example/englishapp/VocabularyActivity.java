package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VocabularyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    private List<String> topicList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        recyclerView = findViewById(R.id.recycler_vocabulary);
        if (recyclerView == null) {
            android.util.Log.e("VocabularyActivity", "RecyclerView not found");
            return;
        }

        dbHelper = new DatabaseHelper(this);
        topicList = dbHelper.getAllTopics();
        if (topicList == null || topicList.isEmpty()) {
            android.util.Log.w("VocabularyActivity", "No topics found");
            topicList = new ArrayList<>();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicAdapter(topicList, topic -> {
            Intent intent = new Intent(VocabularyActivity.this, TopicQuizActivity.class);
            intent.putExtra("TOPIC", topic);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        adapter = new TopicAdapter(topicList, topic -> {
            Intent intent = new Intent(VocabularyActivity.this, TopicQuizActivity.class);
            intent.putExtra("TOPIC", topic);
            startActivity(intent);
        });
    }
}