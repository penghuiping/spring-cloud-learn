package com.php25.mediamicroservice.server.service;

import com.php25.mediamicroservice.server.dto.ImgDto;

import java.util.List;
import java.util.Optional;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageService {

    /**
     * 保存base64Image图片，并返回保存的文件名
     *
     * @param base64Image
     * @return
     */
    public String save(String base64Image);

    public Optional<ImgDto> findOne(String id);

    public Optional<List<ImgDto>> findAll(List<String> ids);
}
