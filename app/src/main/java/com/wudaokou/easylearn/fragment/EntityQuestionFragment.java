package com.wudaokou.easylearn.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wudaokou.easylearn.AnswerActivity;
import com.wudaokou.easylearn.EntityInfoActivity;
import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.SearchResultActivity;
import com.wudaokou.easylearn.adapter.EntityQuestionAdapter;
import com.wudaokou.easylearn.adapter.SearchResultAdapter;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.data.SearchResult;
import com.wudaokou.easylearn.databinding.FragmentEntityQuestionBinding;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONArray;
import com.wudaokou.easylearn.utils.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import kotlin.jvm.internal.FunctionReference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EntityQuestionFragment extends Fragment {

    public List<Question> data;
    private FragmentEntityQuestionBinding binding;
    private EntityQuestionAdapter adapter;
//    private LoadingDialog loadingDialog;
    private String label, course;
    boolean forStarHistory;

    public EntityQuestionFragment(final String course, final String label) {
        this.course = course;
        this.label = label;
        this.forStarHistory = false;
    }

    public EntityQuestionFragment(final boolean forStarHistory) {
        this.forStarHistory = true;
        this.label = "收藏习题";
    }

    public void updateData(List<Question> data) {
        this.data = data;
        if(data.isEmpty()){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.notFoundImage.getLayoutParams();
            params.width = binding.recyclerView.getWidth() / 2;
            params.height = binding.recyclerView.getWidth() / 2;
            binding.notFoundImage.setLayoutParams(params);
            binding.notFoundLayout.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.updateData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEntityQuestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (forStarHistory) {
            getStarQuestion();
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EntityQuestionAdapter(data);
        adapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 跳转到试题详情页
                Intent intent = new Intent(requireActivity(), AnswerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("questionList", (Serializable)data);
                intent.putExtra("label", label);
                intent.putExtra("immediateAnswer", false);
                startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!forStarHistory) {
            checkDatabase();
        }
    }

    public void hideNotFoundLayout(){
//        requireActivity().findViewById(R.id.not_found_layout).setVisibility(View.GONE);
        if (binding != null)
            binding.notFoundLayout.setVisibility(View.GONE);
    }

    public void getStarQuestion() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.backendBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackendService backendService = retrofit.create(BackendService.class);
        backendService.getStarQuestion(Constant.backendToken).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(@NotNull Call<List<Question>> call,
                                   @NotNull Response<List<Question>> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    data = response.body();
                    for (Question question : data)
                        question.hasStar = true;
                    if (adapter != null) {
                        adapter.updateData(data);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Question>> call,
                                  @NotNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void checkDatabase() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Future<List<Question>> listFuture = MyDatabase.databaseWriteExecutor.submit(new Callable<List<Question>>() {
            @Override
            public List<Question> call() throws Exception {
                return MyDatabase.getDatabase(getContext()).questionDAO()
                        .loadQuestionByCourseAndLabel(course, label);
            }
        });

        try {
            List<Question> localList = listFuture.get();
            if (localList != null && localList.size() != 0) {
                data = localList;
                updateData(data);
//                loadingDialog.dismiss();
                binding.progressBar.setVisibility(View.GONE);
            } else {
                getEntityQuestion(label);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void getEntityQuestion(final String uriName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduKGService service = retrofit.create(EduKGService.class);

        Call<JSONArray<Question>> call = service.questionListByUriName(Constant.eduKGId, uriName);
        call.enqueue(new Callback<JSONArray<Question>>() {
            @Override
            public void onResponse(@NotNull Call<JSONArray<Question>> call,
                                   @NotNull Response<JSONArray<Question>> response) {
                JSONArray<Question> jsonArray = response.body();
                Log.e("retrofit", "http ok");
                if (jsonArray != null) {
                    if (jsonArray.data != null) {
                        Log.e("retrofit question", String.format("property size: %s",
                                jsonArray.data.size()));

                        // 只展示选择题
                        data = new ArrayList<>();
                        for (Question question: jsonArray.data) {
                            String ans = question.qAnswer;
                            if (ans.length() == 1) {
                                question.totalCount = 0;
                                question.wrongCount = 0;
                                question.hasStar = false;
                                question.label = label;
                                question.course = course;
                                data.add(question);
                                MyDatabase.databaseWriteExecutor.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyDatabase.getDatabase(getContext()).questionDAO()
                                                .insertQuestion(question);
                                    }
                                });
                            }
                        }
                        updateData(data);
                    }
                }
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<JSONArray<Question>> call,
                                  @NotNull Throwable t) {
                Log.e("retrofit", "http error");
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }
}