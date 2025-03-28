package com.example.englishapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizQuestion {
    private int id;
    private String question;
    private String correctAnswer;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;
    private String topic;

    public QuizQuestion(int id, String question, String correctAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3, String topic) {
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
        this.topic = topic;
    }

    public int getId() { return id; }
    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getTopic() { return topic; }

    public List<String> getAnswers() {
        List<String> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.add(wrongAnswer1);
        answers.add(wrongAnswer2);
        answers.add(wrongAnswer3);
        Collections.shuffle(answers);
        return answers;
    }
}