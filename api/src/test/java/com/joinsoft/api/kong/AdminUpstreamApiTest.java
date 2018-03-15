package com.joinsoft.api.kong;

import okhttp3.*;
import org.junit.Test;

/**
 * Created by penghuiping on 2018/3/13.
 */
public class AdminUpstreamApiTest {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XFORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://192.168.99.101:30120/";

    private static final String virtualHost = "php.test.com";

    /**
     * 添加一个upstream
     * @throws Exception
     */
    @Test
    public void addUpstreams() throws Exception {
        String url = baseUrl+"upstreams/";
        RequestBody body = new FormBody.Builder()
                .add("name", virtualHost)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 根据名字获取一个upstreams
     * @throws Exception
     */
    @Test
    public void getUpstreamByName() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost;
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 获取upstreams列表
     * @throws Exception
     */
    @Test
    public void listUpstreams() throws Exception {
        String url = baseUrl+"upstreams/";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 更新upstreams
     * @throws Exception
     */
    @Test
    public void updateUpstreams() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost;
        RequestBody body = new FormBody.Builder()
                .add("name", "php.test.com")
                .build();
        Request request = new Request.Builder().url(url).patch(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 删除upstreams
     * @throws Exception
     */
    @Test
    public void deleteUpstreams() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost;
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 删除upstreams
     * @throws Exception
     */
    @Test
    public void getUpstreamHealthy() throws Exception {
        String url = baseUrl+"upstreams/ad01a2e8-a16f-415d-a373-52511ebff0ca/health/";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }



}
