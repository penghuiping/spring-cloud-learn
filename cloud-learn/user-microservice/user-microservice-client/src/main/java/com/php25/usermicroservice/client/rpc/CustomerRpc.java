package com.php25.usermicroservice.client.rpc;

import com.php25.common.flux.IdStringReq;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.LoginByEmailBo;
import com.php25.usermicroservice.client.bo.LoginByMobileBo;
import com.php25.usermicroservice.client.bo.ResetPwdByEmailBo;
import com.php25.usermicroservice.client.bo.ResetPwdByMobileBo;
import com.php25.usermicroservice.client.bo.StringBo;
import com.php25.usermicroservice.client.bo.res.BooleanRes;
import com.php25.usermicroservice.client.bo.res.CustomerBoRes;
import com.php25.usermicroservice.client.bo.res.StringRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * 前台用户操作
 *
 * @author: penghuiping
 * @date: 2019/1/2 13:06
 * @description:
 */
public interface CustomerRpc {

    /**
     * 注册
     *
     * @param customerBoMono 客户信息
     * @return true:注册成功，false:注册失败
     */
    Mono<BooleanRes> register(@Valid Mono<CustomerBo> customerBoMono);

    /**
     * 根据用户名与密码进行登入
     *
     * @param loginBoMono 用户名与密码
     * @return jwt字符串
     */
    Mono<StringRes> loginByUsername(@Valid Mono<LoginBo> loginBoMono);


    /**
     * 根据手机号进行登入
     *
     * @param loginByMobileBoMono 手机号,验证码
     * @return jwt字符串
     */
    Mono<StringRes> loginByMobile(@Valid Mono<LoginByMobileBo> loginByMobileBoMono);


    /**
     * 根据邮箱与密码进行登入
     *
     * @param loginByEmailBoMono 邮箱,验证码
     * @return jwt字符串
     */
    Mono<StringRes> loginByEmail(@Valid Mono<LoginByEmailBo> loginByEmailBoMono);

    /**
     * 根据手机号重置密码
     *
     * @return true:重置成功,false:重置失败
     */
    Mono<BooleanRes> resetPasswordByMobile(@Valid Mono<ResetPwdByMobileBo> resetPwdByMobileBoMono);

    /**
     * 根据邮箱重置密码
     *
     * @return true:重置成功;false:重置失败
     */
    Mono<BooleanRes> resetPasswordByEmail(@Valid Mono<ResetPwdByEmailBo> resetPwdByEmailBoMono);

    /**
     * 根据jwt获取前台用户详情
     *
     * @param jwtMono 通过登入接口获取的jwt令牌
     * @return 前台用户信息
     */
    Mono<CustomerBoRes> findOne(@Valid Mono<IdStringReq> jwtMono);

    /**
     * 验证jwt令牌是否合法
     *
     * @param jwtMono 登入接口获取的jwt令牌
     * @return true:合法，false:不合法
     */
    Mono<BooleanRes> validateJwt(@Valid Mono<IdStringReq> jwtMono);


    /**
     * 修改前台用户相关信息
     *
     * @param customerBoMono 前台用户信息
     * @return true:修改用户信息成功，false:修改用户信息失败
     */
    Mono<BooleanRes> update(@Valid Mono<CustomerBo> customerBoMono);

    /**
     * 通过手机号查询前台客户信息
     *
     * @param mobileMono 手机号
     * @return 返回前台客户信息
     */
    Mono<CustomerBoRes> findCustomerByMobile(@Valid Mono<StringBo> mobileMono);

    /**
     * 登出
     *
     * @param jwtMono jwt令牌
     * @return true:登出成功,false:登出失败
     */
    Mono<BooleanRes> logout(@Valid Mono<IdStringReq> jwtMono);


//    Mono<Object> testMessage();

}
