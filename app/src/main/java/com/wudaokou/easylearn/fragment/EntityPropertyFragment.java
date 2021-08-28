package com.wudaokou.easylearn.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wudaokou.easylearn.R;
import com.wudaokou.easylearn.adapter.EntityPropertyAdapter;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.data.Property;
import com.wudaokou.easylearn.databinding.FragmentEntityPropertyBinding;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONObject;
import com.wudaokou.easylearn.utils.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntityPropertyFragment extends Fragment {

    public List<Property> data;
    private FragmentEntityPropertyBinding binding;
    private EntityPropertyAdapter adapter;
    private LoadingDialog loadingDialog;
    private String course;
    private String label;

    public EntityPropertyFragment(String course, String label) {
        this.course = course;
        this.label = label;
    }

    public void updateData(List<Property> data) {
        this.data = data;
        if (adapter != null) {
            adapter.updateData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEntityPropertyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        loadingDialog = new LoadingDialog(requireContext());
//        loadingDialog.show();
        getEntityInfo(course, label);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EntityPropertyAdapter(data);
        binding.recyclerView.setAdapter(adapter);
        return root;
    }

    public void getEntityInfo(final String course, final String label) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.eduKGBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EduKGService service = retrofit.create(EduKGService.class);

        Call<JSONObject<EntityInfo>> call = service.infoByInstanceName(course, label);
        call.enqueue(new Callback<JSONObject<EntityInfo>>() {
            @Override
            public void onResponse(@NotNull Call<JSONObject<EntityInfo>> call,
                                   @NotNull Response<JSONObject<EntityInfo>> response) {
                JSONObject<EntityInfo> jsonObject = response.body();
                Log.e("retrofit", "http ok");
                if (jsonObject != null) {
                    if (jsonObject.data.property != null) {
                        Log.e("retrofit", String.format("property size: %s",
                                jsonObject.data.property.size()));
                        data = jsonObject.data.property;
                        updateData(data);
                    }
                }
//                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<JSONObject<EntityInfo>> call,
                                  @NotNull Throwable t) {
                Log.e("retrofit", "http error");
                loadingDialog.dismiss();
            }
        });
    }
}