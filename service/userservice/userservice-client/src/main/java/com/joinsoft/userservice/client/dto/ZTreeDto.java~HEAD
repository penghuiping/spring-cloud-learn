package com.joinsoft.userservice.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * Created by penghuiping on 2016/12/22.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZTreeDto implements Serializable {
    private String id;
    private String parentId;
    private String name;
    private List<ZTreeDto> children;
    private boolean checked;
    private boolean isParent;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

