package com.joinsoft.api;

import okhttp3.*;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by penghuiping on 2017/6/22.
 */
public class ApiTest {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XFORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://localhost:20001/api/";

    private static final String proxyBaseUrl = "http://192.168.99.100:30971/kongtest/api/";


    @Test
    public void SSOLogin() throws Exception {
        String url = proxyBaseUrl+"/common/SSOLogin.do";
        RequestBody body = new FormBody.Builder()
                .add("mobile", "18812345678").add("password", "e10adc3949ba59abbe56e057f20f883e")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    public void logout() throws Exception {
        String url = proxyBaseUrl+"secure/common/SSOLogout.do";
        RequestBody body = new FormBody.Builder()
                .add("token", "18812345678").add("refreshToken", "e10adc3949ba59abbe56e057f20f883e")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }




    @Test
    public void getToken() throws Exception {
        String url = baseUrl+"getToken.do";
        RequestBody body = new FormBody.Builder()
                .add("refreshToken", "40d0d2a99048718b3fa3a85633fa0e04")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
