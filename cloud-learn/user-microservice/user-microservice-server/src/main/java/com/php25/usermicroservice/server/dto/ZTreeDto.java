package com.php25.usermicroservice.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghuiping on 2016/12/22.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZTreeDto implements Serializable {
    private Long id;

    private Long parentId;

    private String name;

    private List<ZTreeDto> children;

    private boolean checked;

    private boolean isParent;
}

