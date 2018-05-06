package com.php25.userservice.client.rest;

import com.php25.common.exception.JsonException;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.dto.JwtCredentialDto;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;


/**
 * Created by penghuiping on 2018/3/21.
 */

public interface KongJwtRest {

    /**
     * 0. 生成jwtCustomerId
     *
     * @param customerDto
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/generateJwtCustomerId")
    String generateJwtCustomerId(@NotNull @RequestBody CustomerDto customerDto);


    /**
     * 1。 往kong中创建一个jwtCustomer
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    @RequestMapping(value = "/createJwtCustomer")
    void createJwtCustomer(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId);


    /**
     * 2. 创建jwt相关的证书
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/generateJwtCredential")
    JwtCredentialDto generateJwtCredential(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId);


    /**
     * 3. 生成最终要的jwtToken
     *
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/generateJwtToken")
    String generateJwtToken(@NotNull @RequestBody JwtCredentialDto jwtCredentialDto);


    /**
     * 根据JwtCustomerId获取customerDto
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    @RequestMapping(value = "/getByJwtCustomerId")
    CustomerDto getByJwtCustomerId(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId);

    /**
     * 清除jwtToken
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    @RequestMapping(value = "/cleanJwtToken")
    void cleanJwtToken(@NotBlank @RequestParam("jwtCustomerId") String jwtCustomerId);
}
