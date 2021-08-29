package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "question")
public class Question implements Serializable {
    @PrimaryKey
    public int id;

    public String qAnswer;

    public String qBody;
}
