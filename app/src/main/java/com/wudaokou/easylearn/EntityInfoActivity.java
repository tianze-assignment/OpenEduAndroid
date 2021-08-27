package com.wudaokou.easylearn;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayoutMediator;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.data.Property;
import com.wudaokou.easylearn.data.SearchResult;
import com.wudaokou.easylearn.databinding.ActivityEntityInfoBinding;
import com.wudaokou.easylearn.fragment.EntityContentFragment;
import com.wudaokou.easylearn.fragment.EntityProblemFragment;
import com.wudaokou.easylearn.fragment.EntityPropertyFragment;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONArray;
import com.wudaokou.easylearn.retrofit.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntityInfoActivity extends AppCompatActivity {

    private ActivityEntityInfoBinding binding;
    public EntityInfo entityInfo;
    public List<Property> propertyList;
    public List<Content> contentList;

    private EntityPropertyFragment entityPropertyFragment;
    private EntityContentFragment entityContentFragment;
    private EntityProblemFragment entityProblemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEntityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 获取实体信息
        Intent intent = getIntent();
        String course = intent.getStringExtra("course");
        String label = intent.getStringExtra("label");
        binding.title.setText(label);

        Log.e("EntityInfoActivity", String.format("title: %s", label));

        getEntityInfo(course, label);

        binding.viewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        // 实体属性
                        if (propertyList != null) {
                            Log.e("EntityInfoActivity", "param constructor");
                            entityPropertyFragment = new EntityPropertyFragment(propertyList);
                        } else {
                            Log.e("EntityInfoActivity", "empty constructor");
                            entityPropertyFragment = new EntityPropertyFragment();
                        }
                        return entityPropertyFragment;
                    case 1:
                        // 实体关联
                        if (contentList != null) {
                            entityContentFragment = new EntityContentFragment(contentList);
                        } else {
                            entityContentFragment = new EntityContentFragment();
                        }
                        return entityContentFragment;
                    default:
                        // 实体相关习题列表
                        return new EntityProblemFragment();
                }
            }
            @Override
            public int getItemCount() {
                return 3;
            }
        });

        new TabLayoutMediator(binding.tabs, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(EntityInfoActivity.this);
                tabView.setGravity(Gravity.CENTER);
                String[] titles = {"知识详解", "关联知识", "相关习题"} ;
                tabView.setText(titles[position]);
                tab.setCustomView(tabView);
            }
        }).attach();

        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void getEntityInfo(final String course, final String label) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduKGService service = retrofit.create(EduKGService.class);

        Call<JSONObject<EntityInfo>> call = service.infoByInstanceName(course, label);
        call.enqueue(new Callback<JSONObject<EntityInfo>>() {
            @Override
            public void onResponse(@NotNull Call<JSONObject<EntityInfo>> call,
                                   @NotNull Response<JSONObject<EntityInfo>> response) {
                JSONObject<EntityInfo> jsonObject = response.body();
                Log.e("retrofit", "http ok");
                if (jsonObject != null) {
                    if (jsonObject.data.property != null) {
                        Log.e("retrofit", String.format("property size: %s",
                                jsonObject.data.property.size()));
                        propertyList = jsonObject.data.property;
                        if (entityPropertyFragment != null) {
                            entityPropertyFragment.updateData(propertyList);
                            Log.e("retrofit", "update entityPropertyFragment");
                        }
                    }
                    if (jsonObject.data.content != null) {
                        Log.e("retrofit", String.format("content size: %s",
                                 jsonObject.data.content.size()));
                        contentList = jsonObject.data.content;
                        if (entityContentFragment != null) {
                            entityContentFragment.updateData(contentList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JSONObject<EntityInfo>> call,
                                  @NotNull Throwable t) {
                Log.e("retrofit", "http error");
            }
        });
    }
}