package com.php25.authserver.controller;

import com.php25.authserver.constant.BusinessError;
import com.php25.authserver.vo.CustomerRes;
import com.php25.authserver.vo.GetMsgCodeReq;
import com.php25.authserver.vo.RegisterReq;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.core.util.JsonUtil;
import com.php25.common.flux.web.ApiErrorCode;
import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.common.flux.web.ReqIdString;
import com.php25.mediamicroservice.client.service.ImageService;
import com.php25.notifymicroservice.client.bo.req.SendSMSReq;
import com.php25.notifymicroservice.client.bo.req.ValidateSMSReq;
import com.php25.notifymicroservice.client.service.MobileMessageService;
import com.php25.usermicroservice.client.dto.req.ReqStringDto;
import com.php25.usermicroservice.client.dto.res.CustomerDto;
import com.php25.usermicroservice.client.dto.res.ResCustomerDto;
import com.php25.usermicroservice.client.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
 * @author: penghuiping
 * @date: 2019/7/30 14:55
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends JSONController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MobileMessageService mobileMessageService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @RequestMapping("/callback")
    public ResponseEntity callback(String code) {
        return ResponseEntity.ok(code);
    }

    @GetMapping(value = "/SSOLogout.do")
    public ResponseEntity<JSONResponse> logout(@NotBlank @RequestHeader(name = "token") String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return ResponseEntity.ok(succeed(true));
    }


    @GetMapping(value = "/showCustomerInfo.do")
    public Mono<JSONResponse> showCustomerInfo(@NotBlank @RequestHeader(name = "username") String username) {
        ReqStringDto idStringReq = new ReqStringDto();
        idStringReq.setContent(username);

        //查询出客户信息
        Mono<CustomerRes> customerVoMono = Mono.just(idStringReq)
                .flatMap(stringDto -> customerService.findCustomerByUsername(stringDto))
                .map(customerDtoRes -> {
                    log.info("showCustomerInfo...:{}", JsonUtil.toJson(customerDtoRes));
                    if (!customerDtoRes.getErrorCode().equals(ApiErrorCode.ok.value)) {
                        throw Exceptions.throwBusinessException(customerDtoRes.getErrorCode(), customerDtoRes.getMessage());
                    } else {
                        CustomerDto customerDto = customerDtoRes.getReturnObject();
                        CustomerRes customerVo = new CustomerRes();
                        BeanUtils.copyProperties(customerDto, customerVo);
                        customerVo.setImage(customerDto.getImageId());
                        return customerVo;
                    }
                });


        //查询出客户对应的图片
        Mono<String> imageMono = customerVoMono.flatMap(customerVo -> {
            String imageId = customerVo.getImage();
            ReqIdString idStringReq1 = new ReqIdString();
            idStringReq1.setId(imageId);
            return imageService.findOne(idStringReq1);
        }).map(imgBoRes -> {
            log.info("图片信息:{}", JsonUtil.toJson(imgBoRes));
            if (imgBoRes.getErrorCode().equals(ApiErrorCode.ok.value)) {
                return imgBoRes.getReturnObject().getImgUrl();
            } else {
                return "";
            }
        });

        //最后组合返回
        return imageMono.zipWith(customerVoMono).map(objects -> {
            CustomerRes customerVo = objects.getT2();
            String imageUrl = objects.getT1();
            customerVo.setImage(imageUrl);
            return customerVo;
        }).map(this::succeed);
    }


    @PostMapping("/getMsgCode.do")
    public Mono<JSONResponse> getMsgCode(@Valid GetMsgCodeReq getMsgCodeVo) {
        return Mono.just(getMsgCodeVo).flatMap(params -> {
            log.info("获取短信验证码。。。。");
            log.info(JsonUtil.toPrettyJson(params));
            var sendSmsReq = new SendSMSReq();
            sendSmsReq.setMobile(params.getMobile());
            return mobileMessageService.sendSMS(sendSmsReq)
                    .map(booleanRes -> succeed(booleanRes.getReturnObject()));
        });
    }


    @PostMapping(value = "/register.do")
    public Mono<JSONResponse> register(@Valid RegisterReq registerReq1) {
        //效验短信验证码
        var customerBoResMono = Mono.just(registerReq1).map(registerReq -> {
            String mobile = registerReq.getMobile();
            String msgCode = registerReq.getMsgCode();
            ValidateSMSReq validateSMSReq1 = new ValidateSMSReq();
            validateSMSReq1.setMobile(mobile);
            validateSMSReq1.setMsgCode(msgCode);
            return validateSMSReq1;
        }).flatMap(validateSMSReq -> {
            //效验短信验证码
            return mobileMessageService.validateSMS(Mono.just(validateSMSReq)).map(booleanRes -> {
                if (booleanRes.getErrorCode().equals(ApiErrorCode.ok.value) || !booleanRes.getReturnObject()) {
                    throw Exceptions.throwBusinessException(BusinessError.MOBILE_CODE_ERROR);
                } else {
                    return validateSMSReq;
                }
            });
        }).flatMap(validateSMSReq -> {
            //根据手机号查询用户信息
            String mobile = validateSMSReq.getMobile();
            ReqStringDto stringBo = new ReqStringDto();
            stringBo.setContent(mobile);
            return customerService.findCustomerByMobile(stringBo);
        });

        return customerBoResMono.zipWith(Mono.just(registerReq1)).map(tuples -> {
            ResCustomerDto customerDtoRes = tuples.getT1();
            RegisterReq registerReq = tuples.getT2();
            //判断此手机是否可以注册
            if (customerDtoRes.getErrorCode().equals(ApiErrorCode.ok.value)) {
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
                return customerService.register(customerBo1);
            }
        }).flatMap(booleanResMono -> booleanResMono.map(booleanRes -> {
            if (!booleanRes.getErrorCode().equals(ApiErrorCode.ok.value)) {
                throw Exceptions.throwBusinessException(booleanRes.getErrorCode(), booleanRes.getMessage());
            } else {
                return succeed(booleanRes.getReturnObject());
            }
        }));
    }

}
