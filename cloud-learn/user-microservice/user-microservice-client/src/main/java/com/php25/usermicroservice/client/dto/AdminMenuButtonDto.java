package com.php25.usermicroservice.client.dto;

import com.php25.common.flux.web.BaseDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author penghuiping
 * @data 2016/2/23.
 */
@Setter
@Getter
public class AdminMenuButtonDto extends BaseDto implements Serializable {
    private Long id;

    private String name;

    private Long parentId;

    private Boolean isLeaf;

    private Integer sort;

    private Integer enable;

    private Boolean isMenu;

    private String description;

}
