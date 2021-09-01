package com.wudaokou.easylearn.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContentDAO {
    @Insert
    void insertContent(Content content);

    @Query("DELETE FROM content")
    public void deleteAllContent();

    @Delete
    public void deleteContent(Content content);

    @Query("SELECT * FROM content")
    public List<Content> loadAllContent();
}
