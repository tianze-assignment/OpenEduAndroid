package com.wudaokou.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wudaokou.easylearn.constant.SubjectMap;
import com.wudaokou.easylearn.databinding.ActivitySearchResultBinding;

import java.util.HashMap;

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
    }

    public void getBack(View v) {
        SearchResultActivity.this.finish();
    }
}