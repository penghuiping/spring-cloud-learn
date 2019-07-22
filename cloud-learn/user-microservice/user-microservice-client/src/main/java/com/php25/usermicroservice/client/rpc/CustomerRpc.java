package com.php25.usermicroservice.client.rpc;

import com.php25.common.flux.IdStringReq;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.bo.LoginBo;
import com.php25.usermicroservice.client.bo.LoginByEmailBo;
import com.php25.usermicroservice.client.bo.LoginByMobileBo;
import com.php25.usermicroservice.client.bo.ResetPwdByEmailBo;
import com.php25.usermicroservice.client.bo.ResetPwdByMobileBo;
import com.php25.usermicroservice.client.bo.StringBo;
import reactor.core.publisher.Mono;

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
    Mono<Boolean> register(Mono<CustomerBo> customerBoMono);

    /**
     * 根据用户名与密码进行登入
     *
     * @param loginBoMono 用户名与密码
     * @return jwt字符串
     */
    Mono<String> loginByUsername(Mono<LoginBo> loginBoMono);


    /**
     * 根据手机号进行登入
     *
     * @param loginByMobileBoMono 手机号,验证码
     * @return jwt字符串
     */
    Mono<String> loginByMobile(Mono<LoginByMobileBo> loginByMobileBoMono);


    /**
     * 根据邮箱与密码进行登入
     *
     * @param loginByEmailBoMono 邮箱,验证码
     * @return jwt字符串
     */
    Mono<String> loginByEmail(Mono<LoginByEmailBo> loginByEmailBoMono);

    /**
     * 根据手机号重置密码
     *
     * @return true:重置成功,false:重置失败
     */
    Mono<Boolean> resetPasswordByMobile(Mono<ResetPwdByMobileBo> resetPwdByMobileBoMono);

    /**
     * 根据邮箱重置密码
     *
     * @return true:重置成功;false:重置失败
     */
    Mono<Boolean> resetPasswordByEmail(Mono<ResetPwdByEmailBo> resetPwdByEmailBoMono);

    /**
     * 根据jwt获取前台用户详情
     *
     * @param jwtMono 通过登入接口获取的jwt令牌
     * @return 前台用户信息
     */
    Mono<CustomerBo> findOne(Mono<IdStringReq> jwtMono);

    /**
     * 验证jwt令牌是否合法
     *
     * @param jwtMono 登入接口获取的jwt令牌
     * @return true:合法，false:不合法
     */
    Mono<Boolean> validateJwt(Mono<IdStringReq> jwtMono);


    /**
     * 修改前台用户相关信息
     *
     * @param customerBoMono 前台用户信息
     * @return true:修改用户信息成功，false:修改用户信息失败
     */
    Mono<Boolean> update(Mono<CustomerBo> customerBoMono);

    /**
     * 通过手机号查询前台客户信息
     *
     * @param mobileMono 手机号
     * @return 返回前台客户信息
     */
    Mono<CustomerBo> findCustomerByMobile(Mono<StringBo> mobileMono);

    /**
     * 登出
     *
     * @param jwtMono jwt令牌
     * @return true:登出成功,false:登出失败
     */
    Mono<Boolean> logout(Mono<IdStringReq> jwtMono);


    Mono<Object> testMessage();

}
