package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wudaokou.easylearn.databinding.FragmentHomePagerBinding;

import org.jetbrains.annotations.NotNull;

public class HomePagerFragment extends Fragment {

    public String ARG_OBJECT = "tab";
    FragmentHomePagerBinding binding;

    public HomePagerFragment () {

    }

    public HomePagerFragment (String subject) {
        ARG_OBJECT = subject;
    }

    public static HomePagerFragment newInstance(String subject) {
        return new HomePagerFragment(subject);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomePagerBinding.inflate(inflater, container, false);
        binding.homePagerTextView.setText(ARG_OBJECT);
        Log.w("HomePagerFragment", String.format("onCreateView setText: %s", ARG_OBJECT));
        return binding.getRoot();
    }
}