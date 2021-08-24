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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.wudaokou.easylearn.adapter.SearchRecordAdapter;
import com.wudaokou.easylearn.constant.SubjectMap;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.SearchRecord;
import com.wudaokou.easylearn.databinding.ActivitySearchableBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SearchableActivity extends AppCompatActivity
            implements PopupMenu.OnMenuItemClickListener{

    public ActivitySearchableBinding binding;
    public SearchView searchView;
    public ListView listView;
    public MyDatabase myDatabase;
    SharedPreferences sharedPreferences;
    public Button typeButton;
    public Button clearRecordButton;

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
        HashMap<String, String> map = SubjectMap.getMap();
        typeButton.setText(map.get(selectedType));

        initSearchView();

        // 先获取数据库
        myDatabase = MyDatabase.getDatabase(this);

        // 再获取历史搜索记录
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
        switch (item.getItemId()) {
            case R.id.type_chinese:
                typeButton.setText("语文");
                editor.putString("searchType", "chinese");
                break;
            case R.id.type_math:
                typeButton.setText("数学");
                editor.putString("searchType", "math");
                break;
            case R.id.type_english:
                typeButton.setText("英语");
                editor.putString("searchType", "english");
                break;
            case R.id.type_physics:
                typeButton.setText("物理");
                editor.putString("searchType", "physics");
                break;
            case R.id.type_chemistry:
                typeButton.setText("化学");
                editor.putString("searchType", "chemistry");
                break;
            case R.id.type_biology:
                typeButton.setText("生物");
                editor.putString("searchType", "biology");
                break;
            case R.id.type_history:
                typeButton.setText("历史");
                editor.putString("searchType", "history");
                break;
            case R.id.type_geography:
                typeButton.setText("地理");
                editor.putString("searchType", "geography");
                break;
            case R.id.type_politics:
                typeButton.setText("政治");
                editor.putString("searchType", "politics");
                break;
            default:
                break;
        }
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

                // todo 执行搜搜

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
            Log.e("SearchableActivity", "after setAdapter");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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

    private void doMySearch(String query) {
        // 执行搜索操作
    }
}