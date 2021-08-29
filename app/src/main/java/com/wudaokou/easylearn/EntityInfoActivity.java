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

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayoutMediator;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.data.Property;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.databinding.ActivityEntityInfoBinding;
import com.wudaokou.easylearn.fragment.EntityContentFragment;
import com.wudaokou.easylearn.fragment.EntityPropertyFragment;
import com.wudaokou.easylearn.fragment.EntityQuestionFragment;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONArray;
import com.wudaokou.easylearn.retrofit.JSONObject;
import com.wudaokou.easylearn.utils.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntityInfoActivity extends AppCompatActivity {

    private ActivityEntityInfoBinding binding;
    private EntityPropertyFragment entityPropertyFragment;
    private EntityContentFragment entityContentFragment;
    private EntityQuestionFragment entityQuestionFragment;

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

        binding.viewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        // 实体属性
                        if (entityPropertyFragment == null) {
                            entityPropertyFragment = new EntityPropertyFragment(course, label);
                        }
                        return entityPropertyFragment;
                    case 1:
                        // 实体关联
                        if (entityContentFragment == null) {
                            entityContentFragment = new EntityContentFragment(course, label);
                        }
                        return entityContentFragment;
                    default:
                        // 实体相关习题列表
                        if (entityQuestionFragment == null) {
                            entityQuestionFragment = new EntityQuestionFragment(label);
                        }
                        return entityQuestionFragment;
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
                String[] titles = {"知识属性", "知识关联", "相关习题"} ;
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

    public void goBack(View view) {
        EntityInfoActivity.this.finish();
    }
}