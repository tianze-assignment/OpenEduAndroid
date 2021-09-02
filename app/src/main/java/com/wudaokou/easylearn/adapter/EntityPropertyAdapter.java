package com.wudaokou.easylearn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.data.Property;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityPropertyAdapter extends RecyclerView.Adapter<EntityPropertyAdapter.VH> {

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView object;
        public final TextView predicateLabel;
        public VH(View v) {
            super(v);
            object = (TextView) v.findViewById(R.id.object);
            predicateLabel = (TextView) v.findViewById(R.id.entityPropertyLabel);
        }
    }

    @NonNull
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_property_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EntityPropertyAdapter.VH holder, int position) {
        Property property = data.get(position);
        if (property.object.contains("http")) {
            if (property.objectLabel == null) {
                holder.object.setText("暂无");
            } else {
                holder.object.setText(property.objectLabel);
            }
        } else {
            holder.object.setText(property.object);
        }
        holder.predicateLabel.setText(property.predicateLabel);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    private List<Property> data;
    public EntityPropertyAdapter(List<Property> data) {
        this.data = data;
    }
    public void updateData(List<Property> data) {
        this.data = data;
    }
}
