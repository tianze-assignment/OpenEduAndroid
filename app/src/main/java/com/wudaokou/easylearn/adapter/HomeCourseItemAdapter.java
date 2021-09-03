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

import java.util.ArrayList;
import java.util.List;

public class HomeCourseItemAdapter
        extends RecyclerView.Adapter<HomeCourseItemAdapter.VH>{

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView entityLabel;
        public final TextView entityDescription;
        public final TextView entityKeyWord;
        public VH(View v) {
            super(v);
            entityLabel = (TextView) v.findViewById(R.id.entityLabel);
            entityDescription = (TextView) v.findViewById(R.id.entityDescriptionLabel);
            entityKeyWord = (TextView) v.findViewById(R.id.entityKeyWord);
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
        if (data != null) {
            HomeCourseItem homeCourseItem = data.get(position);
            if (homeCourseItem.result != null) {
                holder.entityLabel.setText(homeCourseItem.result.label);
                holder.entityKeyWord.setText(String.format("关键词: %s", homeCourseItem.result.searchKey));
                holder.entityDescription.setText(String.format("分类: %s", homeCourseItem.result.category));
            } else {
                holder.entityLabel.setText("Label");
                holder.entityDescription.setText("Category");
                holder.entityKeyWord.setText("KeyWord");
            }

            if (homeCourseItem.result.hasRead) {
                holder.entityKeyWord.setTextColor(holder.entityKeyWord.getContext()
                        .getResources().getColor(R.color.grey_500));
                holder.entityDescription.setTextColor(holder.entityDescription.getContext()
                        .getResources().getColor(R.color.grey_500));
            } else {
                holder.entityKeyWord.setTextColor(holder.entityKeyWord.getContext()
                        .getResources().getColor(R.color.grey_700));
                holder.entityDescription.setTextColor(holder.entityDescription.getContext()
                        .getResources().getColor(R.color.grey_700));
            }

        } else {
            holder.entityLabel.setText("Label");
            holder.entityDescription.setText("Description");
        }
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        return 1;
    }

    List<HomeCourseItem> data;
    public void updateData(List<HomeCourseItem> data) {
        this.data = data;
    }

    public HomeCourseItemAdapter(List<HomeCourseItem> data) {
        this.data = data;
    }

    public HomeCourseItemAdapter() {
//        this.data = new ArrayList<>();
//        data.add();
    }
}
