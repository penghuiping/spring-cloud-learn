package com.php25.notifymicroservice.server.controller;

import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.notifymicroservice.server.dto.PairDto;
import com.php25.notifymicroservice.server.dto.SendAttachmentsMailDto;
import com.php25.notifymicroservice.server.dto.SendSimpleMailDto;
import com.php25.notifymicroservice.server.service.MailService;
import com.php25.notifymicroservice.server.vo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.server.vo.req.SendSimpleMailReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author penghuiping
 * @date 2019/9/12 14:02
 */
@RestController
@RequestMapping("/mail")
public class MailController extends JSONController {

    @Autowired
    private MailService mailService;

    /**
     * 发送简单邮件
     */
    @PostMapping("/sendSimpleMail")
    public JSONResponse sendSimpleMail(@Valid @RequestBody SendSimpleMailReq sendSimpleMailReq) {
        SendSimpleMailDto sendSimpleMailDto = new SendSimpleMailDto();
        BeanUtils.copyProperties(sendSimpleMailReq, sendSimpleMailDto);
        return succeed(mailService.sendSimpleMail(sendSimpleMailDto));
    }

    /**
     * 发送简单邮件+附件
     */
    @PostMapping("/sendAttachmentsMail")
    public JSONResponse sendAttachmentsMail(@Valid @RequestBody SendAttachmentsMailReq sendAttachmentsMailReq) {
        SendAttachmentsMailDto sendAttachmentsMailDto = new SendAttachmentsMailDto();
        BeanUtils.copyProperties(sendAttachmentsMailReq, sendAttachmentsMailDto);
        List<PairDto<String, String>> pairDtoList = sendAttachmentsMailReq.getAttachments().stream().map(stringStringPairVo -> {
            PairDto<String, String> pairDto = new PairDto<>();
            pairDto.setKey(stringStringPairVo.getKey());
            pairDto.setValue(stringStringPairVo.getValue());
            return pairDto;
        }).collect(Collectors.toList());
        sendAttachmentsMailDto.setAttachments(pairDtoList);
        return succeed(mailService.sendAttachmentsMail(sendAttachmentsMailDto));
    }
}
