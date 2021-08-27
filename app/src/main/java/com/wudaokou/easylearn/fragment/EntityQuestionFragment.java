package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.adapter.EntityQuestionAdapter;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.databinding.FragmentEntityQuestionBinding;

import java.util.List;


public class EntityQuestionFragment extends Fragment {

    public List<Question> data;
    private FragmentEntityQuestionBinding binding;
    private EntityQuestionAdapter adapter;

    public EntityQuestionFragment() {}
    public EntityQuestionFragment(List<Question> data) {
        this.data = data;
    }

    public void updateData(List<Question> data) {
        this.data = data;
        if (adapter != null) {
            adapter.updateData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEntityQuestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EntityQuestionAdapter(data);
        binding.recyclerView.setAdapter(adapter);
        return root;
    }
}