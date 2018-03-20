package com.joinsoft.api;

import com.joinsoft.userservice.dto.JwtCredentialDto;
import com.joinsoft.userservice.service.KongJwtService;
import okhttp3.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by penghuiping on 2018/3/15.
 */
public class JwtTest  {

    @Autowired
    private KongJwtService kongJwtService;

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://192.168.99.100:30120/apis/kong_api_secure/plugins";

    @Test
    public void test1() throws Exception {
        JwtCredentialDto jwtCredentialDto = new JwtCredentialDto();
        jwtCredentialDto.setKey("a36c3049b36249a3c9f8891cb127243c");
        jwtCredentialDto.setSecret("e71829c351aa4242c2719cbfbe671c09");
        String value = kongJwtService.generateJwtToken(jwtCredentialDto);
        System.out.println(value);
    }

    @Test
    public void settingApiJwt() throws Exception {
        String url = baseUrl+"";
        RequestBody body = new FormBody.Builder()
                .add("name", "jwt")
                .add("config.claims_to_verify", "exp,nbf")
                .add("config.secret_is_base64", "true").build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    public void updateApiJwt() throws Exception {
        String url = baseUrl+"/0c6c4162-3c3c-4d56-8eeb-ed5234e0bafd";
        RequestBody body = new FormBody.Builder()
                .add("name", "jwt")
                .add("config.claims_to_verify", "exp,nbf")
                .add("config.secret_is_base64", "true").build();
        Request request = new Request.Builder().url(url).patch(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    @Test
    public void getJwtList() throws Exception {
        String url = "http://192.168.99.100:30120"+"/consumers/11111111111111111111111111111111/jwt";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
