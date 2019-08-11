package com.php25.usermicroservice.client.dto.req;

import com.php25.common.flux.web.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by penghuiping on 16/9/2.
 */
@Setter
@Getter
public class ReqCustomerDto extends BaseDto implements Serializable {

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机
     */
    @NotBlank
    private String mobile;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 邮箱
     */
    private String email;


    private Set<Long> roles;

    private Integer enable;
}
