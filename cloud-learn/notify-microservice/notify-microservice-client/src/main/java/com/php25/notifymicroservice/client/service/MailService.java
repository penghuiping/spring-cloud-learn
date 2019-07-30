package com.php25.notifymicroservice.client.service;

import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import com.php25.notifymicroservice.client.bo.res.BooleanRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 11:19
 * @Description:
 */
public interface MailService {

    /**
     * 发送简单邮件
     */
    public Mono<BooleanRes> sendSimpleMail(@Valid Mono<SendSimpleMailReq> sendSimpleMailReqMono);

    /**
     * 发送简单邮件+附件
     */
    public Mono<BooleanRes> sendAttachmentsMail(@Valid Mono<SendAttachmentsMailReq> sendAttachmentsMailReqMono);

}
