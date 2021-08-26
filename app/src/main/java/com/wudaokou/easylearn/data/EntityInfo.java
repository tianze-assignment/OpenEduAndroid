package com.wudaokou.easylearn.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "entity_info")
public class EntityInfo {
    @PrimaryKey
    public String label;

    @Relation(
            parentColumn = "userId",
            entityColumn = "predicateLabel"
    )
    public List<Property> propertyList;

    @Relation(
            parentColumn = "userId",
            entityColumn = "predicate_label"
    )
    public List<Content> contentList;
}

