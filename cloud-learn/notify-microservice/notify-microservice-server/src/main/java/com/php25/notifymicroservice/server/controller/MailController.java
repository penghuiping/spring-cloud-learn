package com.php25.notifymicroservice.server.controller;

import com.php25.common.core.exception.ServiceException;
import com.php25.common.core.util.DigestUtil;
import com.php25.notifymicroservice.client.bo.Pair;
import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import com.php25.notifymicroservice.client.rpc.MailRpc;
import com.php25.notifymicroservice.server.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2018/7/19 12:56
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController implements MailRpc {

    @Autowired
    private MailService mailService;

    @Override
    @PostMapping("/sendSimpleMail")
    public Mono<Boolean> sendSimpleMail(@RequestBody Mono<SendSimpleMailReq> sendSimpleMailReqMono) {
        return sendSimpleMailReqMono.map(params -> {
            mailService.sendSimpleMail(params.getSendTo(), params.getTitle(), params.getContent());
            return true;
        });
    }

    @Override
    @PostMapping(value = "/sendAttachmentsMail")
    public Mono<Boolean> sendAttachmentsMail(@RequestBody Mono<SendAttachmentsMailReq> sendAttachmentsMailReqMono) {
        return sendAttachmentsMailReqMono.map(params -> {
            var pairs = params.getAttachments().stream().map(stringStringPairBo -> {
                var path = Paths.get(System.getProperty("java.io.tmpdir"), stringStringPairBo.getKey());
                try {
                    path = Files.write(path, DigestUtil.decodeBase64(stringStringPairBo.getValue()));
                    Pair<String, File> pairDto = new Pair<>();
                    pairDto.setKey(stringStringPairBo.getKey());
                    pairDto.setValue(path.toFile());
                    return pairDto;
                } catch (IOException e) {
                    throw new ServiceException("存储邮箱附件失败", e);
                }
            }).collect(Collectors.toList());
            mailService.sendAttachmentsMail(params.getSendTo(), params.getTitle(), params.getContent(), pairs);
            return true;
        });
    }

}
