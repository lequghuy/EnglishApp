package com.example.englishapp.Grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.MainActivity;
import com.example.englishapp.R;

import java.util.ArrayList;

public class GrammarTopicsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<GrammarTopic> topicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phuonganh_activity_grammar_topics);

        Button btnBackToMain = findViewById(R.id.btnBackToMainFromTopic);
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(GrammarTopicsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        listView = findViewById(R.id.listViewGrammarTopics);

        // Danh sách chủ đề
        topicList = new ArrayList<>();
        topicList.add(new GrammarTopic("Thì hiện tại đơn"));
        topicList.add(new GrammarTopic("Thì quá khứ đơn"));
        topicList.add(new GrammarTopic("Câu điều kiện"));
        topicList.add(new GrammarTopic("Bị động"));
        topicList.add(new GrammarTopic("Câu gián tiếp"));

        // Tạo adapter và gán vào ListView
        GrammarTopicAdapter adapter = new GrammarTopicAdapter(this, topicList);
        listView.setAdapter(adapter);

        // Bắt sự kiện click
        listView.setOnItemClickListener((parent, view, position, id) -> {
            GrammarTopic selectedTopic = topicList.get(position);
            String topicName = selectedTopic.getName();

            Intent intent = new Intent(GrammarTopicsActivity.this, GrammarDetailActivity.class);
            intent.putExtra("topic", topicName);  // Truyền tên chủ đề
            startActivity(intent);
        });
    }
}