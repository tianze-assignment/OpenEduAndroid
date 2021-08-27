package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.adapter.EntityContentAdapter;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.databinding.FragmentEntityContentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityContentFragment extends Fragment {

    public List<Content> data;
    private FragmentEntityContentBinding binding;
    private EntityContentAdapter adapter;

    public EntityContentFragment () {}
    public EntityContentFragment (List<Content> data) {
        this.data = data;
    }

    public void updateData(List<Content> data) {
        this.data = data;
        if (adapter != null) {
            adapter.updateData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEntityContentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EntityContentAdapter(data);
        binding.recyclerView.setAdapter(adapter);
        return root;
    }
}