package com.example.ayogeshwaran.capstone.networkquery;

import com.example.ayogeshwaran.capstone.App.PNRApp;
import com.example.ayogeshwaran.capstone.Utils;
import com.example.ayogeshwaran.capstone.model.PNRStatus;
import com.example.ayogeshwaran.capstone.service.PNRService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PNRQuery {
    private static final String BASE_URL =
            "https://api.railwayapi.com/v2/pnr-status/";

    private final Retrofit retrofit;
    private final PNRService pnrService;

    public PNRQuery() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();
        pnrService = retrofit.create(PNRService.class);
    }

    public Call<PNRStatus> getPNRResult(String pnrNo) {
        PNRApp pnrApp = new PNRApp();
        return pnrService.pnrResult(pnrNo, Utils.getPNRApiKey(pnrApp.getAppContext()));
    }

}
