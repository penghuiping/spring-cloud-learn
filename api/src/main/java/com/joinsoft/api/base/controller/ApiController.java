package com.joinsoft.api.base.controller;

import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.client.dto.JwtCredentialDto;
import com.joinsoft.userservice.client.rest.CustomerRest;
import com.joinsoft.userservice.client.rest.KongJwtRest;
import com.php25.common.controller.JSONController;
import com.php25.common.dto.JSONResponse;
import com.php25.common.exception.JsonException;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by penghuiping on 2018/3/15.
 */
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
     * @throws JsonException
     * @author penghuiping
     * @Time 1/6/15.
     */
    @RequestMapping(value = "/insecure/common/SSOLogin.do", method = RequestMethod.GET)
    public
    @ResponseBody
    JSONResponse SSSLogin(@NotEmpty String mobile, @NotEmpty String password) throws JsonException {
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
    @RequestMapping(value = "/secure/common/SSOLogout.do", method = RequestMethod.GET)
    public
    @ResponseBody
    JSONResponse SSOLogout(@NotEmpty @RequestHeader("X-Consumer-Username") String jwtCustomerId) throws JsonException {
        kongJwtRest.cleanJwtToken(jwtCustomerId);
        return succeed(jwtCustomerId);
    }

    /**
     * 显示客户信息
     *
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "/secure/common/showCustomerInfo.do", method = RequestMethod.GET)
    public
    @ResponseBody
    JSONResponse showCustomerInfo(@NotEmpty @RequestHeader("X-Consumer-Username") String jwtCustomerId) throws JsonException {
        CustomerDto customerDto = kongJwtRest.getByJwtCustomerId(jwtCustomerId);
        return succeed(customerDto);
    }
}
