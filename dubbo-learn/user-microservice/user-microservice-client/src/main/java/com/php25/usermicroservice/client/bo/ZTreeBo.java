package com.php25.usermicroservice.client.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghuiping on 2016/12/22.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZTreeBo implements Serializable {
    private Long id;

    private Long parentId;

    private String name;

    private List<ZTreeBo> children;

    private boolean checked;

    private boolean isParent;
}

