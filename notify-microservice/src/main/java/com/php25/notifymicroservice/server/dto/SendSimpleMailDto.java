package com.php25.notifymicroservice.server.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author penghuiping
 * @date 2019/9/12 13:49
 */
@Setter
@Getter
public class SendSimpleMailDto {

    /**
     * 收件人邮箱
     **/
    private String sendTo;

    /**
     * 邮件标题
     **/
    private String title;

    /**
     * 邮件内容
     **/
    private String content;
}
