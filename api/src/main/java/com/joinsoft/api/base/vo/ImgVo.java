package com.joinsoft.api.base.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by Zhangbing on 2017/4/6.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImgVo implements Serializable {
    private String id;

    private String imgUrl;//图片url

    private Integer type;//类型

    private Integer enable;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }
}
