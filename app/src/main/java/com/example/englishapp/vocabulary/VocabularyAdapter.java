package com.example.englishapp.vocabulary;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder> {
    private List<Vocabulary> vocabularyList;
    private TextToSpeech textToSpeech;
    private String topic;

    public VocabularyAdapter(List<Vocabulary> vocabularyList, Context context, String topic) {
        this.vocabularyList = (vocabularyList != null) ? new ArrayList<>(vocabularyList) : new ArrayList<>();
        this.topic = topic != null ? topic : ""; // Đảm bảo topic không null
        textToSpeech = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
    }

    @Override
    public VocabularyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.huy_item_vocabulary, parent, false);
        return new VocabularyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VocabularyViewHolder holder, int position) {
        Vocabulary vocab = vocabularyList.get(position);
        if (vocab != null) {
            holder.tvWord.setText(vocab.getWord() != null ? vocab.getWord() : "");
            holder.tvTranscription.setText(vocab.getTranscription() != null ? vocab.getTranscription() : "");
            holder.tvMeaning.setText(vocab.getMeaning() != null ? vocab.getMeaning() : "");

            // Tải hình ảnh
            if (vocab.getImage() != null) {
                int resId = holder.itemView.getContext().getResources().getIdentifier(
                        vocab.getImage(), "drawable", holder.itemView.getContext().getPackageName());
                if (resId != 0) {
                    holder.ivImage.setImageResource(resId); // Hình ảnh cụ thể của từ (như family_image)
                } else {
                    // Hình ảnh cụ thể không tồn tại, sử dụng hình ảnh của chủ đề
                    String topicImageName = topic.toLowerCase() + "_topic_image"; // Ví dụ: family_topic_image
                    int topicResId = holder.itemView.getContext().getResources().getIdentifier(
                            topicImageName, "drawable", holder.itemView.getContext().getPackageName());
                    if (topicResId != 0) {
                        holder.ivImage.setImageResource(topicResId); // Hình ảnh của chủ đề
                    } else {
                        holder.ivImage.setImageResource(R.drawable.ic_vocabulary); // Fallback cuối cùng
                    }
                }
            } else {
                // Nếu không có hình ảnh cụ thể, sử dụng hình ảnh của chủ đề
                String topicImageName = topic.toLowerCase() + "_topic_image";
                int topicResId = holder.itemView.getContext().getResources().getIdentifier(
                        topicImageName, "drawable", holder.itemView.getContext().getPackageName());
                if (topicResId != 0) {
                    holder.ivImage.setImageResource(topicResId);
                } else {
                    holder.ivImage.setImageResource(R.drawable.ic_vocabulary);
                }
            }

            holder.btnSpeak.setOnClickListener(v -> {
                if (vocab.getWord() != null) {
                    textToSpeech.speak(vocab.getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return vocabularyList.size();
    }

    static class VocabularyViewHolder extends RecyclerView.ViewHolder {
        TextView tvWord, tvTranscription, tvMeaning;
        ImageView ivImage;
        ImageButton btnSpeak;

        public VocabularyViewHolder(View itemView) {
            super(itemView);
            tvWord = itemView.findViewById(R.id.tv_word);
            tvTranscription = itemView.findViewById(R.id.tv_transcription);
            tvMeaning = itemView.findViewById(R.id.tv_meaning);
            ivImage = itemView.findViewById(R.id.iv_image);
            btnSpeak = itemView.findViewById(R.id.btn_speak);
            if (tvWord == null || tvTranscription == null || tvMeaning == null || ivImage == null || btnSpeak == null) {
                android.util.Log.e("VocabularyAdapter", "One or more views not found in item_vocabulary");
            }
        }
    }

    // Giải phóng TextToSpeech khi adapter bị hủy
    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}