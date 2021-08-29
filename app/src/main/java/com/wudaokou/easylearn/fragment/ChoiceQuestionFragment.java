package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.databinding.FragmentChoiceQuestionBinding;
import com.wudaokou.easylearn.utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

public class ChoiceQuestionFragment extends Fragment {

    String qAnswer;
    String qBody;
    String[] choices;
    FragmentChoiceQuestionBinding binding;
    List<RadioButton> radioButtonList;

    public ChoiceQuestionFragment(Question question) {
        // Required empty public constructor
        qAnswer = question.qAnswer;
        qBody = question.qBody;
        String ch = ".";
        int aPos = qBody.lastIndexOf("A" + ch);
        if (aPos == -1) {
            ch = "、";
            aPos = qBody.lastIndexOf("A" + ch);
        }
        if (aPos == -1) {
            ch = "．";
            aPos = qBody.lastIndexOf("A" + ch);
        }
        int bPos = qBody.lastIndexOf("B" + ch);
        int cPos = qBody.lastIndexOf("C" + ch);
        int dPos = qBody.lastIndexOf("D" + ch);
        choices = new String[4];
        choices[0] = qBody.substring(aPos, bPos);
        choices[1] = qBody.substring(bPos, cPos);
        choices[2] = qBody.substring(cPos, dPos);
        choices[3] = qBody.substring(dPos);
        qBody = qBody.substring(0, aPos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChoiceQuestionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.choiceQuestionBody.setText(qBody);
        binding.choiceQuestionAnswer.setText(qAnswer);

        radioButtonList = new ArrayList<>();

        for (String choice : choices) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(choice);
            radioButton.setTextSize(20);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton radioButton1 = (RadioButton) v;
                    String text = (String) radioButton1.getText();

                    if (text.substring(0, 1).equals(qAnswer)) {
//                        radioButton1.setButtonDrawable(R.drawable.correct_answer);
                        radioButton1.setTextColor(getResources().getColor(R.color.green_A700));
                    } else {
//                        radioButton1.setButtonDrawable(R.drawable.false_answer);
                        radioButton1.setTextColor(getResources().getColor(R.color.red_900));
                    }
                    binding.answerLayout.setVisibility(View.VISIBLE);
                    for (RadioButton button : radioButtonList) {
                        button.setEnabled(false);
                    }
                }
            });
            binding.radioGroup.addView(radioButton);
            radioButtonList.add(radioButton);
        }
        return root;
    }
}