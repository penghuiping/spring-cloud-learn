package com.php25.notifymicroservice.client.rpc;

import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 11:19
 * @Description:
 */
public interface MailRpc {

    /**
     * 发送简单邮件
     */
    public Mono<Boolean> sendSimpleMail(@Valid Mono<SendSimpleMailReq> sendSimpleMailReqMono);

    /**
     * 发送简单邮件+附件
     */
    public Mono<Boolean> sendAttachmentsMail(@Valid Mono<SendAttachmentsMailReq> sendAttachmentsMailReqMono);

}
