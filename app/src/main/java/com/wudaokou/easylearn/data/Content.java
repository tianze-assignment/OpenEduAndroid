package com.wudaokou.easylearn.data;

import androidx.room.Entity;

@Entity(tableName = "content")
public class Content {
    public String predicate;

    public String predicate_label;

    public String object_label;

    public String object;

    public String subject_label;

    public String subject;
}
