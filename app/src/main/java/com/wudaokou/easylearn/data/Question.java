package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question")
public class Question {
    @PrimaryKey
    public int id;

    public String qAnswer;

    public String qBody;
}
