package com.php25.usermicroservice.client.service;

import com.php25.common.flux.web.IdLongReq;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.ResetPwdByEmailDto;
import com.php25.usermicroservice.client.dto.ResetPwdByMobileDto;
import com.php25.usermicroservice.client.dto.StringDto;
import com.php25.usermicroservice.client.dto.res.BooleanRes;
import com.php25.usermicroservice.client.dto.res.CustomerDtoRes;
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
    Mono<BooleanRes> register(@Valid CustomerDto customerDto);

    /**
     * 根据手机号重置密码
     *
     * @return true:重置成功,false:重置失败
     */
    Mono<BooleanRes> resetPasswordByMobile(@Valid ResetPwdByMobileDto resetPwdByMobileDto);

    /**
     * 根据邮箱重置密码
     *
     * @return true:重置成功;false:重置失败
     */
    Mono<BooleanRes> resetPasswordByEmail(@Valid ResetPwdByEmailDto resetPwdByEmailDto);

    /**
     * 根据jwt获取前台用户详情
     *
     * @param idLongReq 用户id
     * @return 前台用户信息
     */
    Mono<CustomerDtoRes> findOne(@Valid IdLongReq idLongReq);


    /**
     * 修改前台用户相关信息
     *
     * @param customerDto 前台用户信息
     * @return true:修改用户信息成功，false:修改用户信息失败
     */
    Mono<BooleanRes> update(@Valid CustomerDto customerDto);

    /**
     * 通过手机号查询前台客户信息
     *
     * @param mobile 手机号
     * @return 返回前台客户信息
     */
    Mono<CustomerDtoRes> findCustomerByMobile(@Valid StringDto mobile);


    Mono<CustomerDtoRes> findCustomerByUsername(@Valid StringDto username);

//    Mono<Object> testMessage();

}
