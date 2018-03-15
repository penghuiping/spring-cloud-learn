package com.joinsoft.api.kong;

import okhttp3.*;
import org.junit.Test;

/**
 * Created by penghuiping on 2018/3/13.
 */
public class AdminCustomerApiTest {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XFORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://192.168.99.101:30120/consumers";


    /**
     * 添加一个customer
     * @throws Exception
     */
    @Test
    public void addCustomer() throws Exception {
        String url = baseUrl+"/";
        RequestBody body = new FormBody.Builder()
                .add("username", "xiaoping")
                .add("custom_id", "1231231231231").build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }


    @Test
    public void getCustomer() throws Exception {
        String url = baseUrl+"/xiaoping";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getCustomerList() throws Exception {
        String url = baseUrl+"/";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void updateCustomer() throws Exception {
        String url = baseUrl+"/xiaoping";
        RequestBody body = new FormBody.Builder()
                .add("username", "xiaoping")
                .add("custom_id", "123123123")
                .build();
        Request request = new Request.Builder().url(url).patch(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 更新某个反向代理api列表
     * @throws Exception
     */
    @Test
    public void deleteCustomer() throws Exception {
        String url = baseUrl+"/xiaoping";
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }



}
