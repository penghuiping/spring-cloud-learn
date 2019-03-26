package com.php25.notifyservice.server.service.impl;

import com.php25.notifyservice.server.service.MailService;
import com.php25.notifyservice.server.service.dto.PairDto;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
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
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(sendTo); //自己给自己发送邮件
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String sendTo, String title, String content, List<PairDto<String, File>> attachments) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(sendTo);
            helper.setSubject(title);
            helper.setText(content);

            for (PairDto<String, File> pair : attachments) {
                helper.addAttachment(pair.getKey(), new FileSystemResource(pair.getValue()));
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("发送邮件失败！", e);
            throw new RuntimeException("发送邮件失败！", e);
        }
    }

    @Override
    public void sendTemplateMail(String sendTo, String title, Map<String, Object> content, List<PairDto<String, File>> attachments) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(sendTo);
            helper.setSubject(title);

            ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/");
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            Template t = gt.getTemplate("mail_template.txt");
            t.binding(content);
            String text = t.render();
            helper.setText(text, true);

            for (PairDto<String, File> pair : attachments) {
                helper.addAttachment(pair.getKey(), new FileSystemResource(pair.getValue()));
            }
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("发送邮件失败！", e);
            throw new RuntimeException("发送邮件失败！", e);
        }
    }
}
