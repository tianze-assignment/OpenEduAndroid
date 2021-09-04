package com.wudaokou.easylearn.ui.user;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wudaokou.easylearn.BrowsingHistoryActivity;
import com.wudaokou.easylearn.LoginActivity;
import com.wudaokou.easylearn.StarHistoryActivity;
import com.wudaokou.easylearn.databinding.FragmentUserBinding;

import org.jetbrains.annotations.NotNull;

public class UserFragment extends Fragment{

    private UserViewModel userViewModel;
    private FragmentUserBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        arrangeLayout();

        binding.loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });

        binding.logoffButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", "-1");
            editor.apply();
            arrangeLayout();
        });

        binding.starButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StarHistoryActivity.class);
            startActivity(intent);
        });

        binding.historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), BrowsingHistoryActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void arrangeLayout() {
        String token = sharedPreferences.getString("token", "-1");
        if (token.equals("-1")) {
            String text = "请登录";
            binding.nameText.setText(text);
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.logoffButton.setVisibility(View.GONE);
        } else {
            String text = "欢迎  " + sharedPreferences.getString("username", "");
            binding.nameText.setText(text);
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.logoffButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        arrangeLayout();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}