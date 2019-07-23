package com.php25.gateway.controller;


import com.php25.common.core.exception.BusinessException;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.JSONController;
import com.php25.common.flux.JSONResponse;
import com.php25.common.redis.RedisService;
import com.php25.gateway.constant.BusinessError;
import com.php25.gateway.vo.CustomerVo;
import com.php25.gateway.vo.req.GetMsgCodeReq;
import com.php25.gateway.vo.req.LoginByMobileReq;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.rpc.MailRpc;
import com.php25.notifymicroservice.client.rpc.MobileMessageRpc;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.bo.LoginByMobileBo;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author penghuiping
 * @date 2018/03/15
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiCommonController extends JSONController {

    @Autowired
    private CustomerRpc customerRpc;

    @Autowired
    private MailRpc mailRpc;

    @Autowired
    private MobileMessageRpc mobileMessageRpc;

    @Autowired
    private ImageRpc imageRpc;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private RedisService redisService;


    @PostMapping("/common/getMsgCode.do")
    public Mono<JSONResponse> getMsgCode(@Valid @RequestBody Mono<GetMsgCodeReq> getMsgCodeVoMono) {
        return getMsgCodeVoMono.flatMap(params -> {
            log.info("获取短信验证码。。。。");
            log.info(JsonUtil.toPrettyJson(params));
            var sendSmsReq = new SendSMSReq();
            sendSmsReq.setMobile(params.getMobile());
            return mobileMessageRpc.sendSMS(Mono.just(sendSmsReq))
                    .map(booleanRes -> succeed(booleanRes.getReturnObject()));
        });
    }


    @PostMapping(value = "/common/loginByMobile.do")
    public Mono<JSONResponse> loginByMobile(@Valid @RequestBody Mono<LoginByMobileReq> loginByMobileReqMono) {
        return loginByMobileReqMono.map(params -> {
            LoginByMobileBo loginByMobileBo = new LoginByMobileBo();
            loginByMobileBo.setCode(params.getMsgCode());
            loginByMobileBo.setMobile(params.getMobile());
            return loginByMobileBo;
        }).flatMap(loginByMobileBo -> customerRpc.loginByMobile(Mono.just(loginByMobileBo)).map(jwtRes -> {
            if (jwtRes.getErrorCode() != ApiErrorCode.ok.value) {
                log.info("无法找到相应的手机号对应的用户记录,手机号为{}", loginByMobileBo.getMobile());
                throw Exceptions.throwBusinessException(BusinessError.MOBILE_NOT_EXIST_ERROR);
            } else {
                String jwt = jwtRes.getReturnObject();
                IdStringReq idStringReq = new IdStringReq();
                idStringReq.setId(jwt);
                return idStringReq;
            }
        }).flatMap(idStringReq -> customerRpc.findOne(Mono.just(idStringReq)).flatMap(customerBoRes -> {
            if (customerBoRes.getErrorCode() == ApiErrorCode.ok.value) {
                CustomerVo customerVo = new CustomerVo();
                CustomerBo customerBo = customerBoRes.getReturnObject();
                BeanUtils.copyProperties(customerBo, customerVo);
                IdStringReq idStringReq1 = new IdStringReq();
                idStringReq1.setId(customerBo.getImageId());
                customerVo.setToken(idStringReq.getId());
                return imageRpc.findOne(Mono.just(idStringReq1)).map(imgBoRes -> {
                    if(imgBoRes.getErrorCode() == ApiErrorCode.ok.value) {
                        customerVo.setImage(imgBoRes.getReturnObject().getImgUrl());
                    }
                    return succeed(customerVo);
                });
            } else {
                throw Exceptions.throwBusinessException(BusinessError.COMMON_ERROR);
            }
        }))).onErrorResume(throwable -> {
            if (throwable instanceof BusinessException) {
                var businessException = (BusinessException) throwable;
                return Mono.just(failed(businessException.getBusinessErrorStatus()));
            } else {
                throw Exceptions.throwIllegalStateException("出错啦", throwable);
            }
        });
    }


//    @PostMapping(value = "/common/loginByUsername.do")
//    public Mono<JSONResponse> loginByUsername(@Valid Mono<LoginByUsernameReq> loginByUsernameReqMono) {
//        return loginByUsernameReqMono.flatMap(params -> {
//            //获取参数
//            String username = params.getUsername();
//            String password = params.getPassword();
//            String kaptchaCode = params.getKaptchaCode();
//
//            //验证图形验证码
//            if (!redisService.exists("kaptcha" + kaptchaCode)) {
//                return Mono.just(failed(BusinessError.KAPTCHA_ERROR));
//            }
//
//            LoginBo loginBo = new LoginBo();
//            loginBo.setPassword(password);
//            loginBo.setUsername(username);
//
//
//            return customerRpc.loginByUsername(Mono.just(loginBo)).flatMap(jwtRes -> {
//                if (ApiErrorCode.ok.value != jwtRes.getErrorCode()) {
//                    return Mono.just(failed(BusinessError.USERNAME_PASSWORD_ERROR));
//                } else {
//                    String jwt = jwtRes.getReturnObject();
//                    IdStringReq idStringReq = new IdStringReq();
//                    idStringReq.setId(jwt);
//                    return customerRpc.findOne(Mono.just(idStringReq))
//                            .flatMap(customerBoRes -> {
//                                if (customerBoRes.getErrorCode() != ApiErrorCode.ok.value) {
//                                    return Mono.just(failed(BusinessError.COMMON_ERROR));
//                                } else {
//                                    //获取头像
//                                    IdStringReq idStringReq1 = new IdStringReq();
//                                    idStringReq1.setId(customerBoRes.getReturnObject().getImageId());
//                                    return imageRpc.findOne(Mono.just(idStringReq1)).map(imgBoRes -> {
//                                        CustomerVo customerVo = new CustomerVo();
//                                        BeanUtils.copyProperties(customerBoRes.getReturnObject(), customerVo);
//                                        customerVo.setToken(jwt);
//                                        if (imgBoRes.getErrorCode() == ApiErrorCode.ok.value) {
//                                            customerVo.setImage(imgBoRes.getReturnObject().getImgUrl());
//                                        }
//                                        return succeed(customerVo);
//                                    });
//                                }
//                            });
//                }
//            });
//        });
//    }
//
//
//    @PostMapping(value = "/common/register.do")
//    public Mono<JSONResponse> register(@Valid Mono<RegisterReq> registerReqMono) {
//        return registerReqMono.flatMap(registerReq -> {
//            String mobile = registerReq.getMobile();
//            String msgCode = registerReq.getMsgCode();
//
//            ValidateSMSReq validateSMSReq = new ValidateSMSReq();
//            validateSMSReq.setMobile(mobile);
//            validateSMSReq.setMsgCode(msgCode);
//
//            //效验短信验证码
//            return mobileMessageRpc.validateSMS(Mono.just(validateSMSReq)).flatMap(aBoolean -> {
//                if (!aBoolean) {
//                    return Mono.just(failed(BusinessError.MOBILE_CODE_ERROR));
//                } else {
//                    StringBo stringBo = new StringBo();
//                    stringBo.setContent(mobile);
//                    return customerRpc.findCustomerByMobile(Mono.just(stringBo)).flatMap(customerBoRes -> {
//                        //判断此手机是否可以注册
//                        if (customerBoRes.getErrorCode() != ApiErrorCode.ok.value) {
//                            log.info("{},此手机号在系统中已经存在", mobile);
//                            return Mono.just(failed(BusinessError.MOBILE_ALREADY_EXIST_ERROR));
//                        } else {
//                            CustomerBo customerBo1 = new CustomerBo();
//                            customerBo1.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
//                            customerBo1.setCreateTime(LocalDateTime.now());
//                            customerBo1.setEnable(1);
//                            String username = registerReq.getUsername();
//                            String password = registerReq.getPassword();
//                            customerBo1.setUsername(username);
//                            customerBo1.setPassword(password);
//                            //注册
//                            return customerRpc.register(Mono.just(customerBo1)).map(this::succeed);
//                        }
//                    });
//                }
//            });
//        });
//    }
//
//
//    @GetMapping(value = "/common/SSOLogout.do")
//    public Mono<JSONResponse> logout(@NotBlank @RequestHeader(name = "jwt") String jwt) {
//        IdStringReq idStringReq = new IdStringReq();
//        idStringReq.setId(jwt);
//        return customerRpc.logout(Mono.just(idStringReq)).map(booleanRes -> {
//            if (booleanRes.getErrorCode() != ApiErrorCode.ok.value) {
//                throw new ControllerException(booleanRes.getMessage());
//            } else {
//                return succeed(booleanRes.getReturnObject());
//            }
//        });
//    }
//
//    @GetMapping(value = "/common/showCustomerInfo.do")
//    public Mono<JSONResponse> showCustomerInfo(@NotBlank @RequestHeader(name = "jwt") String jwt) {
//        IdStringReq idStringReq = new IdStringReq();
//        idStringReq.setId(jwt);
//        return customerRpc.findOne(Mono.just(idStringReq)).flatMap(customerBoRes -> {
//            if (customerBoRes.getErrorCode() != ApiErrorCode.ok.value) {
//                return Mono.just(failed(BusinessError.COMMON_ERROR));
//            } else {
//                CustomerBo customerBo = customerBoRes.getReturnObject();
//                CustomerVo customerVo = new CustomerVo();
//                BeanUtils.copyProperties(customerBo, customerVo);
//                customerVo.setToken(jwt);
//
//                IdStringReq idStringReq1 = new IdStringReq();
//                idStringReq1.setId(customerBo.getImageId());
//
//                return imageRpc.findOne(Mono.just(idStringReq1)).map(imgBoRes -> {
//                    if (imgBoRes.getErrorCode() == ApiErrorCode.ok.value) {
//                        customerVo.setImage(imgBoRes.getReturnObject().getImgUrl());
//                    }
//                    return succeed(customerVo);
//                });
//            }
//        });
//    }
//
//
//    @GetMapping("/common/kaptchaRender.do")
//    public Mono<String> kaptchaRender() {
//        String kaptcha = RandomUtil.getRandom(6) + "";
//        redisService.set("kaptcha" + kaptcha, 1);
//        return ResponseEntity.ok(kaptcha);
//    }
//
//
//    @PostMapping("/common/changePersonInfo.do")
//    public Mono<JSONResponse> changePersonInfo(@NotBlank @RequestHeader(name = "jwt") String jwt) {
//        ResultDto<CustomerBo> resultDto = customerRpc.findOne(jwt);
//        return null;
//    }
//
//    @GetMapping("/testMessage.do")
//    public ResponseEntity<String> testMessage() {
//        customerRpc.testMessage();
//        return ResponseEntity.ok("ok");
//    }


}
