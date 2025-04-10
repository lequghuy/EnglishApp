package com.example.englishapp.Grammar;

public class PracticeItem {
    String question;
    String[] options;
    int correctIndex;

    PracticeItem(String question, String[] options, int correctIndex) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
    }
}
