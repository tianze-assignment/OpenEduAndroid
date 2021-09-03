package com.wudaokou.easylearn.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wudaokou.easylearn.EntityInfoActivity;
import com.wudaokou.easylearn.adapter.HomeCourseItemAdapter;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.constant.SubjectKeyWords;
import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.data.HomeCourseItem;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.Property;
import com.wudaokou.easylearn.data.SearchResult;
import com.wudaokou.easylearn.databinding.FragmentHomePagerBinding;
import com.wudaokou.easylearn.retrofit.BackendObject;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.HistoryParam;
import com.wudaokou.easylearn.retrofit.JSONArray;
import com.wudaokou.easylearn.retrofit.JSONObject;
import com.wudaokou.easylearn.utils.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePagerFragment extends Fragment {

    FragmentHomePagerBinding binding;
    List<HomeCourseItem> homeCourseItemList;
    String course;
    String [] keyWordList;
    EduKGService service;
    BackendService backendService;
    int resultThreadCount, propertyThreadCount;
    HomeCourseItemAdapter adapter;
    LoadingDialog loadingDialog;

    public HomePagerFragment (String course) {
        this.course = course;
    }

    public static HomePagerFragment newInstance(String course) {
        return new HomePagerFragment(course);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        keyWordList = SubjectKeyWords.getMap().get(course);
//        Log.e("home", String.format("keyWordList size: %d", keyWordList.length));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(EduKGService.class);

        Retrofit backendRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.backendBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        backendService = backendRetrofit.create(BackendService.class);
        homeCourseItemList = new ArrayList<>();

        binding = FragmentHomePagerBinding.inflate(inflater, container, false);
        binding.pagerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeCourseItemAdapter(homeCourseItemList);
        adapter.setOnItemClickListener(new HomeCourseItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HomeCourseItem homeCourseItem = homeCourseItemList.get(position);
                backendService.postClickEntity(Constant.backendToken, new HistoryParam(course.toUpperCase(),
                        homeCourseItem.result.label, homeCourseItem.result.uri)).enqueue(new Callback<BackendObject>() {
                    @Override
                    public void onResponse(@NotNull Call<BackendObject> call,
                                           @NotNull Response<BackendObject> response) {
                        if (response.code() == 200) {
                            Log.e("home", "post click ok");
                        } else {
                            Log.e("home", "post click fail");
                            Log.e("home", String.format("code: %d", response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<BackendObject> call,
                                          @NotNull Throwable t) {
                        Log.e("home", "post click error");
                    }
                });

                Intent intent = new Intent(getActivity(), EntityInfoActivity.class);
                intent.putExtra("course", course);
                intent.putExtra("label", homeCourseItem.result.label);
                intent.putExtra("uri", homeCourseItem.result.uri);
                intent.putExtra("searchResult", homeCourseItem.result);
                startActivity(intent);
            }
        });
        binding.pagerRecyclerView.setAdapter(adapter);

        loadingDialog = new LoadingDialog(requireContext());
        loadingDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getCourseData();
            }
        }).start();
        return binding.getRoot();
    }

    public void getCourseData() {
        int randomCount = 4;  // 随机选4个关键词
        List<String> selectedKeyWordList = new ArrayList<>();
        int chosen = 0;
        Random random = new Random();
        while (chosen != randomCount) {
            int idx = random.nextInt() % keyWordList.length;
            while (idx < 0) {
                idx = random.nextInt() % keyWordList.length;
            }
            String keyWord = keyWordList[idx];
            if (!selectedKeyWordList.contains(keyWord)) {
                selectedKeyWordList.add(keyWord);
                chosen++;
            }
        }

        List<SearchResult> searchResultList = new ArrayList<>();
        resultThreadCount = randomCount;

        Log.e("home", String.format("resultThreadCount： %d", resultThreadCount));

        for (String label : selectedKeyWordList) {

            Future<List<SearchResult>> featureList = MyDatabase.databaseWriteExecutor.submit(new Callable<List<SearchResult>>() {
                @Override
                public List<SearchResult> call() throws Exception {
                    return MyDatabase.getDatabase(getContext()).searchResultDAO()
                            .loadSearchResultByCourseAndLabel(course, label);
                }
            });
            try {
                List<SearchResult> resultList = featureList.get();
                if (resultList != null && resultList.size() != 0) {
                    searchResultList.addAll(resultList);
                    resultThreadCount--;
                    Log.e("home", String.format("keyWord[%s] success", label));
                    Log.e("home", String.format("resultThreadCount： %d", resultThreadCount));
                    continue;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // 数据库查不到该关键词，则向服务器请求数据
            service.instanceList(Constant.eduKGId, course, label).enqueue(
                    new Callback<JSONArray<SearchResult>>() {
                @Override
                public void onResponse(@NotNull Call<JSONArray<SearchResult>> call,
                                       @NotNull Response<JSONArray<SearchResult>> response) {
                    resultThreadCount--;
                    Log.e("home", String.format("keyWord[%s] success", label));
                    Log.e("home", String.format("resultThreadCount： %d", resultThreadCount));
                    if (response.body() != null && response.body().data != null) {
                        List<SearchResult> dataList = response.body().data;

                        for (SearchResult searchResult : dataList) {
                            searchResult.searchKey = label;
                            searchResult.course = course;
                            searchResult.hasRead = false;
                            searchResult.hasStar = false;

                            MyDatabase.databaseWriteExecutor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    MyDatabase.getDatabase(getContext()).searchResultDAO()
                                            .insertSearchResult(searchResult);
                                }
                            });
                        }

                        searchResultList.addAll(dataList);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<JSONArray<SearchResult>> call,
                                      @NotNull Throwable t) {
                    resultThreadCount--;
                    Log.e("home", String.format("keyWord[%s] fail", label));
                    Log.e("home", String.format("resultThreadCount： %d", resultThreadCount));
                }
            });
        }

        // 上述四个关键词的查询都结束后开始查询每个搜索结果的property
        while (true) {

            //!! log不能注释掉
            Log.i("home", "home page waiting");

            if (resultThreadCount == 0) {

                // 暂时只搜索label， 不搜索详情

//                Log.e("home", "finish get search result");
//                propertyThreadCount = searchResultList.size();
//                for (SearchResult searchResult : searchResultList) {
//                    Future<List<Property>> listFuture = MyDatabase.databaseWriteExecutor
//                            .submit(new Callable<List<Property>>() {
//                        @Override
//                        public List<Property> call() throws Exception {
//                            return MyDatabase.getDatabase(getContext()).propertyDAO()
//                                    .loadPropertyByParentUri(searchResult.uri);
//                        }
//                    });
//                    try {
//                        List<Property> propertyList = listFuture.get();
//                        if (propertyList != null && propertyList.size() != 0) {
//                            homeCourseItemList.add(new HomeCourseItem(searchResult, propertyList));
//                            propertyThreadCount--;
//                            continue;
//                        }
//                    } catch (InterruptedException | ExecutionException e) {
//                        e.printStackTrace();
//                    }
//
//                    // 向服务器请求数据
//                    service.infoByInstanceName(Constant.eduKGId, course, searchResult.label).enqueue(new Callback<JSONObject<EntityInfo>>() {
//                        @Override
//                        public void onResponse(@NotNull Call<JSONObject<EntityInfo>> call,
//                                               @NotNull Response<JSONObject<EntityInfo>> response) {
//                            if (response.body() != null && response.body().data != null
//                            && response.body().data.property != null) {
//                                List<Property> propertyList = response.body().data.property;
//                                for (Property property : propertyList) {
//                                    property.course = course;
//                                    property.label = searchResult.label;
//                                    property.parentUri = searchResult.uri;
//                                    property.hasRead = false;
//                                    property.hasStar = false;
//
//                                    MyDatabase.databaseWriteExecutor.submit(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            MyDatabase.getDatabase(getContext()).propertyDAO()
//                                                    .insertProperty(property);
//                                        }
//                                    });
//                                }
//                                homeCourseItemList.add(new HomeCourseItem(searchResult, propertyList));
//                                propertyThreadCount--;
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(@NotNull Call<JSONObject<EntityInfo>> call,
//                                              @NotNull Throwable t) {
//                            propertyThreadCount--;
//                        }
//                    });
//                }
//
//                while (true) {
//                    if (propertyThreadCount == 0) {
//                        adapter.updateData(homeCourseItemList);
//                        Log.e("home", "finish get property");
//                        requireActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                            }
//                        });
//                        loadingDialog.dismiss();
//                        break;  // 跳出获取property的循环
//                    }
//                }
                Log.e("home", "enter break");
                Log.e("home", String.format("list size: %d", searchResultList.size()));
                for (int id = 0; id != searchResultList.size(); id++) {
                    SearchResult searchResult = searchResultList.get(id);
                    homeCourseItemList.add(new HomeCourseItem(searchResult, null));
                }
                // 使用迭代器时，在home页连续切换tab会闪退

//                for (SearchResult searchResult : searchResultList) {
//                    homeCourseItemList.add(new HomeCourseItem(searchResult, null));
//                }
                Collections.shuffle(homeCourseItemList); // 打乱数据
                adapter.updateData(homeCourseItemList);
                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                loadingDialog.dismiss();
                break;  // 跳出获取 SearchResult 的循环
            }
        }
    }
}