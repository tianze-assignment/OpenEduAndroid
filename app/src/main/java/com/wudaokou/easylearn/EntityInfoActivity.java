package com.wudaokou.easylearn;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;
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

public class EntityInfoActivity extends AppCompatActivity implements WbShareCallback {

    private ActivityEntityInfoBinding binding;
    private EntityPropertyFragment entityPropertyFragment;
    private EntityContentFragment entityContentFragment;
    private EntityQuestionFragment entityQuestionFragment;

    String label;
    IWBAPI mWBAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWeiboSdk();

        binding = ActivityEntityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 获取实体信息
        Intent intent = getIntent();
        String course = intent.getStringExtra("course");
        label = intent.getStringExtra("label");
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
                            entityQuestionFragment = new EntityQuestionFragment(course, label);
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

    // 微博sdk初始化
    private void initWeiboSdk() {
        AuthInfo authInfo = new AuthInfo(this, "949341693", "", "");
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);

    }

    // 微博分享回调
    @Override
    public void onComplete() {
        Toast.makeText(EntityInfoActivity.this, "分享成功",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onError(UiError error) {
        Toast.makeText(EntityInfoActivity.this, "分享失败:" + error.errorMessage,
                Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCancel() {
        Toast.makeText(EntityInfoActivity.this, "分享取消",
                Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mWBAPI != null) {
            mWBAPI.doResultIntent(data, this);
        }
    }

    public void shareToWeibo(View view) {
        WeiboMultiMessage message = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = shareText(entityPropertyFragment.data);
        message.textObject = textObject;
        mWBAPI.shareMessage(message, false);
    }

    String shareText(List<Property> data){
        StringBuilder sb = new StringBuilder();
        sb.append(label).append("：\n\n");
        for(Property p : data)
            sb.append(p.toString());
        return sb.toString();
    }
}