package com.php25.api.base.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.kaptcha.Kaptcha;
import com.baomidou.kaptcha.exception.KaptchaIncorrectException;
import com.baomidou.kaptcha.exception.KaptchaNotFoundException;
import com.php25.api.base.constant.BusinessError;
import com.php25.api.base.vo.CustomerVo;
import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.service.IdGeneratorService;
import com.php25.common.mvc.JSONController;
import com.php25.common.mvc.JSONResponse;
import com.php25.common.mvc.JsonException;
import com.php25.mediaservice.client.dto.ImgDto;
import com.php25.mediaservice.client.rpc.ImageRpc;
import com.php25.notifyservice.client.rpc.MailRpc;
import com.php25.notifyservice.client.rpc.MobileMessageRpc;
import com.php25.userservice.client.dto.CustomerDto;
import com.php25.userservice.client.rpc.CustomerRpc;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author penghuiping
 * @date 2018/03/15
 */
@Slf4j
@Controller
@RequestMapping("/api")
public class ApiCommonController extends JSONController {

    @Reference(check = false)
    private CustomerRpc customerRest;

    @Reference(check = false)
    private MailRpc mailRpc;

    @Reference(check = false)
    private MobileMessageRpc mobileMessageRpc;

    @Reference(check = false)
    private ImageRpc imageRpc;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private Kaptcha kaptcha;


