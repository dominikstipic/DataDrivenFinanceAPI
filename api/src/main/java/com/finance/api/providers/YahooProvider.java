package com.finance.api.providers;


import netscape.javascript.JSObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Map;

public interface YahooProvider {
    @GET("/")
    Call<Map<String, Map<String, String>>> getData(@Query("ticker") String ticker,
                           @Query("start_date") String startDate,
                           @Query("end_date") String endDate);
}
