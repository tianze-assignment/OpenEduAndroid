package com.wudaokou.easylearn.ui.home;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.SearchableActivity;
import com.wudaokou.easylearn.SubjectManageActivity;
import com.wudaokou.easylearn.databinding.FragmentHomeBinding;
import com.wudaokou.easylearn.fragment.HomePagerFragment;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Vector;

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

        intiSearchView();

        // 显示定制的toolbar
//        NavController navController = Navigation.findNavController(requireActivity(),
//                R.id.nav_host_fragment_activity_main);
//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        Toolbar toolbar = binding.toolbar;
//        NavigationUI.setupWithNavController(
//                toolbar, navController, appBarConfiguration);

        // 使activity回调fragment的onCreateOptionsMenu函数
//        setHasOptionsMenu(true);
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

    @Override
    public void onResume() {
        super.onResume();

        final String[] keys = {"chineseChosen", "mathChosen", "englishChosen",
                    "physicsChosen", "chemistryChosen", "biologyChosen", "historyChosen",
                    "geographyChosen", "politicsChosen"};
        HashMap<String, String> map = new HashMap<>();
        map.put("chineseChosen", "语文");
        map.put("mathChosen", "数学");
        map.put("englishChosen", "英语");
        map.put("physicsChosen", "物理");
        map.put("chemistryChosen", "化学");
        map.put("biologyChosen", "生物");
        map.put("historyChosen", "历史");
        map.put("geographyChosen", "地理");
        map.put("politicsChosen", "政治");

        final Vector<String> subject = new Vector<>();

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext());
        for (String key : keys) {
            boolean value = sharedPreferences.getBoolean(key, false);
            if (value) {
                subject.add(key);
                Log.w("HomeFragment",String.format("[%s] has been selected!", key));
            }
        }

        binding.pager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(),
                getLifecycle()) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                String key = subject.get(position);
                return HomePagerFragment.newInstance(map.get(key));
            }

            @Override
            public int getItemCount() {
                return subject.size();
            }
        });

        new TabLayoutMediator(binding.tabs, binding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(requireContext());
                tabView.setGravity(Gravity.CENTER);
                tabView.setText(map.get(subject.get(position)));
                tab.setCustomView(tabView);
            }
        }).attach();
        Log.w("HomeFragment","tabLayout has been initiated");
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

    public void intiSearchView() {
        SearchView mSearchView = binding.searchView;
        mSearchView.setIconifiedByDefault(false);
//        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryRefinementEnabled(true);
        mSearchView.setQueryHint("搜索知识点");

        // 去掉搜索框默认的下划线
//        mSearchView.findViewById(R.id.search_plate).setBackground(null);
//        mSearchView.findViewById(R.id.submit_area).setBackground(null);

        // 设置搜索框背景样式
//        mSearchView.setBackground(ContextCompat.getDrawable(
//                requireActivity(), R.drawable.search_view_background));


        // 搜索内容自动提示
        // 待完成Adapter
//        mSearchView.setSuggestionsAdapter();


        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
//        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));

//         搜索框打开监听
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SearchableActivity.class);
//                intent.setAction(Intent.ACTION_SEARCH);
//                startActivity(intent);
//                Toast.makeText(requireActivity(),"open",Toast.LENGTH_SHORT).show();
            }
        });

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
}