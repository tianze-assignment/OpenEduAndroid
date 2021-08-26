package com.wudaokou.easylearn.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.data.SearchResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.VH>{
    @NonNull
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        SearchResult searchResult = data.get(position);
        holder.label.setText(searchResult.label);
        holder.category.setText(searchResult.category);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //item 点击事件
                Log.e("SearchResultAdapter", "Click item");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView category;
        public final TextView label;
        public VH(View v) {
            super(v);
            category = (TextView) v.findViewById(R.id.category);
            label = (TextView) v.findViewById(R.id.label);
        }
    }

    private List<SearchResult> data;
    public SearchResultAdapter(List<SearchResult> data) {
        this.data = data;
    }
}
