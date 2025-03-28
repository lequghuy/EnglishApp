package com.example.englishapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishapp.R;
import java.util.ArrayList;
import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder> implements Filterable {
    private List<Vocabulary> vocabularyList;
    private List<Vocabulary> vocabularyListFull;

    public VocabularyAdapter(List<Vocabulary> vocabularyList) {
        this.vocabularyList = (vocabularyList != null) ? new ArrayList<>(vocabularyList) : new ArrayList<>();
        this.vocabularyListFull = new ArrayList<>(this.vocabularyList);
    }

    @Override
    public VocabularyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
        return new VocabularyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VocabularyViewHolder holder, int position) {
        Vocabulary vocab = vocabularyList.get(position);
        if (vocab != null) {
            holder.tvWord.setText(vocab.getWord() != null ? vocab.getWord() : "");
            holder.tvMeaning.setText(vocab.getMeaning() != null ? vocab.getMeaning() : "");
            holder.tvTopic.setText(vocab.getTopic() != null ? "Chủ đề: " + vocab.getTopic() : "Chủ đề: Không có");
        }
    }

    @Override
    public int getItemCount() {
        return vocabularyList.size();
    }

    @Override
    public Filter getFilter() {
        return vocabularyFilter;
    }

    private final Filter vocabularyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Vocabulary> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(vocabularyListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Vocabulary vocab : vocabularyListFull) {
                    if (vocab.getWord().toLowerCase().contains(filterPattern) ||
                            vocab.getMeaning().toLowerCase().contains(filterPattern) ||
                            vocab.getTopic().toLowerCase().contains(filterPattern)) {
                        filteredList.add(vocab);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            vocabularyList.clear();
            if (results.values != null) {
                vocabularyList.addAll((List<Vocabulary>) results.values);
            }
            notifyDataSetChanged();
        }
    };

    static class VocabularyViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord, tvMeaning, tvTopic;

        public VocabularyViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tv_word);
            tvMeaning = itemView.findViewById(R.id.tv_meaning);
            tvTopic = itemView.findViewById(R.id.tv_topic);
            if (tvWord == null || tvMeaning == null || tvTopic == null) {
                android.util.Log.e("VocabularyAdapter", "One or more TextViews not found in item_vocabulary");
            }
        }
    }
}