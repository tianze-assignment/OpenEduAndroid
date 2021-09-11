package com.wudaokou.easylearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

public class SettingActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    RadioGroup testInfoGroup;
    RadioGroup testTestGroup;
    RadioGroup testRecommendGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        testInfoGroup = findViewById(R.id.setting_test_info_group);
        testTestGroup = findViewById(R.id.setting_test_test_group);
        testRecommendGroup = findViewById(R.id.setting_test_recommend_group);
        findViewById(R.id.exploreBackButton).setOnClickListener(v -> finish());

        testInfoGroup.check(sharedPreferences.getBoolean("setting_test_info_instant", true) ? R.id.setting_test_info_instant : R.id.setting_test_info_final);
        testTestGroup.check(sharedPreferences.getBoolean("setting_test_test_instant", true) ? R.id.setting_test_test_instant : R.id.setting_test_test_final);
        testRecommendGroup.check(sharedPreferences.getBoolean("setting_test_recommend_instant", true) ? R.id.setting_test_recommend_instant : R.id.setting_test_recommend_final);

        testInfoGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("setting_test_info_instant", checkedId == R.id.setting_test_info_instant);
            editor.apply();
        });
        testTestGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("setting_test_test_instant", checkedId == R.id.setting_test_test_instant);
            editor.apply();
        });
        testRecommendGroup.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("setting_test_recommend_instant", checkedId == R.id.setting_test_recommend_instant);
            editor.apply();
        });
    }
}