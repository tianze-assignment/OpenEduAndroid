package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "property")
public class Property {
    @PrimaryKey
    public String predicate;

    public String predicateLabel;

    public String object;  // uri

    public String objectLabel;
}
