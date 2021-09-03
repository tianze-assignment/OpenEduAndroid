package com.wudaokou.easylearn.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BackendService {
    @POST("/api/history/search")
    public Call<List<BackendObject>> postHistorySearch(@Header ("Authorization") String backendToken,
                                                           @Body HistoryParam param);

    @GET("/api/history/search")
    public Call<List<BackendObject>> getHistorySearch(@Header ("Authorization") String backendToken);

    @DELETE("/api/history/search")
    public Call<BackendObject> deleteAllHistorySearch(@Header ("Authorization") String backendToken);

    @POST("/api/history/star")
    public Call<BackendObject> starEntity(@Header ("Authorization") String backendToken,
                                          @Body HistoryParam param);

    @DELETE("/api/history/{id}")
    public Call<BackendObject> cancelStarEntity(@Header ("Authorization") String backendToken,
                                                @Path("id") int id);

    @POST("/api/history/info")
    public Call<BackendObject> postClickEntity(@Header ("Authorization") String backendToken,
                                               @Body HistoryParam param);
}
