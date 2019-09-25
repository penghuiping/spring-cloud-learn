package com.php25.mediamicroservice.server.service;

import com.php25.mediamicroservice.server.dto.ImgDto;
import reactor.core.publisher.Flux;

import java.awt.image.DataBuffer;
import java.nio.file.Path;
import java.util.List;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageService {

    /**
     * 保存base64Image图片，并返回保存的文件名
     */
    public String save(String base64StringContent);


    /**
     * 通过图片id查询图片信息
     */
    public Path findOne(String id);

}
