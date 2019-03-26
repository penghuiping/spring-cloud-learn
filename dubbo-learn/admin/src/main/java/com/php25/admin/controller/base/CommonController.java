package com.php25.admin.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.php25.common.mvc.JSONController;
import com.php25.common.redis.RedisService;
import com.php25.usermicroservice.client.rpc.AdminUserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@Controller
@RequestMapping("/admin/base/common")
public class CommonController extends JSONController {

    @Reference
    private AdminUserRpc adminUserRest;

    @Autowired
    private RedisService redisService;


}
