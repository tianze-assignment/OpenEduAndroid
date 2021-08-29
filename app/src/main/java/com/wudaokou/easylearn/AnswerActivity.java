package com.wudaokou.easylearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
        String label = intent.getStringExtra("label");

        // 为viewpager2设置adapter
        binding.questionViewPager2.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(),
                getLifecycle()) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                // assert: questionList != null
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
        binding.questionViewPager2.setCurrentItem(position, true);
        // 设置初始位置
        binding.workProgress.setText(String.format("%d/%d", position + 1, questionList.size()));
        binding.questionViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.workProgress.setText(String.format("%d/%d",
                        binding.questionViewPager2.getCurrentItem() + 1, questionList.size()));
            }
        });
        String label2 = label.length() < 10 ? label : label.substring(0, 10);
        binding.entityTitle.setText(String.format("%s相关习题", label2));
    }

    public void goBack(View view) {
        this.finish();
    }
}