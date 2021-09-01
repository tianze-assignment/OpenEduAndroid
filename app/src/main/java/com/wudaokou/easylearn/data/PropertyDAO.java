package com.wudaokou.easylearn.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PropertyDAO {
    @Insert
    void insertProperty(Property property);

    @Query("DELETE FROM property")
    public void deleteAllProperty();

    @Delete
    public void deleteProperty(Property property);

    @Query("SELECT * FROM property")
    public List<Property> loadAllProperty();
}
