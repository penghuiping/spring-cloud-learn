package com.php25.notifymicroservice.client.bo.req;

import com.php25.notifymicroservice.client.bo.Pair;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author: penghuiping
 * @date: 2019/7/19 13:17
 * @description:
 */
@Getter
@Setter
public class SendAttachmentsMailReq {

    @NotBlank(message = "邮箱收件人地址不能为空")
    private String sendTo;

    @NotBlank(message = "邮件标题不能为空")
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @Size(min = 1)
    private List<Pair<String, String>> attachments;
}
