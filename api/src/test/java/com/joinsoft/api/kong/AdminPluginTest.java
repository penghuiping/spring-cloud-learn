package com.joinsoft.api.kong;

import okhttp3.*;
import org.junit.Test;

/**
 * Created by penghuiping on 2018/3/13.
 */
public class AdminPluginTest {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XFORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://192.168.99.101:30120/plugins";

    /**
     * 添加一个插件
     * @throws Exception
     */
    @Test
    public void addPlugin() throws Exception {
        String url = baseUrl+"";
        RequestBody body = new FormBody.Builder()
                .add("name", "rate-limiting")
                .add("config.second", "5")
                .add("config.hour", "10000")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 获取插件列表
     * @throws Exception
     */
    @Test
    public void getPluginList() throws Exception {
        String url = baseUrl+"/";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 获取一个插件详情
     * @throws Exception
     */
    @Test
    public void getPlugin() throws Exception {
        String url = baseUrl+"/963f4d6c-45ff-444a-99b3-965e7f2b2b87";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 更新插件
     * @throws Exception
     */
    @Test
    public void updatePlugin() throws Exception {
        String url = baseUrl+"/963f4d6c-45ff-444a-99b3-965e7f2b2b87";
        RequestBody body = new FormBody.Builder()
                .add("name", "rate-limiting")
                .add("config.second", "100")
                .add("config.hour", "10000")
                .build();
        Request request = new Request.Builder().url(url).patch(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 删除插件
     * @throws Exception
     */
    @Test
    public void deletePlugin() throws Exception {
        String url = baseUrl+"/963f4d6c-45ff-444a-99b3-965e7f2b2b87";
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 获取有效的插件
     * @throws Exception
     */
    @Test
    public void getEnabledPlugin() throws Exception {
        String url = baseUrl+"/enabled";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }




}
