package com.wudaokou.easylearn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.SearchResultActivity;
import com.wudaokou.easylearn.data.SearchRecord;

import java.util.List;

public class SearchRecordAdapter extends BaseAdapter {

    public List<SearchRecord> searchRecords;
    public int resourceId;
    private LayoutInflater inflater;

    public SearchRecordAdapter(@NonNull LayoutInflater inflater, int resource,
                               List<SearchRecord> searchRecords) {
//        super(context, resource);
        this.searchRecords = searchRecords;
        this.resourceId = resource;
        this.inflater = inflater;
//        Log.e("SearchRecordAdapter", "init");
//        for (SearchRecord record : searchRecords) {
//            Log.e("SearchableActivity", String.format("subject: %s, content: %s",
//                    record.subject, record.content));
//        }
    }

    @Nullable
    @Override
    public SearchRecord getItem(int position) {
        return searchRecords.get(position);
    }

    @Override
    public int getCount() {
        return searchRecords.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup container) {

        View view = inflater.inflate(resourceId, null);
        TextView textView = view.findViewById(R.id.textView);
        ImageView imageView = view.findViewById(R.id.subject_avatar);
        SearchRecord searchRecord = searchRecords.get(position);

        if (searchRecord != null) {
            textView.setText(searchRecord.content);
            int imageRes;
            switch (searchRecord.subject) {
                case "chinese":
                    imageRes = R.drawable.chinese;
                    break;
                case "math":
                    imageRes = R.drawable.math;
                    break;
                case "english":
                    imageRes = R.drawable.english;
                    break;
                case "physics":
                    imageRes = R.drawable.physics;
                    break;
                case "chemistry":
                    imageRes = R.drawable.chemistry;
                    break;
                case "biology":
                    imageRes = R.drawable.biology;
                    break;
                case "politics":
                    imageRes = R.drawable.politics;
                    break;
                case "history":
                    imageRes = R.drawable.history;
                    break;
                case "geo":
                    imageRes = R.drawable.geography;
                    break;
                default:
                    imageRes = R.drawable.error;
                    break;
            }
            imageView.setImageResource(imageRes);
//            Log.e("SearchRecordAdapter",
//                    String.format("%s | %s", searchRecord.subject, searchRecord.content));
//            ((ListView)container).setOnClickListener(this);
        } else {
            Log.e("SearchRecordAdapter", "null view");
        }
        return view;
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        SearchRecord record = searchRecords.get(position);
//        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
//        intent.putExtra("queryType", queryType);
//        intent.putExtra("queryContent", queryContent);
//        startActivity(intent);
//    }
}
