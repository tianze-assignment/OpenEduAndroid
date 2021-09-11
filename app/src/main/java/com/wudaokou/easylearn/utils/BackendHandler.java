package com.wudaokou.easylearn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.wudaokou.easylearn.bean.SubjectChannelBean;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.SearchRecord;
import com.wudaokou.easylearn.retrofit.BackendObject;
import com.wudaokou.easylearn.retrofit.BackendService;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendHandler {
    public static void initDatabaseFromBackend(Context context) {
        // 清除历史记录
        MyDatabase.databaseWriteExecutor.submit(new Runnable() {
            @Override
            public void run() {
                MyDatabase.getDatabase(context).searchRecordDAO().deleteAllRecord();
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.backendBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackendService backendService = retrofit.create(BackendService.class);
        backendService.getHistorySearch(Constant.backendToken)
                .enqueue(new Callback<List<BackendObject>>() {
                    @Override
            public void onResponse(@NotNull Call<List<BackendObject>> call,
                                   @NotNull Response<List<BackendObject>> response) {
                if (response.body() != null) {
                    for (BackendObject backendObject : response.body()) {
                        String timeStr = backendObject.createdAt.replace("T", " ");
                        Timestamp timestamp = Timestamp.valueOf(timeStr);
                        MyDatabase.databaseWriteExecutor.submit(new Runnable() {
                            @Override
                            public void run() {
                                MyDatabase.getDatabase(context).searchRecordDAO()
                                        .insertRecord(new SearchRecord(timestamp.getTime(),
                                                backendObject.name, backendObject.course.toLowerCase()));
                            }
                        });
                    }
                    Log.e("backend_handler", "update search record ok");
                } else {
                    Log.e("backend_handler", "update null search record");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<BackendObject>> call, @NotNull Throwable t) {
                Log.e("backend_handler", "update search record error");
            }
        });
    }

    public static void initPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString("token", "-1");
        if(!token.equals("-1"))
            Constant.backendToken = token;

        boolean isFirst = sharedPreferences.getBoolean("isFirst", true);
        if (isFirst) {
            Log.e("main", "first login");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirst", false);

            List<SubjectChannelBean> myChannelList = new ArrayList<>();
            myChannelList.add(new SubjectChannelBean("语文", "chinese"));
            myChannelList.add(new SubjectChannelBean("数学", "math"));
            myChannelList.add(new SubjectChannelBean("英语", "english"));

            List<SubjectChannelBean> moreChannelList =  new ArrayList<>();
            moreChannelList.add(new SubjectChannelBean("物理", "physics"));
            moreChannelList.add(new SubjectChannelBean("化学", "chemistry"));
            moreChannelList.add(new SubjectChannelBean("生物", "biology"));
            moreChannelList.add(new SubjectChannelBean("历史", "history"));
            moreChannelList.add(new SubjectChannelBean("地理", "geo"));
            moreChannelList.add(new SubjectChannelBean("政治", "politics"));

            ListDataSave listDataSave = new ListDataSave(context, "channel");
            listDataSave.setDataList("myChannel", myChannelList);
            listDataSave.setDataList("moreChannel", moreChannelList);

            // 设置试题批阅方式
            editor.putBoolean("setting_test_info_instant", false);
            editor.putBoolean("setting_test_test_instant", false);
            editor.putBoolean("setting_test_recommend_instant", true);
            editor.apply();
        }
    }
}
