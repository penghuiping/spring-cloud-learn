package com.php25.gateway.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author penghuiping
 * @date 2016-09-02
 */
@Getter
@Setter
public class CustomerResp implements Serializable {

    private String id;

    private String username;

    private String email;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    private Integer sex;

    private String nickname;

    private String token;

    private String image;
}
