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
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.SearchableActivity;
import com.wudaokou.easylearn.SubjectManageActivity;
import com.wudaokou.easylearn.bean.SubjectChannelBean;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.constant.SubjectMap;
import com.wudaokou.easylearn.constant.SubjectMapChineseToEnglish;
import com.wudaokou.easylearn.databinding.FragmentHomeBinding;
import com.wudaokou.easylearn.fragment.HomePagerFragment;
import com.wudaokou.easylearn.utils.ListDataSave;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private final int subjectManageRequestCode = 789;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final String[] keys = Constant.subjectList;

        // 监听按钮事件，启动添加学科tab的activity
        binding.imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubjectManageActivity.class);
                intent.putExtra("tabPosition", binding.pager.getCurrentItem());
                startActivityForResult(intent, subjectManageRequestCode);
            }
        });

        binding.searchLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchableActivity.class);
                startActivity(intent);
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

        Vector<String> courseList = new Vector<>();
        ListDataSave listDataSave = new ListDataSave(requireContext(), "channel");
        List<SubjectChannelBean> subjectChannelBeanList = listDataSave
                .getDataList("myChannel", SubjectChannelBean.class);
        for (SubjectChannelBean subjectChannelBean : subjectChannelBeanList) {
            courseList.add(subjectChannelBean.tid);
        }

        binding.pager.setAdapter(new FragmentStateAdapter(getChildFragmentManager(),
                getLifecycle()) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                String key = courseList.get(position);
                return new HomePagerFragment(key);
            }

            @Override
            public int getItemCount() {
                return courseList.size();
            }
        });

        HashMap<String, String> map = SubjectMap.getMap();
        new TabLayoutMediator(binding.tabs, binding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(requireContext());
                tabView.setGravity(Gravity.CENTER);
                tabView.setText(map.get(courseList.get(position)));
                tab.setCustomView(tabView);
            }
        }).attach();
    }
}