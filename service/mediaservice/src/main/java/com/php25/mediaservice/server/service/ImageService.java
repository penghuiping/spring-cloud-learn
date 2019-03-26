package com.php25.mediaservice.server.service;

import com.php25.common.core.service.BaseService;
import com.php25.common.core.service.SoftDeletable;
import com.php25.mediaservice.client.bo.ImgDto;
import com.php25.mediaservice.server.model.Img;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageService extends BaseService<ImgDto, Img, String>, SoftDeletable<ImgDto> {

    /**
     * 保存base64Image图片，并返回保存的文件名
     *
     * @param base64Image
     * @return
     */
    public String save(String base64Image);
}
