package com.php25.usermicroservice.client.bo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author penghuiping
 * @data 2016/2/23.
 */
@Setter
@Getter
public class AdminMenuButtonBo implements Serializable {
    private Long id;

    private String name;

    private String url;

    private List<AdminMenuButtonBo> children;

    private AdminMenuButtonBo parent;

    private Long parentId;

    private Boolean isShow = false;

    private Boolean isLeaf;

    private Integer sort;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer enable;

    private Boolean isMenu;

    private String description;

    private String icon;
}
