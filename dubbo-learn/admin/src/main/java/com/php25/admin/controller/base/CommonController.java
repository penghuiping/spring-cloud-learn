package com.php25.admin.controller.base;

import com.php25.common.mvc.JSONController;
import com.php25.common.mvc.JSONResponse;
import com.php25.common.mvc.JsonException;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@Controller
@RequestMapping("/admin/base/common")
public class CommonController extends JSONController {

    @ApiOperation(value = "测试", notes = "测试</br>" +
            "错误码:<br>" +
            "10000=出错啦,请重试<br>" +
            "20001=短信验证码不正确<br>" +
            "20002=手机号系统不存在", response = String.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
    })
    @GetMapping(value = "/test.do")
    public ResponseEntity<JSONResponse> test() throws JsonException {
        return (ResponseEntity.ok(succeed("hello world")));
    }
}
