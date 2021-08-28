package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.databinding.FragmentChoiceQuestionBinding;

public class ChoiceQuestionFragment extends Fragment {

    String qAnswer;
    String qBody;
    String[] choices;
    FragmentChoiceQuestionBinding binding;

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

        for (String choice : choices) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(choice);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton radioButton1 = (RadioButton) v;
                    String text = (String) radioButton1.getText();
                    if (text.substring(0, 1).equals(qAnswer)) {
                        radioButton1.setButtonDrawable(R.drawable.correct_answer);
                    } else {
                        radioButton1.setButtonDrawable(R.drawable.false_answer);
                    }
                    binding.answerLayout.setVisibility(View.VISIBLE);
                    binding.radioGroup.setEnabled(false);
                }
            });
            binding.radioGroup.addView(radioButton);
        }
        return root;
    }
}