package com.php25.api.base.controller;

import com.php25.common.controller.JSONController;
import com.php25.common.dto.JSONResponse;
import com.php25.common.exception.JsonException;
import com.php25.userservice.server.dto.CustomerDto;
import com.php25.userservice.server.dto.JwtCredentialDto;
import com.php25.userservice.server.service.CustomerService;
import com.php25.userservice.server.service.KongJwtService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by penghuiping on 2018/3/15.
 */
@Validated
@Controller
@RequestMapping("/api")
public class ApiController extends JSONController {

    @Autowired
    private CustomerService customerService;


    @Autowired
    private KongJwtService kongJwtService;

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
    JSONResponse SSSLogin(@RequestParam @NotEmpty String mobile, @RequestParam @NotEmpty String password) throws JsonException {
        Optional<CustomerDto> customer = customerService.findOneByPhoneAndPassword(mobile, password);
        if (customer.isPresent()) {
            String jwtCustomerId = kongJwtService.generateJwtCustomerId(customer.get());
            kongJwtService.createJwtCustomer(jwtCustomerId);
            JwtCredentialDto jwtCredentialDto = kongJwtService.generateJwtCredential(jwtCustomerId);
            String jwt = kongJwtService.generateJwtToken(jwtCredentialDto);
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
    JSONResponse SSOLogout(@NotEmpty @RequestHeader(name = "X-Consumer-Username") String jwtCustomerId) throws JsonException {
        kongJwtService.cleanJwtToken(jwtCustomerId);
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
    JSONResponse showCustomerInfo(@NotEmpty @RequestHeader(name = "X-Consumer-Username") String jwtCustomerId) throws JsonException {
        CustomerDto customerDto = kongJwtService.getByJwtCustomerId(jwtCustomerId);
        return succeed(customerDto);
    }
}
