package com.php25.mediamicroservice.client.bo.res;

import lombok.Data;

import java.util.Date;

/**
 * @author: penghuiping
 * @date: 2019/1/4 11:15
 * @description:
 */
@Data
public class ImgBo {

    private String id;

    /**
     * 图片地址 http访问地址
     */
    private String imgUrl;

    /**
     * 图片名
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 0:无效 1:有效 2:软删除
     */
    private Integer enable;


}
