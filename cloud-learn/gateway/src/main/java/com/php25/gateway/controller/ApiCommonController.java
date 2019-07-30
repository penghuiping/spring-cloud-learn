package com.php25.gateway.controller;


import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.core.util.RandomUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.JSONController;
import com.php25.common.flux.JSONResponse;
import com.php25.common.redis.RedisService;
import com.php25.gateway.constant.BusinessError;
import com.php25.gateway.vo.CustomerVo;
import com.php25.gateway.vo.req.GetMsgCodeReq;
import com.php25.gateway.vo.req.RegisterReq;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.bo.req.ValidateSMSReq;
import com.php25.notifymicroservice.client.service.MailService;
import com.php25.notifymicroservice.client.service.MobileMessageService;
import com.php25.usermicroservice.client.dto.CustomerDto;
import com.php25.usermicroservice.client.dto.StringDto;
import com.php25.usermicroservice.client.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author penghuiping
 * @date 2018/03/15
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ApiCommonController extends JSONController {

    @Autowired
    private CustomerService customerRpc;

    @Autowired
    private MailService mailRpc;

    @Autowired
    private MobileMessageService mobileMessageRpc;

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
            sendSmsReq.setMobile(params.getMobile());
            return mobileMessageRpc.sendSMS(Mono.just(sendSmsReq))
                    .map(booleanRes -> succeed(booleanRes.getReturnObject()));
        });
    }


    @PostMapping(value = "/common/register.do")
    public Mono<JSONResponse> register(@Valid Mono<RegisterReq> registerReqMono) {

        //效验短信验证码
        var customerBoResMono = registerReqMono.map(registerReq -> {
            String mobile = registerReq.getMobile();
            String msgCode = registerReq.getMsgCode();
            ValidateSMSReq validateSMSReq1 = new ValidateSMSReq();
            validateSMSReq1.setMobile(mobile);
            validateSMSReq1.setMsgCode(msgCode);
            return validateSMSReq1;
        }).flatMap(validateSMSReq -> {
            //效验短信验证码
            return mobileMessageRpc.validateSMS(Mono.just(validateSMSReq)).map(booleanRes -> {
                if (booleanRes.getErrorCode() != ApiErrorCode.ok.value || !booleanRes.getReturnObject()) {
                    throw Exceptions.throwBusinessException(BusinessError.MOBILE_CODE_ERROR);
                } else {
                    return validateSMSReq;
                }
            });
        }).flatMap(validateSMSReq -> {
            //根据手机号查询用户信息
            String mobile = validateSMSReq.getMobile();
            StringDto stringBo = new StringDto();
            stringBo.setContent(mobile);
            return customerRpc.findCustomerByMobile(stringBo);
        });


        return customerBoResMono.zipWith(registerReqMono, (customerBoRes, registerReq) -> {
            //判断此手机是否可以注册
            if (customerBoRes.getErrorCode() == ApiErrorCode.ok.value) {
                log.info("{},此手机号在系统中已经存在", registerReq.getMobile());
                throw Exceptions.throwBusinessException(BusinessError.MOBILE_ALREADY_EXIST_ERROR);
            } else {
                CustomerDto customerBo1 = new CustomerDto();
                customerBo1.setId(idGeneratorService.getSnowflakeId().longValue());
                customerBo1.setCreateTime(LocalDateTime.now());
                customerBo1.setEnable(1);
                String username = registerReq.getUsername();
                String password = registerReq.getPassword();
                customerBo1.setUsername(username);
                customerBo1.setPassword(password);
                //注册
                return customerRpc.register(customerBo1);
            }
        }).flatMap(booleanResMono -> booleanResMono.map(booleanRes -> {
            if (booleanRes.getErrorCode() != ApiErrorCode.ok.value) {
                throw Exceptions.throwBusinessException(BusinessError.COMMON_ERROR);
            } else {
                return succeed(booleanRes.getReturnObject());
            }
        }));


    }


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


    @GetMapping(value = "/common/showCustomerInfo.do")
    public Mono<JSONResponse> showCustomerInfo(@NotBlank @RequestHeader(name = "jwt") String jwt,
                                               @NotBlank @RequestHeader(name = "username") String username) {
        StringDto idStringReq = new StringDto();
        idStringReq.setJwt(jwt);
        idStringReq.setContent(username);

        return customerRpc.findCustomerByUsername(idStringReq).flatMap(customerBoRes -> {
            if (customerBoRes.getErrorCode() != ApiErrorCode.ok.value) {
                return Mono.just(failed(BusinessError.COMMON_ERROR));
            } else {
                CustomerDto customerBo = customerBoRes.getReturnObject();
                CustomerVo customerVo = new CustomerVo();
                BeanUtils.copyProperties(customerBo, customerVo);
                customerVo.setToken(jwt);

                IdStringReq idStringReq1 = new IdStringReq();
                idStringReq1.setId(customerBo.getImageId());

                return imageRpc.findOne(Mono.just(idStringReq1)).map(imgBoRes -> {
                    if (imgBoRes.getErrorCode() == ApiErrorCode.ok.value) {
                        customerVo.setImage(imgBoRes.getReturnObject().getImgUrl());
                    }
                    return succeed(customerVo);
                });
            }
        });
    }

    @GetMapping(value = "/common/kaptchaRender.do")
    public Mono<JSONResponse> kaptchaRender() {
        return Mono.fromCallable(() -> {
            String kaptcha = RandomUtil.getRandomNumbers(6) + "";
            redisService.set("kaptcha" + kaptcha, 1);
            return succeed(kaptcha);
        });
    }
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
