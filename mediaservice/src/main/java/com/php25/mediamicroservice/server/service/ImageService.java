package com.php25.mediamicroservice.server.service;

import reactor.core.publisher.Mono;

import java.nio.file.Path;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageService {

    /**
     * 保存base64Image图片，并返回保存的文件名
     */
    public Mono<String> save(String base64StringContent);


    /**
     * 通过图片id查询图片信息
     */
    public Mono<Path> findOne(String id);

}
