package com.php25.api.kong;

import okhttp3.*;
import org.junit.Test;

/**
 * Created by penghuiping on 2018/3/13.
 */
public class AdminTargetApiTest {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XFORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://192.168.99.100:30120/";

    private static final String virtualHost = "php.test.com";

    /**
     * 添加一个upstream target
     * @throws Exception
     */
    @Test
    public void addUpstreamTarget() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost+"/targets";
        RequestBody body = new FormBody.Builder()
                .add("target", "192.168.1.47:20001")
                .add("weight", "100")
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 根据名字获取upstream target
     * @throws Exception
     */
    @Test
    public void getUpstreamTargetByName() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost+"/targets";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 列出所有的upstream target
     * @throws Exception
     */
    @Test
    public void listUpstreamTarget() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost+"/targets/all/";
        Request request = new Request.Builder().url(url).get().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 删除upstream target
     * @throws Exception
     */
    @Test
    public void deleteUpstreamTarget() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost+"/targets/9cad2f22-b5af-4fa9-ae6d-4fe813561c70/";
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 手动设置target的状态为正常
     * @throws Exception
     */
    @Test
    public void setTargetHealthy() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost+"/targets/81dfad1b-bfa8-4a00-a503-77fae51969d6/healthy";
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    /**
     * 手动设置target的状态为非正常
     * @throws Exception
     */
    @Test
    public void setTargetUnHealthy() throws Exception {
        String url = baseUrl+"upstreams/"+virtualHost+"/targets/81dfad1b-bfa8-4a00-a503-77fae51969d6/unhealthy";
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }



}
