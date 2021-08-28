package com.wudaokou.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class EntityLinkSearchActivity extends AppCompatActivity {

    AutoCompleteTextView chooseSubject;
    TextInputEditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_link_search);

        searchText = findViewById(R.id.text);
        chooseSubject = findViewById(R.id.chooseSubject);

        // 选择学科
        String[] subjects = getResources().getStringArray(R.array.subjects);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_subject_item, subjects);
        chooseSubject.setAdapter(arrayAdapter);

    }

    public void getBack(View view) {
        EntityLinkSearchActivity.this.finish();
    }


    public void search(View view) {
        String subject = chooseSubject.getText().toString();
        if(subject.equals("")){
            Toast.makeText(this, "请选择学科", Toast.LENGTH_SHORT).show();
            return;
        }
        String text = String.valueOf(searchText.getText());
        if(text.equals("")) {
            Toast.makeText(this, "请填写搜索文本", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, EntityLinkResultActivity.class);
        intent.putExtra("subject", subject);
        intent.putExtra("text", text);
        startActivity(intent);
    }

    public void clearText(View view) {
        searchText.setText("");
    }
}