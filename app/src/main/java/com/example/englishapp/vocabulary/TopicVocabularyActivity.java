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
    private RecyclerView recyclerTopicVocabulary;
    private VocabularyAdapter vocabularyAdapter;
    private List<Vocabulary> vocabularyList;
    private DatabaseHelper dbHelper;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huy_activity_topic_vocabulary);

        // Khởi tạo RecyclerView
        recyclerTopicVocabulary = findViewById(R.id.recycler_topic_vocabulary);

        // Lấy chủ đề từ Intent
        topic = getIntent().getStringExtra("TOPIC");
        if (topic == null) {
            Toast.makeText(this, "Không tìm thấy chủ đề", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo DatabaseHelper và lấy danh sách từ vựng theo chủ đề
        dbHelper = new DatabaseHelper(this);
        vocabularyList = dbHelper.getVocabularyByTopic(topic);
        if (vocabularyList == null) {
            vocabularyList = new ArrayList<>();
        }

        // Khởi tạo adapter với topic để hiển thị hình ảnh theo chủ đề
        vocabularyAdapter = new VocabularyAdapter(vocabularyList, this, topic);
        recyclerTopicVocabulary.setLayoutManager(new LinearLayoutManager(this));
        recyclerTopicVocabulary.setAdapter(vocabularyAdapter);

        // Thiết lập nút "Làm bài kiểm tra"
        Button btnStartQuiz = findViewById(R.id.btn_start_quiz);
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(TopicVocabularyActivity.this, TopicQuizActivity.class);
            intent.putExtra("TOPIC", topic);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vocabularyAdapter != null) {
            vocabularyAdapter.shutdown(); // Giải phóng TextToSpeech
        }
    }
}