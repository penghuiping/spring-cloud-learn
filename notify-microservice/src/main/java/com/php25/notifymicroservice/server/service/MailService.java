package com.php25.notifymicroservice.server.service;

import com.php25.notifymicroservice.server.dto.SendAttachmentsMailDto;
import com.php25.notifymicroservice.server.dto.SendSimpleMailDto;
import reactor.core.publisher.Mono;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 11:19
 * @Description:
 */
public interface MailService {

    /**
     * 发送简单邮件
     */
    public Mono<Boolean> sendSimpleMail(SendSimpleMailDto sendSimpleMailDto);

    /**
     * 发送简单邮件+附件
     */
    public Mono<Boolean> sendAttachmentsMail(SendAttachmentsMailDto sendAttachmentsMailDto);

}
