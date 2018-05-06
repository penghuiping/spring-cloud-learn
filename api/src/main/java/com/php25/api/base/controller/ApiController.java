package com.php25.api.base.controller;

import com.php25.common.controller.JSONController;
import com.php25.common.dto.JSONResponse;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.dto.JwtCredentialDto;
import com.php25.userservice.client.rest.CustomerRest;
import com.php25.userservice.client.rest.KongJwtRest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Validated
@Controller
@RequestMapping("/api")
public class ApiController extends JSONController {

    @Autowired
    private CustomerRest customerRest;


    @Autowired
    private KongJwtRest kongJwtRest;

    /**
     * 登入接口，返回access_token与refresh_token
     * access_token用于应用访问
     * refresh_token用于重新获取access_token
     *
     * @param mobile
     * @param password
     * @return
     * @author penghuiping
     * @Time 1/6/15.
     */
    @ApiOperation(value = "登入", notes = "输入手机号与密码登入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/insecure/common/SSOLogin.do", method = RequestMethod.GET)
    public @ResponseBody
    JSONResponse SSSLogin(@RequestParam @NotEmpty String mobile, @RequestParam @NotEmpty String password) {
        CustomerDto customer = customerRest.findOneByPhoneAndPassword(mobile, password);
        if (null != customer) {
            String jwtCustomerId = kongJwtRest.generateJwtCustomerId(customer);
            kongJwtRest.createJwtCustomer(jwtCustomerId);
            JwtCredentialDto jwtCredentialDto = kongJwtRest.generateJwtCredential(jwtCustomerId);
            String jwt = kongJwtRest.generateJwtToken(jwtCredentialDto);
            return succeed(jwt);
        } else {
            return failed("登入失败");
        }
    }

    /**
     * 登出
     *
     * @return
     * @throws Throwable
     */
    @ApiOperation(value = "登出", notes = "登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Consumer-Username", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @RequestMapping(value = "/secure/common/SSOLogout.do", method = RequestMethod.GET)
    public
    @ResponseBody
    JSONResponse SSOLogout(@NotEmpty @RequestHeader(name = "X-Consumer-Username") String jwtCustomerId) {
        kongJwtRest.cleanJwtToken(jwtCustomerId);
        return succeed(jwtCustomerId);
    }

    /**
     * 显示客户信息
     *
     * @return
     * @throws Throwable
     */
    @ApiOperation(value = "查询客户信息", notes = "查询客户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Consumer-Username", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @RequestMapping(value = "/secure/common/showCustomerInfo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    JSONResponse showCustomerInfo(@NotEmpty @RequestHeader(name = "X-Consumer-Username") String jwtCustomerId) {
        CustomerDto customerDto = kongJwtRest.getByJwtCustomerId(jwtCustomerId);
        return succeed(customerDto);
    }
}
