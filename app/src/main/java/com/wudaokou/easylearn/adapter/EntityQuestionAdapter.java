package com.wudaokou.easylearn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.data.Content;
import com.wudaokou.easylearn.data.Question;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityQuestionAdapter
        extends RecyclerView.Adapter<EntityQuestionAdapter.VH>{

    @NonNull
    @NotNull
    @Override
    public VH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_question_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        Question question = data.get(position);
        // todo 将问题拆分为问题和选项
        String qBody = question.qBody;
        int splitPos = qBody.lastIndexOf("A.");
        if (splitPos == -1) {
            splitPos = qBody.lastIndexOf("A、");
        }
        String questionText;
        if (splitPos != -1) {
            questionText = qBody.substring(0, splitPos);
        } else {
            questionText = qBody;
        }

        holder.qBodyText.setText(questionText);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public static class VH extends RecyclerView.ViewHolder{
        public final TextView qBodyText;
        public VH(View v) {
            super(v);
            qBodyText = (TextView) v.findViewById(R.id.qBodyText);
        }
    }

    private List<Question> data;
    public EntityQuestionAdapter(List<Question> data) {
        this.data = data;
    }
    public void updateData(List<Question> data) {
        this.data = data;
    }
}
