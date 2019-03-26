package com.php25.notifyservice.server.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Lists;
import com.php25.notifyservice.client.bo.PairBo;
import com.php25.notifyservice.client.rpc.MailRpc;
import com.php25.notifyservice.server.service.MailService;
import com.php25.notifyservice.server.service.dto.PairDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void sendAttachmentsMail(String sendTo, String title, String content, List<PairBo<String, File>> attachments) {
        if(null == attachments) attachments = Lists.newArrayList();
        List<PairDto<String, File>> pairDtos = attachments.stream().map(stringFilePairBo -> {
            PairDto<String,File> pairDto = new PairDto<>();
            BeanUtils.copyProperties(stringFilePairBo, pairDto);
            return pairDto;
        }).collect(Collectors.toList());
        mailService.sendAttachmentsMail(sendTo, title, content, pairDtos);
    }

    @Override
    public void sendTemplateMail(String sendTo, String title, Map<String, Object> content, List<PairBo<String, File>> attachments) {
        if(null == attachments) attachments = Lists.newArrayList();
        List<PairDto<String, File>> pairDtos = attachments.stream().map(stringFilePairBo -> {
            PairDto<String,File> pairDto = new PairDto<>();
            BeanUtils.copyProperties(stringFilePairBo, pairDto);
            return pairDto;
        }).collect(Collectors.toList());
        mailService.sendTemplateMail(sendTo, title, content, pairDtos);
    }
}
