package com.php25.usermicroservice.client.service;

import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.usermicroservice.client.dto.Oauth2ClientDto;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.dto.res.Oauth2ClientDtoRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:37
 * @description:
 */
public interface Oauth2ClientService {

    /**
     * 根据oauth2的appId查询对应的客户登记信息
     *
     * @param idStringReq
     * @return
     */
    Mono<Oauth2ClientDtoRes> findOne(@Valid IdStringReq idStringReq);

    /**
     * 新增或者更新oauth2认证客户信息
     *
     * @param oauth2ClientDtoMono
     * @return
     */
    Mono<BooleanRes> save(@Valid Mono<Oauth2ClientDto> oauth2ClientDtoMono);

    /**
     * 删除oauth2认证客户信息
     *
     * @param idsStringReqMono
     * @return
     */
    Mono<BooleanRes> softDelete(@Valid Mono<IdsStringReq> idsStringReqMono);


}
