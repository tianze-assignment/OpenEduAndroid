package com.wudaokou.easylearn.data;

import android.widget.ListView;

import androidx.room.Entity;

import java.util.List;

@Entity(tableName = "content")
public class Content {
    public String predicate;

    public String predicate_label;

    public String object_label;

    public String object;

    public String subject_label;

    public String subject;

    public boolean hasRead;

    public List<EntityFeature> entityFeatureList;
}
