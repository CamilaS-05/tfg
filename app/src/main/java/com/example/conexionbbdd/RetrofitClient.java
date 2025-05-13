package com.example.conexionbbdd;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    // Cambia esta URL por la de tu servidor o túnel actual
    private static final String BASE_URL = "https://api-cxdc.onrender.com";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
