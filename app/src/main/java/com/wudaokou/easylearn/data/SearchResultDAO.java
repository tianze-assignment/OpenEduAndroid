package com.wudaokou.easylearn.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SearchResultDAO {
    @Insert
    void insertSearchResult(SearchResult searchResult);

    @Query("DELETE FROM search_result")
    public void deleteAllSearchResult();

    @Delete
    public void deleteSearchResult(SearchResult searchResult);

    @Query("SELECT * FROM search_result")
    public List<SearchResult> loadAllSearchResult();
}
