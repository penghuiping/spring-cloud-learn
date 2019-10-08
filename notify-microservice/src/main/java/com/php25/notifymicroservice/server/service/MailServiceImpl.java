package com.php25.notifymicroservice.server.service;

import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.util.DigestUtil;
import com.php25.notifymicroservice.server.dto.PairDto;
import com.php25.notifymicroservice.server.dto.SendAttachmentsMailDto;
import com.php25.notifymicroservice.server.dto.SendSimpleMailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2018/7/19 12:56
 * @description:
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Mono<Boolean> sendSimpleMail(SendSimpleMailDto sendSimpleMailDto) {
        String sendTo = sendSimpleMailDto.getSendTo();
        String title = sendSimpleMailDto.getTitle();
        String content = sendSimpleMailDto.getContent();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        //自己给自己发送邮件
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        return Mono.fromCallable(() -> {
            mailSender.send(message);
            return true;
        }).subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<Boolean> sendAttachmentsMail(SendAttachmentsMailDto sendAttachmentsMailDto) {
        return Mono.fromCallable(() -> {
            List<PairDto<String, File>> pairs = sendAttachmentsMailDto.getAttachments().stream().map(stringStringPairBo -> {
                Path path = Paths.get(System.getProperty("java.io.tmpdir"), stringStringPairBo.getKey());
                try {
                    path = Files.write(path, DigestUtil.decodeBase64(stringStringPairBo.getValue()));
                    PairDto<String, File> pairDto = new PairDto<>();
                    pairDto.setKey(stringStringPairBo.getKey());
                    pairDto.setValue(path.toFile());
                    return pairDto;
                } catch (IOException e) {
                    throw Exceptions.throwIllegalStateException("存储邮箱附件失败", e);
                }
            }).collect(Collectors.toList());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                String sendTo = sendAttachmentsMailDto.getSendTo();
                String title = sendAttachmentsMailDto.getTitle();
                String content = sendAttachmentsMailDto.getContent();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(sender);
                helper.setTo(sendTo);
                helper.setSubject(title);
                helper.setText(content);

                for (PairDto<String, File> pair : pairs) {
                    helper.addAttachment(pair.getKey(), new FileSystemResource(pair.getValue()));
                }
                mailSender.send(mimeMessage);
            } catch (Exception e) {
                throw Exceptions.throwIllegalStateException("发送邮件失败！", e);
            }
            return true;
        }).subscribeOn(Schedulers.elastic());
    }

}
