package com.joinsoft.api.kong;

import okhttp3.*;
import org.junit.Test;

/**
 * Created by penghuiping on 2018/3/13.
 */
public class AdminProxyApiTest {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XFORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://192.168.99.100:30120/";

    private static final String virtualHost = "php.test.com";

    /**
     * kong节点信息
     * @throws Exception
     */
    @Test
    public void getKongInfo() throws Exception {
        String url = baseUrl+"";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * kong节点状态
     * @throws Exception
     */
    @Test
    public void getKongStatus() throws Exception {
        String url = baseUrl+"status";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 添加一个反向代理api
     *
     * 然后可以通过 http://kong-proxy:port/kongtest/ 访问到对应的地址
     * @throws Exception
     */
    @Test
    public void addKongApi() throws Exception {
        String url = baseUrl+"apis/";
        RequestBody body = new FormBody.Builder()
                .add("name", "kong_api_secure")
                .add("upstream_url", "http://"+virtualHost+"/api/secure")
                .add("uris", "/kongtest/api/secure")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 获取反向代理api详情
     * @throws Exception
     */
    @Test
    public void getKongApi() throws Exception {
        String url = baseUrl+"apis/kongtest";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 获取所有反向代理api列表
     * @throws Exception
     */
    @Test
    public void getKongApiList() throws Exception {
        String url = baseUrl+"apis/";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 更新某个反向代理api列表
     * @throws Exception
     */
    @Test
    public void updateKongApi() throws Exception {
        String url = baseUrl+"apis/kong_api_secure";
        RequestBody body = new FormBody.Builder()
                .add("name", "kong_api_secure")
                .add("upstream_url", "http://"+virtualHost+"/api/secure")
                .add("uris", "/kongtest/api/secure")
                .build();
        Request request = new Request.Builder().url(url).patch(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 删除某个反向代理api
     * @throws Exception
     */
    @Test
    public void deleteKongApi() throws Exception {
        String url = baseUrl+"apis/kong_api_insecure";
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }



}
