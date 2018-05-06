package com.php25.admin.controller.base;

import com.php25.common.controller.JSONController;
import com.php25.common.dto.JSONResponse;
import com.php25.common.service.RedisService;
import com.php25.userservice.client.dto.AdminUserDto;
import com.php25.userservice.client.rest.AdminUserRest;
import com.php25.userservice.client.rest.TokenRest;
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

    @Autowired
    private AdminUserRest adminUserRest;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenRest tokenRest;

    @ApiOperation(value = "登入", notes = "登入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "form")
    })
    @RequestMapping(value = "/SSOLogin.do", method = RequestMethod.POST)
    public ResponseEntity<JSONResponse>
    SSOLogin(@RequestParam @NotEmpty String username, @RequestParam @NotEmpty String password) {
        AdminUserDto adminUserDto = adminUserRest.findByUsernameAndPassword(username, password);
        //首先清空原来的token
        Map<String, String> result = tokenRest.getTokenByObjId(adminUserDto.getId());
        return ResponseEntity.ok(succeed(result));
    }

}
