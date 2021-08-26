package com.wudaokou.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wudaokou.easylearn.adapter.SearchResultAdapter;
import com.wudaokou.easylearn.constant.SubjectMap;
import com.wudaokou.easylearn.data.SearchResult;
import com.wudaokou.easylearn.data.User;
import com.wudaokou.easylearn.databinding.ActivitySearchResultBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    public ActivitySearchResultBinding binding;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();

        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        map = SubjectMap.getMap();
        binding.subjectButton.setText(map.get(intent.getStringExtra("queryType")));
        binding.searchLine.setText(intent.getStringExtra("queryContent"));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<SearchResult> list = new ArrayList<>();
        for (int i = 0; i != 15; i++) {
            list.add(new SearchResult("label", "category", "uri"));
        }
        binding.recyclerView.setAdapter(new SearchResultAdapter(list));
    }

    public void getBack(View v) {
        SearchResultActivity.this.finish();
    }
}