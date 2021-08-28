package com.wudaokou.easylearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.databinding.ActivityAnswerBinding;
import com.wudaokou.easylearn.fragment.ChoiceQuestionFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AnswerActivity extends AppCompatActivity {

    private ActivityAnswerBinding binding;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 获取试题信息
        Intent intent = getIntent();
        questionList = (List<Question>) intent.getSerializableExtra("questionList");
        if (questionList != null) {
            for (Question question : questionList){
                if (question.qBody != null) {
                    Log.e("serializable body", question.qBody);
                } else {
                    Log.e("serializable body", "null body");
                }
                if (question.qAnswer != null) {
                    Log.e("serializable answer", question.qAnswer);
                } else {
                    Log.e("serializable body", "null answer");
                }
            }
            Log.e("serializable size", String.valueOf(questionList.size()));
        }
        int position = intent.getIntExtra("position", 0);

        // 为viewpager2设置adapter
        binding.questionViewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),
                getLifecycle()) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                return new ChoiceQuestionFragment(questionList.get(position));
            }

            @Override
            public int getItemCount() {
                if (questionList != null) {
                    return questionList.size();
                }
                return 0;
            }
        });
    }
}