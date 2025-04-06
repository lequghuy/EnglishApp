package com.example.englishapp.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishapp.DatabaseHelper;
import com.example.englishapp.R;
import java.util.ArrayList;
import java.util.List;

public class TopicVocabularyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VocabularyAdapter adapter;
    private List<Vocabulary> vocabularyList;
    private DatabaseHelper dbHelper;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huy_activity_topic_vocabulary);

        recyclerView = findViewById(R.id.recycler_topic_vocabulary);
        Button btnStartQuiz = findViewById(R.id.btn_start_quiz);

        if (recyclerView == null || btnStartQuiz == null) {
            android.util.Log.e("TopicVocabularyActivity", "RecyclerView or Button not found in activity_topic_vocabulary.xml");
            Toast.makeText(this, "Lỗi giao diện, vui lòng thử lại!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        topic = getIntent().getStringExtra("TOPIC");
        if (topic == null) {
            android.util.Log.e("TopicVocabularyActivity", "No topic received from intent");
            Toast.makeText(this, "Không nhận được chủ đề!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setTitle("Chủ đề: " + topic);

        dbHelper = new DatabaseHelper(this);
        try {
            vocabularyList = dbHelper.getVocabularyByTopic(topic);
        } catch (Exception e) {
            android.util.Log.e("TopicVocabularyActivity", "Error fetching vocabulary: " + e.getMessage());
            vocabularyList = new ArrayList<>();
        }

        if (vocabularyList == null || vocabularyList.isEmpty()) {
            android.util.Log.w("TopicVocabularyActivity", "No vocabulary found for topic: " + topic);
            vocabularyList = new ArrayList<>();
        } else {
            // Lọc danh sách: chỉ giữ từ có word khớp với topic
            List<Vocabulary> filteredList = new ArrayList<>();
            for (Vocabulary vocab : vocabularyList) {
                if (vocab.getWord() != null && vocab.getWord().equalsIgnoreCase(topic)) {
                    filteredList.add(vocab);
                    break; // Chỉ lấy từ đầu tiên khớp
                }
            }

            if (filteredList.isEmpty()) {
                android.util.Log.w("TopicVocabularyActivity", "No vocabulary found with word matching topic: " + topic);
                vocabularyList = new ArrayList<>(); // Nếu không tìm thấy từ khớp, để danh sách rỗng
            } else {
                vocabularyList = filteredList; // Cập nhật danh sách với từ đã lọc
            }
        }

        // Log danh sách từ vựng để kiểm tra
        android.util.Log.d("TopicVocabularyActivity", "Vocabulary list for topic " + topic + ":");
        for (Vocabulary vocab : vocabularyList) {
            android.util.Log.d("TopicVocabularyActivity", "Word: " + vocab.getWord() + ", Topic: " + vocab.getTopic());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VocabularyAdapter(vocabularyList, this);
        recyclerView.setAdapter(adapter);

        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(TopicVocabularyActivity.this, TopicQuizActivity.class);
            intent.putExtra("TOPIC", topic);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.shutdown();
        }
    }
}