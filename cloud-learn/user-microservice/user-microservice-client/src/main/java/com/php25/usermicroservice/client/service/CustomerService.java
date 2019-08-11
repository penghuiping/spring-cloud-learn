package com.php25.usermicroservice.client.service;

import com.php25.common.flux.web.ReqIdLong;
import com.php25.usermicroservice.client.dto.req.ReqResetPwdByEmailDto;
import com.php25.usermicroservice.client.dto.req.ReqResetPwdByMobileDto;
import com.php25.usermicroservice.client.dto.req.ReqStringDto;
import com.php25.usermicroservice.client.dto.res.CustomerDto;
import com.php25.usermicroservice.client.dto.res.ResBoolean;
import com.php25.usermicroservice.client.dto.res.ResCustomerDto;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 前台用户操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 13:06
 * @description:
 */
public interface CustomerService {

    /**
     * 注册
     *
     * @param customerDto 客户信息
     * @return true:注册成功，false:注册失败
     */
    Mono<ResBoolean> register(@Valid CustomerDto customerDto);

    /**
     * 根据手机号重置密码
     *
     * @return true:重置成功,false:重置失败
     */
    Mono<ResBoolean> resetPasswordByMobile(@Valid ReqResetPwdByMobileDto resetPwdByMobileDto);

    /**
     * 根据邮箱重置密码
     *
     * @return true:重置成功;false:重置失败
     */
    Mono<ResBoolean> resetPasswordByEmail(@Valid ReqResetPwdByEmailDto resetPwdByEmailDto);

    /**
     * 根据jwt获取前台用户详情
     *
     * @param idLongReq 用户id
     * @return 前台用户信息
     */
    Mono<ResCustomerDto> findOne(@Valid ReqIdLong idLongReq);


    /**
     * 修改前台用户相关信息
     *
     * @param customerDto 前台用户信息
     * @return true:修改用户信息成功，false:修改用户信息失败
     */
    Mono<ResBoolean> update(@Valid CustomerDto customerDto);

    /**
     * 通过手机号查询前台客户信息
     *
     * @param mobile 手机号
     * @return 返回前台客户信息
     */
    Mono<ResCustomerDto> findCustomerByMobile(@Valid ReqStringDto mobile);


    Mono<ResCustomerDto> findCustomerByUsername(@Valid ReqStringDto username);

//    Mono<Object> testMessage();

}
