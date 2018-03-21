package com.joinsoft.userservice.server.rest;

import com.joinsoft.common.exception.JsonException;
import com.joinsoft.userservice.client.dto.CustomerDto;
import com.joinsoft.userservice.client.dto.JwtCredentialDto;
import com.joinsoft.userservice.client.rest.KongJwtRest;
import com.joinsoft.userservice.server.service.KongJwtService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * Created by penghuiping on 2018/3/21.
 */
@Validated
@RestController
@RequestMapping("/kongJwt")
public class KongJwtRestImpl implements KongJwtRest {

    @Autowired
    KongJwtService kongJwtService;

    /**
     * 0. 生成jwtCustomerId
     *
     * @param customerDto
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/generateJwtCustomerId")
    public String generateJwtCustomerId(@NotNull @RequestBody CustomerDto customerDto) {
        return kongJwtService.generateJwtCustomerId(customerDto);
    }


    /**
     * 1。 往kong中创建一个jwtCustomer
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    @RequestMapping(value = "/createJwtCustomer")
    public void createJwtCustomer(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId) {
        kongJwtService.createJwtCustomer(jwtCustomerId);
    }


    /**
     * 2. 创建jwt相关的证书
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/generateJwtCredential")
    public JwtCredentialDto generateJwtCredential(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId) {
        return kongJwtService.generateJwtCredential(jwtCustomerId);
    }


    /**
     * 3. 生成最终要的jwtToken
     *
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/generateJwtToken")
    public String generateJwtToken(@NotNull @RequestBody JwtCredentialDto jwtCredentialDto) {
        return kongJwtService.generateJwtToken(jwtCredentialDto);
    }


    /**
     * 根据JwtCustomerId获取customerDto
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/getByJwtCustomerId")
    public CustomerDto getByJwtCustomerId(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId) {
        return kongJwtService.getByJwtCustomerId(jwtCustomerId);
    }

    /**
     * 清除jwtToken
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    @RequestMapping(value = "/cleanJwtToken")
    public void cleanJwtToken(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId) {
        kongJwtService.cleanJwtToken(jwtCustomerId);
    }
}
