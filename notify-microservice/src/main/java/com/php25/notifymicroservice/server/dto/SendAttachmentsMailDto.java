package com.php25.notifymicroservice.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:17
 * @description:
 */
@Getter
@Setter
public class SendAttachmentsMailDto {

    private String sendTo;

    private String title;

    private String content;

    private List<PairDto<String, String>> attachments;
}
