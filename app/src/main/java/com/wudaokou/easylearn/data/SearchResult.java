package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_result")
public class SearchResult {
    @PrimaryKey
    public String uri;

    public String label;

    public String category;

    public SearchResult(final String label, final String category, final String uri) {
        this.category = category;
        this.label = label;
        this.uri = uri;
    }
}
