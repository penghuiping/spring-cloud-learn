package com.php25.notifymicroservice.server.service;

import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.util.DigestUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.notifymicroservice.client.bo.Pair;
import com.php25.notifymicroservice.client.bo.req.SendAttachmentsMailReq;
import com.php25.notifymicroservice.client.bo.req.SendSimpleMailReq;
import com.php25.notifymicroservice.client.bo.res.BooleanRes;
import com.php25.notifymicroservice.client.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    @PostMapping("/sendSimpleMail")
    public Mono<BooleanRes> sendSimpleMail(@RequestBody Mono<SendSimpleMailReq> sendSimpleMailReqMono) {
        return sendSimpleMailReqMono.map(params -> {
            String sendTo = params.getSendTo();
            String title = params.getTitle();
            String content = params.getContent();
            var message = new SimpleMailMessage();
            message.setFrom(sender);
            //自己给自己发送邮件
            message.setTo(sendTo);
            message.setSubject(title);
            message.setText(content);
            mailSender.send(message);
            return true;
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

    @Override
    @PostMapping(value = "/sendAttachmentsMail")
    public Mono<BooleanRes> sendAttachmentsMail(@RequestBody Mono<SendAttachmentsMailReq> sendAttachmentsMailReqMono) {
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
                    throw Exceptions.throwIllegalStateException("存储邮箱附件失败", e);
                }
            }).collect(Collectors.toList());

            var mimeMessage = mailSender.createMimeMessage();
            try {
                String sendTo = params.getSendTo();
                String title = params.getTitle();
                String content = params.getContent();
                var helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(sender);
                helper.setTo(sendTo);
                helper.setSubject(title);
                helper.setText(content);

                for (var pair : pairs) {
                    helper.addAttachment(pair.getKey(), new FileSystemResource(pair.getValue()));
                }
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                throw Exceptions.throwIllegalStateException("发送邮件失败！", e);
            }
            return true;
        }).map(aBoolean -> {
            BooleanRes booleanRes = new BooleanRes();
            booleanRes.setErrorCode(ApiErrorCode.ok.value);
            booleanRes.setReturnObject(aBoolean);
            return booleanRes;
        });
    }

}
