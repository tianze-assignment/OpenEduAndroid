package com.wudaokou.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wudaokou.easylearn.adapter.SearchRecordAdapter;
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
    public String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        map = SubjectMap.getMap();
        Intent intent = getIntent();
        course = intent.getStringExtra("queryType");
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

                    // 添加选项点击监听器
                    SearchResultAdapter adapter = new SearchResultAdapter(jsonArray.data);
                    adapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // 跳转到实体详情页
                            SearchResult searchResult = jsonArray.data.get(position);
                            Intent intent = new Intent(SearchResultActivity.this, EntityInfoActivity.class);
                            intent.putExtra("course", course);
                            intent.putExtra("label", searchResult.label);
                            startActivity(intent);
                        }
                    });

                    adapter.setOnItemLongClickListener(new SearchResultAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(View view, int position) {
                            // todo 待长按搜索结果的功能
                            Toast.makeText(getApplicationContext(), "长按选项", Toast.LENGTH_LONG).show();
                        }
                    });
                    binding.recyclerView.setAdapter(adapter);
                } else {
                    Log.e("retrofit", "error request");
                    List<SearchResult> searchResultList = new ArrayList<SearchResult>();
                    searchResultList.add(new SearchResult("暂时找不到您想要的结果", "抱歉", ""));
                    binding.recyclerView.setAdapter(new SearchResultAdapter(searchResultList));
                }
            }

            @Override
            public void onFailure(@NotNull Call<JSONArray<SearchResult>> call,
                                  @NotNull Throwable t) {
                Log.e("retrofit", "connect error");
                // todo
            }
        });
    }
}