package com.wudaokou.easylearn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.data.HomeCourseItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeCourseItemAdapter
        extends RecyclerView.Adapter<HomeCourseItemAdapter.VH>{

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView entityLabel;
        public final TextView entityDescription;
        public VH(View v) {
            super(v);
            entityLabel = (TextView) v.findViewById(R.id.entityLabel);
            entityDescription = (TextView) v.findViewById(R.id.entityPropertyLabel);
        }
    }

    @NonNull
    @NotNull
    @Override
    public HomeCourseItemAdapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_course_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeCourseItemAdapter.VH holder, int position) {
        // todo
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    List<HomeCourseItem> data;
    public void updateData(List<HomeCourseItem> data) {
        this.data = data;
    }

    public HomeCourseItemAdapter(List<HomeCourseItem> data) {
        this.data = data;
    }
}
