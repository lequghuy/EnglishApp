package com.example.englishapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<String> topicList;
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(String topic);
    }

    public TopicAdapter(List<String> topicList, OnTopicClickListener listener) {
        this.topicList = topicList;
        this.listener = listener;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        String topic = topicList.get(position);
        holder.tvTopic.setText(topic);
        holder.itemView.setOnClickListener(v -> listener.onTopicClick(topic));
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopic;

        public TopicViewHolder(View itemView) {
            super(itemView);
            tvTopic = itemView.findViewById(R.id.tv_topic);
        }
    }
}