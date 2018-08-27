package com.php25.admin.controller.base;

import com.alibaba.dubbo.config.annotation.Reference;
import com.php25.common.mvc.JSONController;
import com.php25.common.mvc.JSONResponse;
import com.php25.common.mvc.JsonException;
import com.php25.common.redis.RedisService;
import com.php25.userservice.client.dto.AdminUserDto;
import com.php25.userservice.client.rpc.AdminUserRpc;
import com.php25.userservice.client.rpc.TokenRpc;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Validated
@Controller
@RequestMapping("/admin/base/common")
public class CommonController extends JSONController {

    @Reference
    private AdminUserRpc adminUserRest;

    @Autowired
    private RedisService redisService;

    @Reference
    private TokenRpc<String> tokenRest;

    @ApiOperation(value = "登入", notes = "登入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "form")
    })
    @RequestMapping(value = "/SSOLogin.do", method = RequestMethod.POST)
    public ResponseEntity<JSONResponse>
    SSOLogin(@RequestParam @NotEmpty String username, @RequestParam @NotEmpty String password) throws JsonException {
        AdminUserDto adminUserDto = adminUserRest.findByLoginNameAndPassword(username, password);
        //首先清空原来的token
        Map<String, String> result = tokenRest.generateToken(adminUserDto.getId().toString());
        return ResponseEntity.ok(succeed(result));
    }

}