    @ApiOperation(value = "获取短信验证码", notes = "获取短信验证码", response = Boolean.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/common/getMsgCode.do")
    public ResponseEntity<JSONResponse> getMsgCode(@NotBlank String mobile) throws JsonException {
        mobileMessageRpc.sendSMS(mobile);
        return ResponseEntity.ok(succeed(true));
    }


    @ApiOperation(value = "通过手机验证码登入", notes = "输入手机号与手机验证码登入</br>" +
            "错误码:<br>" +
            "10000=出错啦,请重试<br>" +
            "20001=短信验证码不正确<br>" +
            "20002=手机号系统不存在", response = CustomerVo.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "msgCode", value = "短信验证码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = "/common/loginByMobile.do")
    public ResponseEntity<JSONResponse> loginByMobile(@RequestParam @NotBlank String mobile, @RequestParam @NotBlank String msgCode) throws JsonException {
        //效验短信验证码
        if (!mobileMessageRpc.validateSMS(mobile, msgCode)) {
            return ResponseEntity.ok(failed(BusinessError.MOBILE_CODE_ERROR));
        }

        ResultDto<String> resultDto = customerRest.loginByMobile(mobile);
        if (!resultDto.isStatus()) {
            log.info("无法找到相应的手机号对应的用户记录,手机号为{}", mobile);
            return ResponseEntity.ok(failed(BusinessError.MOBILE_NOT_EXIST_ERROR));
        } else {
            String jwt = resultDto.getObject();
            ResultDto<CustomerDto> customerDtoResultDto = customerRest.findOne(jwt);
            if (customerDtoResultDto.isStatus()) {
                CustomerDto customerDto = customerDtoResultDto.getObject();
                CustomerVo customerVo = new CustomerVo();
                BeanUtils.copyProperties(customerDto, customerVo);
                //获取头像
//                ResultDto<ImgDto> imgDtoResultDto = imageRpc.findOne(customerDto.getImageId());
//                if (imgDtoResultDto.isStatus()) {
//                    customerVo.setImage(imgDtoResultDto.getObject().getImgUrl());
//                }
                customerVo.setToken(jwt);
                return ResponseEntity.ok(succeed(customerVo));
            } else {
                return ResponseEntity.ok(failed(BusinessError.COMMON_ERROR));
            }
        }
    }

    @ApiOperation(value = "通过用户名与密码登入", notes = "输入用户名与密码登入</br>" +
            "错误码:<br>" +
            "10000=出错啦,请重试<br>" +
            "20000=图形验证码不正确<br>" +
            "20004=用户名与密码不匹配<br>", response = CustomerVo.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "kaptchaCode", value = "图形验证码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = "/common/loginByUsername.do")
    public ResponseEntity<JSONResponse> loginByUsername(@RequestParam @NotBlank String username, @RequestParam @NotBlank String password, @NotBlank String kaptchaCode) throws JsonException {
        //验证图形验证码
        try {
            if (!kaptcha.validate(kaptchaCode)) {
                return ResponseEntity.ok(failed(BusinessError.KAPTCHA_ERROR));
            }
        }catch (KaptchaNotFoundException | KaptchaIncorrectException e) {
            return ResponseEntity.ok(failed(BusinessError.KAPTCHA_ERROR));
        }

        ResultDto<String> resultDto = customerRest.loginByUsername(username, password);
        if (!resultDto.isStatus()) {
            return ResponseEntity.ok(failed(BusinessError.USERNAME_PASSWORD_ERROR));
        } else {
            String jwt = resultDto.getObject();
            ResultDto<CustomerDto> customerDtoResultDto = customerRest.findOne(jwt);
            if (customerDtoResultDto.isStatus()) {
                CustomerDto customerDto = customerDtoResultDto.getObject();
                CustomerVo customerVo = new CustomerVo();
                BeanUtils.copyProperties(customerDto, customerVo);
                //获取头像
                ResultDto<ImgDto> imgDtoResultDto = imageRpc.findOne(customerDto.getImageId());
                if (imgDtoResultDto.isStatus()) {
                    customerVo.setImage(imgDtoResultDto.getObject().getImgUrl());
                }
                customerVo.setToken(jwt);
                return ResponseEntity.ok(succeed(customerVo));
            } else {
                return ResponseEntity.ok(failed(BusinessError.COMMON_ERROR));
            }
        }
    }


    @ApiOperation(value = "注册", notes = "用户注册</br>" +
            "错误码:<br>" +
            "10000=出错啦,请重试<br>" +
            "20001=短信验证码不正确<br>" +
            "20003=此手机号系统已存在", response = Boolean.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "msgCode", value = "短信验证码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = "/common/register.do")
    public ResponseEntity<JSONResponse> register(@NotBlank String username,
                                                 @NotBlank String password,
                                                 @NotBlank String mobile,
                                                 @NotBlank String msgCode) throws JsonException {
        //效验短信验证码
        if (!mobileMessageRpc.validateSMS(mobile, msgCode)) {
            return ResponseEntity.ok(failed(BusinessError.MOBILE_CODE_ERROR));
        }

        //判断此手机是否可以注册
        ResultDto<CustomerDto> customerDtoResultDto = customerRest.findCustomerDtoByMobile(mobile);
        if (customerDtoResultDto.isStatus()) {
            log.info("{},此手机号在系统中已经存在", mobile);
            return ResponseEntity.ok(failed(BusinessError.MOBILE_ALREADY_EXIST_ERROR));
        }

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(idGeneratorService.getModelPrimaryKeyNumber().longValue());
        customerDto.setCreateTime(new Date());
        customerDto.setEnable(1);
        customerDto.setUsername(username);
        customerDto.setPassword(password);

        //注册
        Boolean result = customerRest.register(customerDto);
        return ResponseEntity.ok(succeed(result));
    }


    @ApiOperation(value = "登出", notes = "登出", response = Boolean.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jwt", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @GetMapping(value = "/common/SSOLogout.do")
    public ResponseEntity<JSONResponse> logout(@NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
        return ResponseEntity.ok(succeed(customerRest.logout(jwt)));
    }


    @ApiOperation(value = "查询客户信息", notes = "查询客户信息<br>" +
            "错误状态码:<br>" +
            "10000=出错啦,请重试", response = CustomerVo.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jwt", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @GetMapping(value = "/common/showCustomerInfo.do")
    public ResponseEntity<JSONResponse> showCustomerInfo(@NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
        ResultDto<CustomerDto> customerDtoResultDto = customerRest.findOne(jwt);
        if (!customerDtoResultDto.isStatus()) {
            return ResponseEntity.ok(failed(BusinessError.COMMON_ERROR));
        }

        CustomerDto customerDto = customerDtoResultDto.getObject();
        CustomerVo customerVo = new CustomerVo();
        BeanUtils.copyProperties(customerDto, customerVo);
        customerVo.setToken(jwt);
        ResultDto<ImgDto> imgDtoResultDto = imageRpc.findOne(customerDto.getEmail());
        if (imgDtoResultDto.isStatus()) {
            customerVo.setImage(imgDtoResultDto.getObject().getImgUrl());
        }
        return ResponseEntity.ok(succeed(customerVo));
    }

    @ApiOperation(value = "获取图形验证码", notes = "获取图形验证码", produces = MediaType.IMAGE_JPEG_VALUE)
    @GetMapping("/common/kaptchaRender.do")
    public void kaptchaRender() throws JsonException {
        kaptcha.render();
    }


    @ApiOperation(value = "修改个人信息", notes = "修改个人信息", response = Boolean.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jwt", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "nickname", value = "昵称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "image", value = "base64图片", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/common/changePersonInfo.do")
    public ResponseEntity<JSONResponse> changePersonInfo(@NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
        ResultDto<CustomerDto> resultDto = customerRest.findOne(jwt);
        return null;
    }


}
