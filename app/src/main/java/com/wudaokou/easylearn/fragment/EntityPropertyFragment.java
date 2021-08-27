package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.adapter.EntityPropertyAdapter;
import com.wudaokou.easylearn.data.Property;
import com.wudaokou.easylearn.databinding.FragmentEntityPropertyBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityPropertyFragment extends Fragment {

    public List<Property> data;
    private FragmentEntityPropertyBinding binding;
    private EntityPropertyAdapter adapter;

    public EntityPropertyFragment() {}
    public EntityPropertyFragment(List<Property> data) {
        this.data = data;
    }

    public void updateData(List<Property> data) {
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
        binding = FragmentEntityPropertyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EntityPropertyAdapter(data);
        binding.recyclerView.setAdapter(adapter);
        return root;
    }
}