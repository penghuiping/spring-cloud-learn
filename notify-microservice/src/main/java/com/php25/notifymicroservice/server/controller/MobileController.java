package com.php25.notifymicroservice.server.controller;

import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.notifymicroservice.server.dto.SendSMSDto;
import com.php25.notifymicroservice.server.dto.ValidateSMSDto;
import com.php25.notifymicroservice.server.service.MobileMessageService;
import com.php25.notifymicroservice.server.vo.req.SendSMSReq;
import com.php25.notifymicroservice.server.vo.req.ValidateSMSReq;
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
 * @date 2019/9/12 14:02
 */
@RestController
@RequestMapping("/mobile")
public class MobileController extends JSONController {

    @Autowired
    private MobileMessageService mobileMessageService;

    /**
     * 插入\修改一条验证码信息
     */
    @PostMapping("/sendSMS")
    public Mono<JSONResponse> sendSMS(@Valid @RequestBody SendSMSReq sendSMSReq) {
        SendSMSDto sendSMSDto = new SendSMSDto();
        BeanUtils.copyProperties(sendSMSReq, sendSMSDto);
        return mobileMessageService.sendSMS(sendSMSDto).map(this::succeed);
    }

    /**
     * 通过电话号码查询有效验证码数据
     */
    @PostMapping("/validateSMS")
    public Mono<JSONResponse> validateSMS(@Valid @RequestBody ValidateSMSReq validateSMSReq) {
        ValidateSMSDto validateSMSDto = new ValidateSMSDto();
        BeanUtils.copyProperties(validateSMSReq, validateSMSDto);
        return mobileMessageService.validateSMS(validateSMSDto).map(this::succeed);
    }

}
