package com.php25.gateway.controller;


import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.core.util.StringUtil;
import com.php25.common.flux.JSONController;
import com.php25.common.flux.JSONResponse;
import com.php25.common.redis.RedisService;
import com.php25.gateway.constant.BusinessError;
import com.php25.gateway.vo.req.GetMsgCodeReq;
import com.php25.gateway.vo.req.LoginByMobileReq;
import com.php25.gateway.vo.req.LoginByUsernameReq;
import com.php25.gateway.vo.req.RegisterReq;
import com.php25.gateway.vo.resp.CustomerResp;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.rpc.MailRpc;
import com.php25.notifymicroservice.client.rpc.MobileMessageRpc;
import com.php25.usermicroservice.client.bo.CustomerBo;
import com.php25.usermicroservice.client.rpc.CustomerRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Date;

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
    public Mono<JSONResponse> getMsgCode(@Valid Mono<GetMsgCodeReq> getMsgCodeVoMono) {
        return getMsgCodeVoMono.flatMap(params -> {
            log.info("获取短信验证码。。。。");
            log.info(JsonUtil.toPrettyJson(params));
            var sendSmsReq = new SendSMSReq();
            //sendSmsReq.setMobile(params.getMobile());
            return mobileMessageRpc.sendSMS(sendSmsReq)
                    .map(mobile2 -> (succeed(true)));
        });
    }


//    @PostMapping(value = "/common/loginByMobile.do")
//    public Mono<JSONResponse> loginByMobile(@Valid Mono<LoginByMobileReq> loginByMobileReqMono) {
//        return loginByMobileReqMono.flatMap(params -> {
//            return customerRpc.loginByMobile(params.getMobile(), params.getMsgCode()).flatMap(jwt -> {
//                if (StringUtil.isBlank(jwt)) {
//                    log.info("无法找到相应的手机号对应的用户记录,手机号为{}", params.getMobile());
//                    return Mono.just(failed(BusinessError.MOBILE_NOT_EXIST_ERROR));
//                } else {
//                    return customerRpc.findOne(jwt).map(customerBo -> {
//                        if (null != customerBo) {
//                            CustomerResp customerVo = new CustomerResp();
//                            BeanUtils.copyProperties(customerBo, customerVo);
//                            var imgBoMono = imageRpc.findOne(customerBo.getImageId());
//                            var imgBo = imgBoMono.block();
//                            if (null != imgBo) {
//                                customerVo.setImage(imgBo.getImgUrl());
//                            }
//                            customerVo.setToken(jwt);
//                            return succeed(customerVo);
//                        } else {
//                            return failed(BusinessError.COMMON_ERROR);
//                        }
//                    });
//                }
//            });
//        });
//    }


//    @PostMapping(value = "/common/loginByUsername.do")
//    public Mono<JSONResponse> loginByUsername(@Valid Mono<LoginByUsernameReq> loginByUsernameReqMono) {
//        return loginByUsernameReqMono.flatMap(params -> {
//            //获取参数
//            String username = params.getUsername();
//            String password = params.getPassword();
//            String kaptchaCode = params.getKaptchaCode();
//
////            //验证图形验证码
////            if (!redisService.exists("kaptcha" + kaptchaCode)) {
////                return Mono.just(failed(BusinessError.KAPTCHA_ERROR));
////            }
//
//            return customerRpc.loginByUsername(username, password).flatMap(jwt -> {
//                if (StringUtil.isBlank(jwt)) {
//                    return Mono.just(failed(BusinessError.USERNAME_PASSWORD_ERROR));
//                } else {
//                    return customerRpc.findOne(jwt).flatMap(customerBo -> {
//                        if (customerBo == null) {
//                            return Mono.just(failed(BusinessError.COMMON_ERROR));
//                        } else {
//                            //获取头像
//                            return imageRpc.findOne(customerBo.getImageId()).map(imgBo -> {
//                                CustomerResp customerVo = new CustomerResp();
//                                BeanUtils.copyProperties(customerBo, customerVo);
//                                customerVo.setImage(imgBo.getImgUrl());
//                                customerVo.setToken(jwt);
//                                return succeed(customerVo);
//                            });
//                        }
//                    });
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
//            //效验短信验证码
//            return mobileMessageRpc.validateSMS(mobile, msgCode).flatMap(aBoolean -> {
//                if (!aBoolean) {
//                    return Mono.just(failed(BusinessError.MOBILE_CODE_ERROR));
//                } else {
//                    return customerRpc.findCustomerByMobile(mobile).flatMap(customerBo -> {
//                        //判断此手机是否可以注册
//                        if (null != customerBo) {
//                            log.info("{},此手机号在系统中已经存在", mobile);
//                            return Mono.just(failed(BusinessError.MOBILE_ALREADY_EXIST_ERROR));
//                        } else {
//                            CustomerBo customerDto = new CustomerBo();
//                            customerDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
//                            customerDto.setCreateTime(new Date());
//                            customerDto.setEnable(1);
//                            String username = registerReq.getUsername();
//                            String password = registerReq.getPassword();
//                            customerDto.setUsername(username);
//                            customerDto.setPassword(password);
//
//                            //注册
//                            return customerRpc.register(customerDto).map(this::succeed);
//                        }
//                    });
//                }
//            });
//        });
//    }


//    @GetMapping(value = "/common/SSOLogout.do")
//    public ResponseEntity<JSONResponse> logout(@NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
//        return ResponseEntity.ok(succeed(customerRpc.logout(jwt)));
//    }
//
//
//    @GetMapping(value = "/common/showCustomerInfo.do")
//    public ResponseEntity<JSONResponse> showCustomerInfo(@NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
//        ResultDto<CustomerBo> customerDtoResultDto = customerRpc.findOne(jwt);
//        if (!customerDtoResultDto.isStatus()) {
//            return ResponseEntity.ok(failed(BusinessError.COMMON_ERROR));
//        }
//
//        CustomerBo customerDto = customerDtoResultDto.getObject();
//        CustomerVo customerVo = new CustomerVo();
//        BeanUtils.copyProperties(customerDto, customerVo);
//        customerVo.setToken(jwt);
//        ResultDto<ImgBo> imgDtoResultDto = imageRpc.findOne(customerDto.getEmail());
//        if (imgDtoResultDto.isStatus()) {
//            customerVo.setImage(imgDtoResultDto.getObject().getImgUrl());
//        }
//        return ResponseEntity.ok(succeed(customerVo));
//    }
//
//    @GetMapping("/common/kaptchaRender.do")
//    public ResponseEntity<String> kaptchaRender() throws JsonException {
//        String kaptcha = RandomUtil.getRandom(6) + "";
//        redisService.set("kaptcha" + kaptcha, 1);
//        return ResponseEntity.ok(kaptcha);
//    }
//
//
//    @PostMapping("/common/changePersonInfo.do")
//    public ResponseEntity<JSONResponse> changePersonInfo(@NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
//        ResultDto<CustomerBo> resultDto = customerRpc.findOne(jwt);
//        return null;
//    }
//
//
//    @GetMapping("/testMessage.do")
//    public ResponseEntity<String> testMessage() {
//        customerRpc.testMessage();
//        return ResponseEntity.ok("ok");
//    }


}
