package com.wudaokou.easylearn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.data.Content;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityContentAdapter
        extends RecyclerView.Adapter<EntityContentAdapter.VH>{

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView subOrObjectLabel;
        public final TextView predicateLabel;
        public final ImageView relationImageView;
        public VH(View v) {
            super(v);
            subOrObjectLabel = (TextView) v.findViewById(R.id.subOrObjectLabel);
            predicateLabel = (TextView) v.findViewById(R.id.predicateLabel);
            relationImageView = (ImageView) v.findViewById(R.id.relationImageView);
        }
    }

    @NonNull
    @NotNull
    @Override
    public EntityContentAdapter.VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_content_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EntityContentAdapter.VH holder, int position) {
        Content content = data.get(position);
        holder.predicateLabel.setText(content.predicate_label);
        if (content.object_label != null) {
            holder.subOrObjectLabel.setText(content.object_label);
        } else if (content.subject_label != null) {
            holder.subOrObjectLabel.setText(content.subject_label);
            holder.relationImageView.setImageResource(R.drawable.arrow_subject);
        }

        // todo 设置监听器
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    private List<Content> data;
    public EntityContentAdapter(List<Content> data) {
        this.data = data;
    }
    public void updateData(List<Content> data) {
        this.data = data;
    }
}
