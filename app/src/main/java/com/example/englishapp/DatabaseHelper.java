package com.example.englishapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.englishapp.vocabulary.QuizQuestion;
import com.example.englishapp.vocabulary.Vocabulary;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EnglishApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_VOCABULARY = "vocabulary";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_TRANSCRIPTION = "transcription";
    private static final String COLUMN_MEANING = "meaning";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_TOPIC = "topic";

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
                COLUMN_TRANSCRIPTION + " TEXT, " +
                COLUMN_MEANING + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_TOPIC + " TEXT)";
        db.execSQL(CREATE_VOCABULARY_TABLE);

        // Dữ liệu từ vựng: Thay "Daily" thành "Food"
        db.execSQL("INSERT INTO " + TABLE_VOCABULARY + " (" + COLUMN_WORD + ", " + COLUMN_TRANSCRIPTION + ", " + COLUMN_MEANING + ", " + COLUMN_IMAGE + ", " + COLUMN_TOPIC + ") " +
                "VALUES " +
                "('family', '/ˈfæmɪli/', 'gia đình', 'family_image', 'Family'), " +
                "('travel', '/ˈtrævəl/', 'du lịch', 'travel_image', 'Travel'), " +
                "('sport', '/spɔːrt/', 'thể thao', 'sport_image', 'Sport'), " +
                "('food', '/fuːd/', 'thức ăn', 'food_image', 'Food')"); // Thay "Daily" thành "Food"

        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_CORRECT_ANSWER + " TEXT, " +
                COLUMN_WRONG_ANSWER_1 + " TEXT, " +
                COLUMN_WRONG_ANSWER_2 + " TEXT, " +
                COLUMN_WRONG_ANSWER_3 + " TEXT, " +
                COLUMN_TOPIC + " TEXT)";
        db.execSQL(CREATE_QUIZ_TABLE);

        // Dữ liệu câu hỏi: Thay "Daily" thành "Food"
        db.execSQL("INSERT INTO " + TABLE_QUIZ + " (" + COLUMN_QUESTION + ", " + COLUMN_CORRECT_ANSWER + ", " +
                COLUMN_WRONG_ANSWER_1 + ", " + COLUMN_WRONG_ANSWER_2 + ", " + COLUMN_WRONG_ANSWER_3 + ", " + COLUMN_TOPIC + ") " +
                "VALUES " +
                // Chủ đề Family
                "('“Gia đình” trong tiếng Anh là gì?', 'family', 'friend', 'food', 'travel', 'Family'), " +
                "('“Mẹ” trong tiếng Anh là gì?', 'mother', 'father', 'sister', 'brother', 'Family'), " +
                "('“Bạn bè” trong tiếng Anh là gì?', 'friend', 'family', 'teacher', 'student', 'Family'), " +
                "('Từ nào dưới đây có nghĩa là “gia đình”?', 'family', 'sport', 'travel', 'food', 'Family'), " +
                "('Từ “mother” có nghĩa là gì?', 'mẹ', 'bố', 'anh', 'chị', 'Family'), " +
                // Chủ đề Travel
                "('“Du lịch” trong tiếng Anh là gì?', 'travel', 'sport', 'family', 'friend', 'Travel'), " +
                "('Từ nào dưới đây có nghĩa là “du lịch”?', 'travel', 'food', 'sport', 'mother', 'Travel'), " +
                "('“Travel” có nghĩa là gì trong tiếng Việt?', 'du lịch', 'thể thao', 'gia đình', 'bạn bè', 'Travel'), " +
                // Chủ đề Sport
                "('“Thể thao” trong tiếng Anh là gì?', 'sport', 'travel', 'food', 'mother', 'Sport'), " +
                "('Từ “sport” có nghĩa là gì?', 'thể thao', 'du lịch', 'thức ăn', 'mẹ', 'Sport'), " +
                "('Từ nào dưới đây có nghĩa là “thể thao”?', 'sport', 'family', 'friend', 'travel', 'Sport'), " +
                // Chủ đề Food (thay từ "Daily")
                "('“Thức ăn” trong tiếng Anh là gì?', 'food', 'sport', 'travel', 'family', 'Food'), " +
                "('Từ “food” có nghĩa là gì?', 'thức ăn', 'du lịch', 'thể thao', 'gia đình', 'Food'), " +
                "('Từ nào dưới đây có nghĩa là “thức ăn”?', 'food', 'friend', 'mother', 'sport', 'Food')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOCABULARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
    }

    public List<Vocabulary> getVocabularyByTopic(String topic) {
        List<Vocabulary> vocabularyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_VOCABULARY + " WHERE " + COLUMN_TOPIC + " = ?", new String[]{topic});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD));
                String transcription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRANSCRIPTION));
                String meaning = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEANING));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
                String vocabTopic = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC));
                vocabularyList.add(new Vocabulary(id, word, transcription, meaning, image, vocabTopic));
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