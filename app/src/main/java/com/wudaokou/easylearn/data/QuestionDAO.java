package com.wudaokou.easylearn.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDAO {
    @Insert
    void insertQuestion(Question question);

    @Query("DELETE FROM question")
    public void deleteAllQuestion();

    @Delete
    public void deleteQuestion(Question question);

    @Query("SELECT * FROM question")
    public List<Question> loadAllQuestion();
}
