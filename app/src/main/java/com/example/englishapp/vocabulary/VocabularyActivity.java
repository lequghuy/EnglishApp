package com.example.englishapp.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishapp.DatabaseHelper;
import com.example.englishapp.R;

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
        setContentView(R.layout.huy_activity_vocabulary);

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
        } else {
            // Log danh sách chủ đề để kiểm tra
            android.util.Log.d("VocabularyActivity", "Topics found: " + topicList.toString());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicAdapter(topicList, topic -> {
            Intent intent = new Intent(VocabularyActivity.this, TopicVocabularyActivity.class);
            intent.putExtra("TOPIC", topic);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}