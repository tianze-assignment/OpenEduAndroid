package com.wudaokou.easylearn.data;
import android.widget.Toast;

import com.wudaokou.easylearn.EntityLinkResultActivity;
import com.wudaokou.easylearn.adapter.EntityLinkAdapter;
import com.wudaokou.easylearn.constant.Constant;
import com.wudaokou.easylearn.data.model.LoggedInUser;
import com.wudaokou.easylearn.data.model.logException;
import com.wudaokou.easylearn.retrofit.BackendService;
import com.wudaokou.easylearn.retrofit.EduKGService;
import com.wudaokou.easylearn.retrofit.JSONObject;
import com.wudaokou.easylearn.retrofit.entityLink.EntityLinkObject;
import com.wudaokou.easylearn.retrofit.entityLink.JsonEntityLink;
import com.wudaokou.easylearn.retrofit.userObject;
import com.wudaokou.easylearn.ui.login.LoginActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.backendBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BackendService service = retrofit.create(BackendService.class);
        Call<JSONObject<userObject>> call = service.userlogin(username, password);

        try {
            // TODO: handle loggedInUser authentication
            //通信
            Response<JSONObject<userObject>> response = call.execute();
            JSONObject<userObject> rsp = response.body();
            if(rsp == null){
                //服务器错误
                throw new logException("service error");
            }
            if(rsp.getCode().equals("404")){
                throw new logException("404_no_such_user");
            }
            if(rsp.getCode().equals("406")){
                throw new logException("406_wrong_password");
            }
            /**
            call.enqueue(new Callback<JSONObject<userObject>>() {
                @Override
                public void onResponse(@NotNull Call<JSONObject<userObject>> call, @NotNull Response<JSONObject<userObject>> response) {
                    JSONObject<userObject> rsp = response.body();
                    // 返回错误，服务器错误
                    if(rsp == null){
                        //Toast.makeText(getActivity().this, "服务器错误", Toast.LENGTH_SHORT).show();
                        throw new logException("service error");
                        return;
                    }
                    //返回错误，账户不存在
                    if(rsp.getCode().equals("404")){
                        throw new logException("404_no_such_user");
                        return;
                    }
                    //返回错误，密码错误
                    if(rsp.getCode().equals("406")){
                        throw new logException("406_wrong_password");
                        return;
                    }
                }

                @Override
                public void onFailure(@NotNull Call<JSONObject<userObject>> call, @NotNull Throwable t) {
                    Toast.makeText(getActivity().this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            });**/
        } catch (logException e) {
            return new Result.Error(e);
        }
        finally {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            //这里获取返回的token，暂时还没有改动
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.Success<>(fakeUser);
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}