package com.wudaokou.easylearn.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BackendService {
    @Headers("Authorization: Bearer ieyMlHM6iYRExfQjkrdkGaTrd7bThLDv9DnbbE4gr7ydViWDz0pWguaCgZoMgNma")
    @POST("/api/history/search")
    public Call<List<BackendObject>> postHistorySearch(@Body HistoryParam param);

    @GET("/api/history/search")
    public Call<List<BackendObject>> getHistorySearch();

    @DELETE("/api/history/search")
    public void deleteAllHistorySearch();
}
