package com.php25.userservice.client.rest;

import com.php25.common.exception.JsonException;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.dto.JwtCredentialDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by penghuiping on 2018/3/21.
 */
public interface KongJwtRest {

    String baseUri = "/kongJwt";


    /**
     * 0. 生成jwtCustomerId
     *
     * @param customerDto
     * @return
     * @throws JsonException
     */
    @RequestLine("POST " + baseUri + "/generateJwtCustomerId")
    @Headers("Content-Type: application/json")
    public String generateJwtCustomerId(@NotNull CustomerDto customerDto);


    /**
     * 1。 往kong中创建一个jwtCustomer
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    @RequestLine("GET " + baseUri + "/createJwtCustomer?jwtCustomerId={jwtCustomerId}")
    public void createJwtCustomer(@NotBlank @Param("jwtCustomerId") String jwtCustomerId);


    /**
     * 2. 创建jwt相关的证书
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    @RequestLine("GET " + baseUri + "/generateJwtCredential?jwtCustomerId={jwtCustomerId}")
    public JwtCredentialDto generateJwtCredential(@NotBlank @Param("jwtCustomerId") String jwtCustomerId);


    /**
     * 3. 生成最终要的jwtToken
     *
     * @return
     * @throws JsonException
     */
    @RequestLine("POST " + baseUri + "/generateJwtToken")
    @Headers("Content-Type: application/json")
    public String generateJwtToken(@NotNull JwtCredentialDto jwtCredentialDto);


    /**
     * 根据JwtCustomerId获取customerDto
     *
     * @param jwtCustomerId
     * @return
     * @throws JsonException
     */
    @RequestLine("GET " + baseUri + "/getByJwtCustomerId?jwtCustomerId={jwtCustomerId}")
    public CustomerDto getByJwtCustomerId(@NotBlank @Param("jwtCustomerId") String jwtCustomerId);

    /**
     * 清除jwtToken
     *
     * @param jwtCustomerId
     * @throws JsonException
     */
    @RequestLine("GET " + baseUri + "/cleanJwtToken?jwtCustomerId={jwtCustomerId}")
    public void cleanJwtToken(@NotBlank @Param("jwtCustomerId") String jwtCustomerId);
}
