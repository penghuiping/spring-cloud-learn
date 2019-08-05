package com.php25.usermicroservice.server.service.impl;

import com.php25.common.core.exception.Exceptions;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.IdStringReq;
import com.php25.common.flux.web.IdsStringReq;
import com.php25.usermicroservice.client.dto.Oauth2ClientDto;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.dto.res.Oauth2ClientDtoRes;
import com.php25.usermicroservice.client.service.Oauth2ClientService;
import com.php25.usermicroservice.server.model.Oauth2Client;
import com.php25.usermicroservice.server.repository.Oauth2ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

/**
 * @author: penghuiping
 * @date: 2019/7/28 20:46
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/oauth2")
public class Oauth2ClientServiceImpl implements Oauth2ClientService {

    @Autowired
    private Oauth2ClientRepository oauth2ClientRepository;


    @Override
    @PostMapping("/findOne")
    public Mono<Oauth2ClientDtoRes> findOne(@RequestBody IdStringReq idStringReq1) {
        return Mono.just(idStringReq1).map(idStringReq -> {
            Optional<Oauth2Client> oauth2ClientOptional = oauth2ClientRepository.findById(idStringReq.getId());
            if (!oauth2ClientOptional.isPresent()) {
                throw Exceptions.throwIllegalStateException("无法找到对应oauth2客户端,appId:" + idStringReq.getId());
            } else {
                Oauth2Client oauth2Client = oauth2ClientOptional.get();
                Oauth2ClientDto oauth2ClientDto = new Oauth2ClientDto();
                BeanUtils.copyProperties(oauth2Client, oauth2ClientDto);
                return oauth2ClientDto;
            }
        }).map(oauth2ClientDto -> {
            Oauth2ClientDtoRes oauth2ClientDtoRes = new Oauth2ClientDtoRes();
            oauth2ClientDtoRes.setErrorCode(ApiErrorCode.ok.value);
            oauth2ClientDtoRes.setReturnObject(oauth2ClientDto);
            return oauth2ClientDtoRes;
        });
    }

    @Override
    public Mono<BooleanRes> save(@Valid Mono<Oauth2ClientDto> oauth2ClientDtoMono) {
        return null;
    }

    @Override
    public Mono<BooleanRes> softDelete(@Valid Mono<IdsStringReq> idsStringReqMono) {
        return null;
    }
}
