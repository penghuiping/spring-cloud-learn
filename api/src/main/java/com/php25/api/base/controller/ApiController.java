package com.php25.api.base.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.kaptcha.Kaptcha;
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
 * Created by penghuiping on 2018/3/15.
 */
@Slf4j
@Controller
@RequestMapping("/api")
public class ApiController extends JSONController {

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
    @GetMapping("/common/getMsgCode")
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
    @PostMapping(value = "/common/SSOLogin.do")
    public ResponseEntity<JSONResponse> SSSLogin(@RequestParam @NotBlank String mobile, @RequestParam @NotBlank String msgCode) throws JsonException {
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
            "20002=手机号系统不存在", response = Boolean.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "msgCode", value = "短信验证码", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping(value = "/common/register")
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

    /**
     * 登出
     *
     * @return
     * @throws Throwable
     */
    @ApiOperation(value = "登出", notes = "登出", response = Boolean.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Consumer-Username", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @GetMapping(value = "/common/SSOLogout.do")
    public ResponseEntity<JSONResponse> SSOLogout(@NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
        return ResponseEntity.ok(succeed(customerRest.logout(jwt)));
    }

    /**
     * 显示客户信息
     *
     * @return
     * @throws Throwable
     */
    @ApiOperation(value = "查询客户信息", notes = "查询客户信息", response = CustomerVo.class, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Consumer-Username", value = "用户的jwt-token", required = true, dataType = "String", paramType = "header"),
    })
    @GetMapping(value = "/common/showCustomerInfo.do")
    public ResponseEntity<JSONResponse> showCustomerInfo(@NotBlank @NotBlank @RequestHeader(name = "jwt") String jwt) throws JsonException {
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
    @GetMapping("/common/render.do")
    public void render() throws JsonException {
        kaptcha.render();
    }
}
