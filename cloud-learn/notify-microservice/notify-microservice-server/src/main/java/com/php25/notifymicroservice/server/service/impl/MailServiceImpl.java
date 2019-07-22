package com.php25.notifymicroservice.server.service.impl;

import com.php25.common.core.exception.ServiceException;
import com.php25.notifymicroservice.client.bo.Pair;
import com.php25.notifymicroservice.server.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 10:06
 * @Description:
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleMail(String sendTo, String title, String content) {
        var message = new SimpleMailMessage();
        message.setFrom(sender);
        //自己给自己发送邮件
        message.setTo(sendTo);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String sendTo, String title, String content, List<Pair<String, File>> attachments) {
        var mimeMessage = mailSender.createMimeMessage();
        try {
            var helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(sendTo);
            helper.setSubject(title);
            helper.setText(content);

            for (var pair : attachments) {
                helper.addAttachment(pair.getKey(), new FileSystemResource(pair.getValue()));
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ServiceException("发送邮件失败！", e);
        }
    }

    @Override
    public void sendTemplateMail(String sendTo, String title, Map<String, Object> content, List<Pair<String, File>> attachments) {
        var mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(sendTo);
            helper.setSubject(title);

            var resourceLoader = new ClasspathResourceLoader("/");
            var cfg = Configuration.defaultConfiguration();
            var gt = new GroupTemplate(resourceLoader, cfg);
            var t = gt.getTemplate("mail_template.txt");
            t.binding(content);
            String text = t.render();
            helper.setText(text, true);

            for (var pair : attachments) {
                helper.addAttachment(pair.getKey(), new FileSystemResource(pair.getValue()));
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ServiceException("发送邮件失败！", e);
        }
    }
}
