package com.php25.notifymicroservice.server.vo.req;

import com.php25.common.validation.annotation.Email;
import com.php25.notifymicroservice.server.vo.PairVo;
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

    @Email
    private String sendTo;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    @Size(min = 1)
    private List<PairVo<String,String>> attachments;
}
