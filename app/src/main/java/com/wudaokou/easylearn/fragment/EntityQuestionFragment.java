package com.wudaokou.easylearn.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wudaokou.easylearn.AnswerActivity;
import com.wudaokou.easylearn.EntityInfoActivity;
import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.SearchResultActivity;
import com.wudaokou.easylearn.adapter.EntityQuestionAdapter;
import com.wudaokou.easylearn.adapter.SearchResultAdapter;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.data.SearchResult;
import com.wudaokou.easylearn.databinding.FragmentEntityQuestionBinding;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONArray;
import com.wudaokou.easylearn.utils.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EntityQuestionFragment extends Fragment {

    public List<Question> data;
    private FragmentEntityQuestionBinding binding;
    private EntityQuestionAdapter adapter;
    private LoadingDialog loadingDialog;
    private String label;

    public EntityQuestionFragment(final String label) {
        this.label = label;
    }

    public void updateData(List<Question> data) {
        this.data = data;
        if (adapter != null) {
            adapter.updateData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEntityQuestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loadingDialog = new LoadingDialog(requireContext());
        loadingDialog.show();
        getEntityQuestion(label);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EntityQuestionAdapter(data);
        adapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 跳转到实体详情页
                Intent intent = new Intent(requireActivity(), AnswerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("questionList", (Serializable)data);
                startActivity(intent);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        return root;
    }


    void getEntityQuestion(final String uriName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduKGService service = retrofit.create(EduKGService.class);

        Call<JSONArray<Question>> call = service.questionListByUriName(uriName);
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
                                data.add(question);
                            }
                        }
                        updateData(data);
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<JSONArray<Question>> call,
                                  @NotNull Throwable t) {
                Log.e("retrofit", "http error");
                loadingDialog.dismiss();
            }
        });
    }
}