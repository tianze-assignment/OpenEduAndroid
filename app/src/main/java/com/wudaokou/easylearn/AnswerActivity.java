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
import android.widget.Toast;

import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.MyDatabase;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.databinding.ActivityAnswerBinding;
import com.wudaokou.easylearn.fragment.ChoiceQuestionFragment;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.retrofit.EduKGService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AnswerActivity extends AppCompatActivity implements ChoiceQuestionFragment.MyListener {

    private ActivityAnswerBinding binding;
    private List<Question> questionList;
    private List<Integer> questionAnswerList; // -1 for unselected, 0~3 for A ~ B

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 获取试题信息
        Intent intent = getIntent();
        questionList = (List<Question>) intent.getSerializableExtra("questionList");
        questionAnswerList = new ArrayList<>();
        if (questionList != null) {
            for (Question question : questionList){
                questionAnswerList.add(-1);
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
                return new ChoiceQuestionFragment(questionList.get(position),
                        questionAnswerList.get(position));
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
        binding.workProgress.setText(String.format(Locale.CHINA, "%d/%d", position + 1, questionList.size()));
        binding.questionViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Question question = questionList.get(position);
                binding.workProgress.setText(String.format(Locale.CHINA, "%d/%d",
                        binding.questionViewPager2.getCurrentItem() + 1, questionList.size()));
                if (question.hasStar) {
                    binding.starQuestionButton.setImageResource(R.drawable.star_fill);
                } else {
                    binding.starQuestionButton.setImageResource(R.drawable.star_blank);
                }
            }
        });
        String label2 = label.length() < 10 ? label : label.substring(0, 10);
        binding.entityTitle.setText(String.format("%s相关习题", label2));
    }

    public void goBack(View view) {
        this.finish();
    }

    public void onStarQuestionClick(View view) {
        int position = binding.questionViewPager2.getCurrentItem();
        Question question = questionList.get(position);
        question.hasStar = !question.hasStar;

        if (question.hasStar) {
            binding.starQuestionButton.setImageResource(R.drawable.star_fill);
        } else {
            binding.starQuestionButton.setImageResource(R.drawable.star_blank);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.backendBaseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackendService backendService = retrofit.create(BackendService.class);
        backendService.onStarQuestion(Constant.backendToken, question.hasStar, question.id,
                question.qAnswer, question.qAnswer,
                question.label, question.course.toUpperCase()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call,
                                   @NotNull Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(AnswerActivity.this, "收藏题目成功!", Toast.LENGTH_LONG).show();
                    Log.e("question", "star ok");
                } else {
                    Toast.makeText(AnswerActivity.this, "收藏题目失败!", Toast.LENGTH_LONG).show();
                    Log.e("question", "star fail");
                }
            }

            @Override
            public void onFailure(@NotNull Call<String> call,
                                  @NotNull Throwable t) {
                Toast.makeText(AnswerActivity.this, "收藏题目失败!", Toast.LENGTH_LONG).show();
                Log.e("question", "star error");
                Log.e("question", t.toString());
            }
        });

        MyDatabase.databaseWriteExecutor.submit(new Runnable() {
            @Override
            public void run() {
                MyDatabase.getDatabase(AnswerActivity.this).questionDAO()
                        .updateQuestion(question);
            }
        });
    }

    @Override
    public void sendValue(int option) {
        // 修改选中的选项值
        int position = binding.questionViewPager2.getCurrentItem();
        questionAnswerList.set(position, option);
    }
}