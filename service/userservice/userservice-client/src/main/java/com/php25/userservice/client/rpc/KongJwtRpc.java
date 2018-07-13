package com.php25.userservice.client.rpc;

import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.dto.JwtCredentialDto;

/**
 * Created by penghuiping on 2018/3/15.
 */
public interface KongJwtRpc {

    /**
     * 0. 生成jwtCustomerId
     *
     * @param customerDto
     * @return
     */
    String generateJwtCustomerId(CustomerDto customerDto);


    /**
     * 1。 往kong中创建一个jwtCustomer
     *
     * @param jwtCustomerId
     */
    void createJwtCustomer(String jwtCustomerId);


    /**
     * 2. 创建jwt相关的证书
     *
     * @param jwtCustomerId
     * @return
     */
    JwtCredentialDto generateJwtCredential(String jwtCustomerId);


    /**
     * 3. 生成最终要的jwtToken
     *
     * @return
     */
    String generateJwtToken(JwtCredentialDto jwtCredentialDto);


    /**
     * 根据JwtCustomerId获取customerDto
     *
     * @param jwtCustomerId
     * @return
     */
    CustomerDto getByJwtCustomerId(String jwtCustomerId);

    /**
     * 清除jwtToken
     *
     * @param jwtCustomerId
     */
    void cleanJwtToken(String jwtCustomerId);

}
