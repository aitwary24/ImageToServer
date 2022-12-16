package com.socio.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient   {
    private  static final String BASE_URL = "https://dev.nojoto.com/api/beta/content.php/?cid=7ec99b415af3e88205250e3514ce0fa7";                          // Base URL
    private static ApiClient mInstance;
    private Retrofit retrofit;

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiClient getInstance() {
        if (mInstance == null) {
            mInstance = new ApiClient();
        }
        return mInstance;
    }

    public ApiInterface getAPI() {
        return retrofit.create(ApiInterface.class);
    }
}
