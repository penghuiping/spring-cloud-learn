package com.php25.userservice.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghuiping on 2016/12/22.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZTreeDto implements Serializable {
    private Long id;
    private Long parentId;
    private String name;
    private List<ZTreeDto> children;
    private boolean checked;
    private boolean isParent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ZTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ZTreeDto> children) {
        this.children = children;
    }

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

