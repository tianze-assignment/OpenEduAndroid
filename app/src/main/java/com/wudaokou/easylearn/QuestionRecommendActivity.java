package com.wudaokou.easylearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.constant.SubjectMapChineseToEnglish;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.utils.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionRecommendActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    TextInputLayout chooseSubjectLayout;
    AutoCompleteTextView chooseSubject;
    Slider chooseNumber;
    TextInputLayout numberTextLayout;
    TextInputEditText numberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_recommend);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        chooseSubjectLayout = findViewById(R.id.chooseSubjectLayout);
        chooseSubject = findViewById(R.id.chooseSubject);
        chooseNumber = findViewById(R.id.chooseNumber);
        numberTextLayout = findViewById(R.id.numberTextLayout);
        numberText = findViewById(R.id.numberText);

        // 选择学科
        String[] subjects = getResources().getStringArray(R.array.subjects);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_subject_item, subjects);
        chooseSubject.setAdapter(arrayAdapter);

        chooseSubject.setOnItemClickListener((parent, view, position, id) -> chooseSubjectLayout.setError(null));
        chooseNumber.addOnChangeListener((slider, value, fromUser) -> numberText.setText( String.valueOf( (int) value ) ));

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException ignored) {}
            String token = sharedPreferences.getString("token", "-1");
            if(token.equals("-1")){
                runOnUiThread(() -> {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    QuestionRecommendActivity.this.finish();
                });
            }
        }).start();

    }

    public void getBack(View view) {
        QuestionRecommendActivity.this.finish();
    }

    public void startRecommend(View view) {

        String subject = chooseSubject.getText().toString();
        if(subject.equals("")){
            chooseSubjectLayout.setError("请选择学科");
            return;
        }
        subject = SubjectMapChineseToEnglish.getMap().get(subject);
        assert subject != null;
        subject = subject.toUpperCase();
        int number = (int)chooseNumber.getValue();

        getQuestions(subject, number);

    }

    void getQuestions(String course, int number){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.backendBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackendService service = retrofit.create(BackendService.class);

        Call<List<Question>> call = service.getRecommendQuestions(Constant.backendToken, course, number, Constant.eduKGId);

        LoadingDialog loadingDialog = new LoadingDialog(this, call);
        loadingDialog.show();

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(@NotNull Call<List<Question>> call, @NotNull Response<List<Question>> response) {
                List<Question> rsp = response.body();
                if(rsp == null){
                    Toast.makeText(QuestionRecommendActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(QuestionRecommendActivity.this, AnswerActivity.class);
                intent.putExtra("questionList", (Serializable) rsp);
                intent.putExtra("label", "推荐");
                intent.putExtra("immediateAnswer", sharedPreferences.getBoolean("setting_test_recommend_instant", true));
                loadingDialog.dismiss();
                startActivity(intent);
            }

            @Override
            public void onFailure(@NotNull Call<List<Question>> call, @NotNull Throwable t) {
                if(call.isCanceled()) return;
                Toast.makeText(QuestionRecommendActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}