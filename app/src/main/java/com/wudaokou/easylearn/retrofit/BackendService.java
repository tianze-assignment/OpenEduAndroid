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
import retrofit2.http.Path;

public interface BackendService {
    @Headers("Authorization: Bearer ieyMlHM6iYRExfQjkrdkGaTrd7bThLDv9DnbbE4gr7ydViWDz0pWguaCgZoMgNma")
    @POST("/api/history/search")
    public Call<List<BackendObject>> postHistorySearch(@Body HistoryParam param);

    @Headers("Authorization: Bearer ieyMlHM6iYRExfQjkrdkGaTrd7bThLDv9DnbbE4gr7ydViWDz0pWguaCgZoMgNma")
    @GET("/api/history/search")
    public Call<List<BackendObject>> getHistorySearch();

    @Headers("Authorization: Bearer ieyMlHM6iYRExfQjkrdkGaTrd7bThLDv9DnbbE4gr7ydViWDz0pWguaCgZoMgNma")
    @DELETE("/api/history/search")
    public Call<BackendObject> deleteAllHistorySearch();

    @Headers("Authorization: Bearer ieyMlHM6iYRExfQjkrdkGaTrd7bThLDv9DnbbE4gr7ydViWDz0pWguaCgZoMgNma")
    @POST("/api/history/star")
    public Call<BackendObject> starEntity(@Body HistoryParam param);

    @Headers("Authorization: Bearer ieyMlHM6iYRExfQjkrdkGaTrd7bThLDv9DnbbE4gr7ydViWDz0pWguaCgZoMgNma")
    @DELETE("/api/history/{id}")
    public Call<BackendObject> cancelStarEntity(@Path("id") int id);
}
