package com.wudaokou.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.wudaokou.easylearn.adapter.SearchRecordAdapter;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.constant.SubjectMap;
import com.wudaokou.easylearn.constant.SubjectMapChineseToEnglish;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.SearchRecord;
import com.wudaokou.easylearn.databinding.ActivitySearchableBinding;
import com.wudaokou.easylearn.retrofit.BackendObject;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.retrofit.HistoryParam;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchableActivity extends AppCompatActivity
            implements PopupMenu.OnMenuItemClickListener{

    public ActivitySearchableBinding binding;
    public SearchView searchView;
    public ListView listView;
    public MyDatabase myDatabase;
    SharedPreferences sharedPreferences;
    public Button typeButton;
    public Button clearRecordButton;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Intent intent = getIntent();

        binding = ActivitySearchableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        typeButton = binding.subjectButton;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedType = sharedPreferences.getString("searchType", "chinese");
        map = SubjectMap.getMap();
        typeButton.setText(map.get(selectedType));

        initSearchView();

        // 先获取数据库
        myDatabase = MyDatabase.getDatabase(this);

        // 再获取历史搜索记录
//        initSearchRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSearchRecord();
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.search_type, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public void getBack(View v) {
        SearchableActivity.this.finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String course = item.toString();
        typeButton.setText(course);
        editor.putString("searchType", SubjectMapChineseToEnglish.getMap().get(course));
//        Log.e("menu", course);
//        Log.e("menu", SubjectMapChineseToEnglish.getMap().get(course));
        editor.apply();
        return false;
    }

    public void initSearchView() {
//        Log.e("SearchableActivity", "initSearchView");
//        searchView = findViewById(R.id.searchView2);
        searchView = binding.searchView2;
        searchView.setActivated(true);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("搜索知识点");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("SearchableActivity", String.format("query string: %s", query));
                String subject = sharedPreferences.getString("searchType", "chinese");
                int timestamp = (int) System.currentTimeMillis();
                SearchRecord searchRecord = new SearchRecord(timestamp, query, subject);

                // 往数据库线程池中添加任务插入搜索记录
                MyDatabase.databaseWriteExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        myDatabase.searchRecordDAO().insertRecord(searchRecord);
                    }
                });

                // 将搜索历史记录传递给后端
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constant.backendBaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                BackendService service = retrofit.create(BackendService.class);
                service.postHistorySearch(Constant.backendToken,
                        new HistoryParam(subject.toUpperCase(), query))
                        .enqueue(new Callback<List<BackendObject>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<BackendObject>> call,
                                           @NotNull Response<List<BackendObject>> response) {
                        Log.e("backend", "post search history ok");
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<BackendObject>> call,
                                          @NotNull Throwable t) {
                        Log.e("backend", "post search history error");
                    }
                });

                // todo 执行搜搜
                doMySearch(subject, query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 文本搜索框发生变化时调用
                return false;
            }
        });
    }

    private void initSearchRecord() {
        Log.e("SearchableActivity", "initSearchRecord");
        Future<List<SearchRecord>> future = MyDatabase.databaseWriteExecutor.submit(
                new Callable<List<SearchRecord>>() {
                    @Override
                    public List<SearchRecord> call() throws Exception {
                        return myDatabase.searchRecordDAO().loadLimitedRecords(10);
                    }
                }
        );
        try {
            List<SearchRecord> searchRecords = future.get();
            clearRecordButton = binding.clearRecordButton;

            if (searchRecords.size() == 0) {
                clearRecordButton.setVisibility(View.INVISIBLE);
            } else {
                clearRecordButton.setVisibility(View.VISIBLE);
            }

            Log.e("SearchableActivity", String.format("record total: %d", searchRecords.size()));

            SearchRecordAdapter searchRecordAdapter = new SearchRecordAdapter(
                    getLayoutInflater(), R.layout.search_record_item, searchRecords);
            listView = binding.recordList;
            listView.setAdapter(searchRecordAdapter);
            // 监听ListView选项被点击
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SearchRecord record = searchRecordAdapter.getItem(position);
                    if (record != null) {
                        doMySearch(record.subject, record.content);
                    }
                }
            });
            Log.e("SearchableActivity", "after setAdapter");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void clearSearchRecord(View view) {
        MyDatabase.databaseWriteExecutor.submit(new Runnable() {
            @Override
            public void run() {
                myDatabase.searchRecordDAO().deleteAllRecord();
            }
        });
        initSearchRecord();
    }

    private void doMySearch(String queryType, String queryContent) {
        // 执行搜索操作
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("queryType", queryType);
        intent.putExtra("queryContent", queryContent);
        startActivity(intent);
    }
}