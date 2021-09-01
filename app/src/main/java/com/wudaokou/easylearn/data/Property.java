package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "property")
public class Property {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String predicate;

    public String predicateLabel;

    public String object;  // uri

    public String objectLabel;

    public String label;  // 记录所属知识点

    public String course;  // 记录所属学科

    public boolean hasStar;

    public boolean hasRead;
}
