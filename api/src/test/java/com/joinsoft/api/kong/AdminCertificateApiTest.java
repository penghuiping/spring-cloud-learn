package com.joinsoft.api.kong;

import okhttp3.*;
import org.junit.Test;

/**
 * Created by penghuiping on 2018/3/13.
 */
public class AdminCertificateApiTest {

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType XFORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient client = new OkHttpClient();

    private static final String baseUrl = "http://192.168.99.101:30120/";



}
