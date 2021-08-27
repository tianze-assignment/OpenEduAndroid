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

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayoutMediator;
import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.databinding.ActivityEntityInfoBinding;

import org.jetbrains.annotations.NotNull;

public class EntityInfoActivity extends AppCompatActivity {

    private ActivityEntityInfoBinding binding;
    public EntityInfo entityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEntityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // todo 获取实体信息
        Intent intent = getIntent();

        ViewPager2 viewPager = binding.viewPager;
        viewPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        // 实体property
                    case 1:
                        // 实体关联
                    case 2:
                        // 实体相关习题列表
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

        new TabLayoutMediator(binding.tabs, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
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
}