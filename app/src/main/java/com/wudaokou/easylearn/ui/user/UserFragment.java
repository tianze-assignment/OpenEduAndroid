package com.wudaokou.easylearn.ui.user;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wudaokou.easylearn.BrowsingHistoryActivity;
import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.StarHistoryActivity;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.databinding.FragmentUserBinding;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.ui.login.LoginActivity;
import org.jetbrains.annotations.NotNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserFragment extends Fragment implements View.OnClickListener{

    private UserViewModel userViewModel;
    private FragmentUserBinding binding;
    BackendService backendService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.backendBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        backendService = retrofit.create(BackendService.class);


        //text组件设置
        //button设置
        final Button login = binding.loginButton;
        final Button pick = binding.pickButton;
        final Button history = binding.historyButton;
        //button行为监听
        login.setOnClickListener(this);
        pick.setOnClickListener(this);
        history.setOnClickListener(this);
        //module行为监听
        userViewModel.getmText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //添加用户id查询申请
            }
        });
        userViewModel.getpText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //添加密码确认申请
            }
        });
        userViewModel.getsucc().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean s) {
                //添加登录成功确认申请
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    //监听反馈函数
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_button:
                onClickButton_his(v);
                break;
            case R.id.login_button:
                onClickButton_log(v);
                break;
            case R.id.pick_button:
                onClickButton_pik(v);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    private void onClickButton_log(View view) {
        //登录逻辑
        String type = "login";
        //进行http请求
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
    private void onClickButton_pik(View view) {
        Intent intent = new Intent(getActivity(), StarHistoryActivity.class);
        startActivity(intent);
    }

    private void onClickButton_his(View view) {
        Intent intent = new Intent(getActivity(), BrowsingHistoryActivity.class);
        startActivity(intent);
    }
    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}