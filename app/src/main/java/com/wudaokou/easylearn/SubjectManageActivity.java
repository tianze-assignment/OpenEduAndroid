package com.wudaokou.easylearn;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.wudaokou.easylearn.data.DataStore;

public class SubjectManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_manage);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.subject_manage, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            // 使用数据存储区存储学科设置信息
//            DataStore dataStore = new DataStore(getContext(), "default1");

            // 为整个层次结构启用自定义数据存储区
//            PreferenceManager preferenceManager = getPreferenceManager();
//            preferenceManager.setPreferenceDataStore(dataStore);

//            final String[] keys = {"chineseChosen", "mathChosen", "englishChosen",
//                    "physicsChosen", "chemistryChosen", "biologyChosen", "historyChosen",
//                    "geographyChosen", "politicsChosen"};
//            for (String key : keys) {
//                Preference preference = findPreference(key);
//                if (preference != null) {
//                    preference.setPreferenceDataStore(dataStore);
//                } else {
//                    Log.w("SubjectManageActivity", String.format("%s is null", key));
//                }
//            }
        }
    }
}