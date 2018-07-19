package com.php25.notifyservice.server.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.php25.notifyservice.client.dto.PairDto;
import com.php25.notifyservice.client.rpc.MailRpc;
import com.php25.notifyservice.server.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Auther: penghuiping
 * @Date: 2018/7/19 12:56
 * @Description:
 */
@Service
public class MailRpcImpl implements MailRpc {

    @Autowired
    private MailService mailService;

    @Override
    public void sendSimpleMail(String sendTo, String title, String content) {
        mailService.sendSimpleMail(sendTo, title, content);
    }

    @Override
    public void sendAttachmentsMail(String sendTo, String title, String content, List<PairDto<String, File>> attachments) {
        mailService.sendAttachmentsMail(sendTo, title, content, attachments);
    }

    @Override
    public void sendTemplateMail(String sendTo, String title, Map<String, Object> content, List<PairDto<String, File>> attachments) {
        mailService.sendTemplateMail(sendTo, title, content, attachments);
    }
}
