package com.example.englishapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EnglishApp.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng Vocabulary
    private static final String TABLE_VOCABULARY = "vocabulary";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_MEANING = "meaning";
    private static final String COLUMN_TOPIC = "topic";

    // Bảng Quiz
    private static final String TABLE_QUIZ = "quiz";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_CORRECT_ANSWER = "correct_answer";
    private static final String COLUMN_WRONG_ANSWER_1 = "wrong_answer_1";
    private static final String COLUMN_WRONG_ANSWER_2 = "wrong_answer_2";
    private static final String COLUMN_WRONG_ANSWER_3 = "wrong_answer_3";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VOCABULARY_TABLE = "CREATE TABLE " + TABLE_VOCABULARY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WORD + " TEXT, " +
                COLUMN_MEANING + " TEXT, " +
                COLUMN_TOPIC + " TEXT)";
        db.execSQL(CREATE_VOCABULARY_TABLE);

        db.execSQL("INSERT INTO " + TABLE_VOCABULARY + " (" + COLUMN_WORD + ", " + COLUMN_MEANING + ", " + COLUMN_TOPIC + ") " +
                "VALUES ('family', 'gia đình', 'Family'), " +
                "('mother', 'mẹ', 'Family'), " +
                "('travel', 'du lịch', 'Travel'), " +
                "('sport', 'thể thao', 'Sport'), " +
                "('friend', 'bạn bè', 'Family'), " +
                "('food', 'thức ăn', 'Daily')");

        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_CORRECT_ANSWER + " TEXT, " +
                COLUMN_WRONG_ANSWER_1 + " TEXT, " +
                COLUMN_WRONG_ANSWER_2 + " TEXT, " +
                COLUMN_WRONG_ANSWER_3 + " TEXT, " +
                COLUMN_TOPIC + " TEXT)";
        db.execSQL(CREATE_QUIZ_TABLE);

        db.execSQL("INSERT INTO " + TABLE_QUIZ + " (" + COLUMN_QUESTION + ", " + COLUMN_CORRECT_ANSWER + ", " +
                COLUMN_WRONG_ANSWER_1 + ", " + COLUMN_WRONG_ANSWER_2 + ", " + COLUMN_WRONG_ANSWER_3 + ", " + COLUMN_TOPIC + ") " +
                "VALUES ('“Gia đình” trong tiếng Anh là gì?', 'family', 'friend', 'food', 'travel', 'Family'), " +
                "('“Mẹ” trong tiếng Anh là gì?', 'mother', 'father', 'sister', 'brother', 'Family'), " +
                "('“Du lịch” trong tiếng Anh là gì?', 'travel', 'sport', 'family', 'friend', 'Travel'), " +
                "('“Thể thao” trong tiếng Anh là gì?', 'sport', 'travel', 'food', 'mother', 'Sport')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOCABULARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
    }

    public List<Vocabulary> getAllVocabulary() {
        List<Vocabulary> vocabularyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VOCABULARY, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD));
                String meaning = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEANING));
                String topic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
                vocabularyList.add(new Vocabulary(id, word, meaning, topic));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return vocabularyList;
    }

    public List<QuizQuestion> getAllQuizQuestions() {
        List<QuizQuestion> quizList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUIZ, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String question = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION));
                String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER));
                String wrongAnswer1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRONG_ANSWER_1));
                String wrongAnswer2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRONG_ANSWER_2));
                String wrongAnswer3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRONG_ANSWER_3));
                String topic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
                quizList.add(new QuizQuestion(id, question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, topic));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return quizList;
    }

    public List<String> getAllTopics() {
        List<String> topics = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_TOPIC + " FROM " + TABLE_VOCABULARY, null);

        if (cursor.moveToFirst()) {
            do {
                String topic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
                if (topic != null && !topics.contains(topic)) {
                    topics.add(topic);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return topics;
    }

    public List<QuizQuestion> getQuizQuestionsByTopic(String topic) {
        List<QuizQuestion> quizList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUIZ + " WHERE " + COLUMN_TOPIC + " = ?", new String[]{topic});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String question = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION));
                String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWER));
                String wrongAnswer1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRONG_ANSWER_1));
                String wrongAnswer2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRONG_ANSWER_2));
                String wrongAnswer3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WRONG_ANSWER_3));
                String questionTopic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
                quizList.add(new QuizQuestion(id, question, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, questionTopic));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return quizList;
    }
}