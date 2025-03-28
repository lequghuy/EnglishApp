package com.example.englishapp;

public class Vocabulary {
    private int id;
    private String word;
    private String meaning;
    private String topic;

    public Vocabulary(int id, String word, String meaning, String topic) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.topic = topic;
    }

    public int getId() { return id; }
    public String getWord() { return word; }
    public String getMeaning() { return meaning; }
    public String getTopic() { return topic; }
}