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

    private static final String baseUrl = "http://www.lamago.net/mijiemonitor/api/";


    @Test
    public void SSOLogin() throws Exception {
        String url = baseUrl+"SSOLogin.do";
        RequestBody body = new FormBody.Builder()
                .add("username", "test").add("password", "e10adc3949ba59abbe56e057f20f883e")
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
