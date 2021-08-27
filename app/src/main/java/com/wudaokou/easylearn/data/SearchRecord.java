package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_record")
public class SearchRecord {
    @PrimaryKey
    public int timestamp;

    public String content;

    public String subject;

    public SearchRecord(final int timestamp, final String content,
                         final String subject) {
        this.timestamp = timestamp;
        this.content = content;
        this.subject = subject;
    }
}
