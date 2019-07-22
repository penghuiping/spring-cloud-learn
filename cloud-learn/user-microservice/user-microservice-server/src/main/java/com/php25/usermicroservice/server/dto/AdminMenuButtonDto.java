package com.php25.usermicroservice.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by penghuiping on 2016/2/23.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminMenuButtonDto implements Serializable {
    private Long id;

    private String name;

    private String url;

    private List<AdminMenuButtonDto> children;

    private AdminMenuButtonDto parent;

    private Long parentId;

    private Boolean isShow = false;

    private Boolean isLeaf;

    private Integer sort;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private Integer enable;

    private Boolean isMenu;

    private String description;

    private String icon;
}
