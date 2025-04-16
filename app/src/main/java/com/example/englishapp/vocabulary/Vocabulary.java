package com.example.englishapp.vocabulary;

public class Vocabulary {
    private int id;
    private String word;
    private String transcription;
    private String meaning;
    private String image;
    private String topic;

    public Vocabulary(int id, String word, String transcription, String meaning, String image, String topic) {
        this.id = id;
        this.word = word;
        this.transcription = transcription;
        this.meaning = meaning;
        this.image = image;
        this.topic = topic;
    }

    public int getId() { return id; }
    public String getWord() { return word; }
    public String getTranscription() { return transcription; }
    public String getMeaning() { return meaning; }
    public String getImage() { return image; }
    public String getTopic() { return topic; }
}