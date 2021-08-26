package com.wudaokou.easylearn.retrofit;

import com.wudaokou.easylearn.data.EntityInfo;
import com.wudaokou.easylearn.data.SearchResult;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EduKGService {

//    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8")
    @GET("instanceList?id=54cb27c0-e910-4963-9c9a-345d7b366b0b")
    Call<JSONArray<SearchResult>> instanceList(@Query("course") String course,
                                    @Query("searchKey") String searchKey);

    @GET("infoByInstanceName?id=54cb27c0-e910-4963-9c9a-345d7b366b0b")
    Call<JSONObject<EntityInfo>> infoByInstanceName(@Query("course") String course,
                                                    @Query("name") String name);
}

