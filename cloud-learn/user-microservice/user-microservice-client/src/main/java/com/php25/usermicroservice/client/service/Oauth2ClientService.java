package com.php25.usermicroservice.client.service;

import com.php25.common.flux.web.ReqIdString;
import com.php25.common.flux.web.ReqIdsString;
import com.php25.usermicroservice.client.dto.res.Oauth2ClientDto;
import com.php25.usermicroservice.client.dto.res.ResAppDto;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
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
    Mono<ResAppDto> findOne(@Valid ReqIdString idStringReq);

    /**
     * 新增或者更新oauth2认证客户信息
     *
     * @param oauth2ClientDtoMono
     * @return
     */
    Mono<ResBoolean> save(@Valid Mono<Oauth2ClientDto> oauth2ClientDtoMono);

    /**
     * 删除oauth2认证客户信息
     *
     * @param idsStringReqMono
     * @return
     */
    Mono<ResBoolean> softDelete(@Valid Mono<ReqIdsString> idsStringReqMono);


}
