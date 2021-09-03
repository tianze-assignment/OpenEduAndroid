package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Entity(tableName = "search_result")
public class SearchResult implements Serializable {
    @NotNull
    @PrimaryKey
    public String uri;

    public String label;

    public String category;

    public boolean hasRead;

    public boolean hasStar;

    public String course;    // 搜索的学科

    public String searchKey; // 搜索的关键词

    public int id;  // 收藏后后端生成的id

    public SearchResult(final String label, final String category, final String uri) {
        this.category = category;
        this.label = label;
        this.uri = uri;
        this.hasRead = false;
        this.hasStar = false;
    }

    public SearchResult(final SearchResult result) {
        this.uri = result.uri;
        this.label = result.label;
        this.category = result.category;
        this.hasRead = result.hasRead;
        this.hasStar = result.hasStar;
        this.course = result.course;
        this.searchKey = result.searchKey;
        this.id = result.id;
    }

}
