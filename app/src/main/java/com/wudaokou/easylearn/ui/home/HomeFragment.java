package com.wudaokou.easylearn.ui.home;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.SearchableActivity;
import com.wudaokou.easylearn.SubjectManageActivity;
import com.wudaokou.easylearn.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private final int subjectManageRequestCode = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        // 显示定制的toolbar
        NavController navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = binding.toolbar;
        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        // 使activity回调fragment的onCreateOptionsMenu函数
        setHasOptionsMenu(true);
//        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);

        // 监听按钮事件，启动添加学科tab的activity
        binding.imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubjectManageActivity.class);
                startActivityForResult(intent, subjectManageRequestCode);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /**
     * 接收从该fragment启动activity的返回结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case subjectManageRequestCode:
                // 获取选择展示的学科目录
                break;
            default:
                break;
        }
    }

    // 加载toolbar的菜单项
    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        menu.clear(); // 清除activity工具栏内容
        inflater.inflate(R.menu.home_toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryRefinementEnabled(true);
        mSearchView.setQueryHint("搜索知识点");

        // 去掉搜索框默认的下划线
//        mSearchView.findViewById(R.id.search_plate).setBackground(null);
//        mSearchView.findViewById(R.id.submit_area).setBackground(null);

        // 设置搜索框背景样式
        mSearchView.setBackground(ContextCompat.getDrawable(
                requireActivity(), R.drawable.search_view_background));


        // 搜索内容自动提示
        // 待完成Adapter
//        mSearchView.setSuggestionsAdapter();


        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
//        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(
//                new ComponentName("com.wudaokou.easylearn", "SearchableActivity")));

        // 搜索框打开监听
//        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(requireActivity(),"open",Toast.LENGTH_SHORT).show();
//            }
//        });

        // 搜索框关闭监听
//        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                Toast.makeText(requireActivity(),"close",Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        // 输入文本变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 提交文本时调用
//                Snackbar.make(mSearchView.findViewById(R.id.search_go_btn),query,Snackbar.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), SearchableActivity.class);
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra("QUERY", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 文本搜索框发生变化时调用
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

        }
        return super.onOptionsItemSelected(item);
    }
}