package com.example.englishapp.Grammar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.englishapp.R;

import java.util.List;

public class GrammarTopicAdapter extends ArrayAdapter<GrammarTopic> {

    Context context;
    List<GrammarTopic> topics;

    public GrammarTopicAdapter(Context context, List<GrammarTopic> topics) {
        super(context, 0, topics);
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.phuonganh_item_grammar_topic, parent, false);
        }

        TextView txtTopicName = convertView.findViewById(R.id.txtTopicName);

        GrammarTopic topic = topics.get(position);
        txtTopicName.setText(topic.getName());

        return convertView;
    }
}
