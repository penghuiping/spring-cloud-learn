package com.php25.userservice.server.service;

import com.php25.common.exception.JsonException;
import com.php25.userservice.server.dto.CustomerDto;
import com.php25.userservice.server.dto.JwtCredentialDto;

/**
 * Created by penghuiping on 2018/3/15.
 */
public interface KongJwtService {

    /**
     * 0. 生成jwtCustomerId
     *
     * @param customerDto
     * @return
     * @throws JsonException
     */
    String generateJwtCustomerId(CustomerDto customerDto);


    /**
     * 1。 往kong中创建一个jwtCustomer
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    void createJwtCustomer(String jwtCustomerId);


    /**
     * 2. 创建jwt相关的证书
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    JwtCredentialDto generateJwtCredential(String jwtCustomerId);


    /**
     * 3. 生成最终要的jwtToken
     *
     * @return
     * @throws JsonException
     */
    String generateJwtToken(JwtCredentialDto jwtCredentialDto);


    /**
     * 根据JwtCustomerId获取customerDto
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    CustomerDto getByJwtCustomerId(String jwtCustomerId);

    /**
     * 清除jwtToken
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    void cleanJwtToken(String jwtCustomerId);

}
