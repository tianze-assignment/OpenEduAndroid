package com.wudaokou.easylearn.utils;

import android.content.Context;
import android.util.Log;

import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.SearchRecord;
import com.wudaokou.easylearn.retrofit.BackendObject;
import com.wudaokou.easylearn.retrofit.BackendService;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
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
}
