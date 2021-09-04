package com.wudaokou.easylearn.retrofit;

import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.data.Question;
import com.wudaokou.easylearn.data.SearchResult;
import com.wudaokou.easylearn.retrofit.entityLink.JsonEntityLink;
import com.wudaokou.easylearn.retrofit.userObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EduKGService {

//    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8")
    @GET("instanceList?id=54cb27c0-e910-4963-9c9a-345d7b366b0b")
    Call<JSONArray<SearchResult>> instanceList(@Query("course") String course,
                                    @Query("searchKey") String searchKey);

    @GET("infoByInstanceName?id=54cb27c0-e910-4963-9c9a-345d7b366b0b")
    Call<JSONObject<EntityInfo>> infoByInstanceName(@Query("course") String course,
                                                    @Query("name") String name);

    @GET("questionListByUriName?id=54cb27c0-e910-4963-9c9a-345d7b366b0b")
    Call<JSONArray<Question>> questionListByUriName(@Query("uriName") String uriName);

    @FormUrlEncoded
    @POST("linkInstance")
    Call<JSONObject<JsonEntityLink>> linkInstance(@Field("id") String id,
                                                  @Field("course") String course,
                                                  @Field("context") String text);
    @POST("login")
    Call<JSONObject<userObject>> userlogin(@Field("username") String username,
                                           @Field("password") String password);

    @POST("register")
    Call<JSONObject<userObject>> userregister(@Field("username") String username,
                                              @Field("password") String password);
}

