package com.wudaokou.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wudaokou.easylearn.adapter.SearchResultAdapter;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.constant.SubjectMap;
import com.wudaokou.easylearn.data.SearchResult;
import com.wudaokou.easylearn.databinding.ActivitySearchResultBinding;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONArray;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        String course = intent.getStringExtra("queryType");
        binding.subjectButton.setText(map.get(course));
        String searchKey = intent.getStringExtra("queryContent");
        binding.searchLine.setText(searchKey);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        doSearch(course, searchKey);
    }

    public void getBack(View v) {
        SearchResultActivity.this.finish();
    }

    public void doSearch(final String course, final String searchKey) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduKGService service = retrofit.create(EduKGService.class);

        Call<JSONArray<SearchResult>> call = service.instanceList(course, searchKey);


        call.enqueue(new Callback<JSONArray<SearchResult>>() {
            @Override
            public void onResponse(@NotNull Call<JSONArray<SearchResult>> call,
                                   @NotNull Response<JSONArray<SearchResult>> response) {
                Log.e("retrofit", "success");
                JSONArray<SearchResult> jsonArray = response.body();

                if (jsonArray != null && jsonArray.code.equals("0")) {
                    Log.e("retrofit", String.format("data size: %d", jsonArray.data.size()));
                    for (SearchResult searchResult : jsonArray.data) {
                        Log.e("retrofit", String.format("category: %s, label: %s, uri: %s",
                                searchResult.category, searchResult.label, searchResult.uri));
                    }
                    binding.recyclerView.setAdapter(new SearchResultAdapter(jsonArray.data));
                } else {
                    Log.e("retrofit", "error request");
                    List<SearchResult> searchResultList = new ArrayList<SearchResult>();
                    searchResultList.add(new SearchResult("暂无结果", "啊哦", ""));
                    binding.recyclerView.setAdapter(new SearchResultAdapter(searchResultList));
                }
            }

            @Override
            public void onFailure(@NotNull Call<JSONArray<SearchResult>> call,
                                  @NotNull Throwable t) {
                Log.e("retrofit", "connect error");
                // todo
            }

            public void onError() {
                // todo
            }
        });
    }
}