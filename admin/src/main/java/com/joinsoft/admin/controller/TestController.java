package com.joinsoft.admin.controller;

import com.joinsoft.common.controller.JSONController;
import com.joinsoft.common.dto.JSONResponse;
import com.joinsoft.common.exception.JsonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by penghuiping on 2018/3/8.
 */
@Controller
@RequestMapping("/test")
public class TestController extends JSONController{

    @Value("${k8s.value}")
    private String value;

    @RequestMapping("/hello")
    public @ResponseBody  JSONResponse hello() throws JsonException {
        String hostname=System.getenv("HOSTNAME");
        return succeed(hostname+","+value);
    }
}
