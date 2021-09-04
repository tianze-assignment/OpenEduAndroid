package com.wudaokou.easylearn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.SearchRecord;
import com.wudaokou.easylearn.databinding.ActivityMainBinding;
import com.wudaokou.easylearn.retrofit.BackendObject;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.EduLoginRet;
import com.wudaokou.easylearn.retrofit.LoginParam;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateEduKGID();

//        去除activity的工具栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_train,
                R.id.navigation_user).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        updateSearchHistory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 设置activity的toolbar样式
//        getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 发现、训练两页面的返回按钮onClick
    public void backToHomePage(View view) {
        Navigation.findNavController(view).navigate(R.id.navigation_home);
    }

    // 实体链接onClick
    public void showEntitySearch(View view) {
        Intent intent = new Intent(this, EntityLinkSearchActivity.class);
        startActivity(intent);
    }

    // 知识问答
    public void showQA(View view) {
        Intent intent = new Intent(this, QuAnActivity.class);
        startActivity(intent);
    }

    // 更新和服务器通信使用的id
    public void updateEduKGID() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGLoginUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduKGService service = retrofit.create(EduKGService.class);
        service.eduLogin(Constant.eduKGPhone, Constant.eduKGPassword)
                .enqueue(new Callback<EduLoginRet>() {
            @Override
            public void onResponse(@NotNull Call<EduLoginRet> call,
                                   @NotNull Response<EduLoginRet> response) {
                if (response.body() != null) {
                    Constant.eduKGId = response.body().id;
                    Log.e("main_activity", "update edukgId ok");
                }
            }

            @Override
            public void onFailure(@NotNull Call<EduLoginRet> call,
                                  @NotNull Throwable t) {
                Log.e("main_activity", "update edukgId error");
            }
        });
    }

    // 向后端请求搜索历史记录
    public void updateSearchHistory() {
        // 清除历史记录
        MyDatabase.databaseWriteExecutor.submit(new Runnable() {
            @Override
            public void run() {
                MyDatabase.getDatabase(MainActivity.this).searchRecordDAO().deleteAllRecord();
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
                                MyDatabase.getDatabase(MainActivity.this).searchRecordDAO()
                                        .insertRecord(new SearchRecord(timestamp.getTime(),
                                                backendObject.name, backendObject.course.toLowerCase()));
                            }
                        });
                    }
                    Log.e("main_activity", "update search record ok");
                } else {
                    Log.e("main_activity", "update null search record");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<BackendObject>> call, @NotNull Throwable t) {
                Log.e("main_activity", "update search record error");
            }
        });
    }

    // 试题推荐onClick
    public void showQuestionRecommend(View view) {
        Intent intent = new Intent(this, QuestionRecommendActivity.class);
        startActivity(intent);
    }
}