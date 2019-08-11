package com.php25.usermicroservice.client.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.php25.common.flux.web.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by penghuiping on 16/9/2.
 */
@Setter
@Getter
public class CustomerDto extends BaseDto implements Serializable {

    private Long id;

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

    /**
     * 图片id
     */
    private String imageId;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Set<String> roles;

    private Integer enable;
}
