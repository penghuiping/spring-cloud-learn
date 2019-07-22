package com.php25.notifymicroservice.client.rpc;

import com.php25.common.flux.BooleanRes;
import com.php25.notifymicroservice.client.bo.Pair;
import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 11:19
 * @Description:
 */
public interface MailRpc {

    /**
     * 发送简单邮件
     */
    public Mono<BooleanRes> sendSimpleMail(@Valid Mono<SendSimpleMailReq> sendSimpleMailReqMono);

    /**
     * 发送简单邮件+附件
     */
    public Mono<BooleanRes> sendAttachmentsMail(@Valid Mono<SendAttachmentsMailReq> sendAttachmentsMailReqMono);

}
