package com.example.ayogeshwaran.capstone.service;

import com.example.ayogeshwaran.capstone.model.PNRStatus;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PNRService {
    @GET("/v2/pnr-status//pnr/{pnrNO}/apikey/{apiKey}")
    Call<PNRStatus> pnrResult(@Path("pnrNO") String pnrNo, @Path("apiKey") String apiKey);
}
