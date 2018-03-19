package com.joinsoft.userservice.service;

import com.joinsoft.common.exception.JsonException;
import com.joinsoft.userservice.dto.CustomerDto;
import com.joinsoft.userservice.dto.JwtCredentialDto;

/**
 * Created by penghuiping on 2018/3/15.
 */
public interface KongJwtService {

    /**
     * 0. 生成jwtCustomerId
     * @param  customerDto
     * @return
     * @throws JsonException
     */
    public String generateJwtCustomerId(CustomerDto customerDto) throws JsonException;


    /**
     * 1。 往kong中创建一个jwtCustomer
     *
     * @param  jwtCustomerId
     * @throws JsonException
     */
    public void createJwtCustomer(String jwtCustomerId) throws JsonException;


    /**
     * 2. 创建jwt相关的证书
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    public JwtCredentialDto generateJwtCredential(String jwtCustomerId) throws JsonException;


    /**
     * 3. 生成最终要的jwtToken
     * @return
     * @throws JsonException
     */
    public String generateJwtToken(JwtCredentialDto jwtCredentialDto) throws JsonException;

}
