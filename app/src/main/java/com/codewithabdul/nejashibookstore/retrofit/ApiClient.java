package com.codewithabdul.nejashibookstore.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://offshoretech.in/najeshi_bookstore/apis/";

    private static Retrofit retrofit = null;

    public ApiClient() {

    }

    public static Retrofit getClient() {

        final OkHttpClient okHttpClient = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

}
