package com.php25.mediamicroservice.client.service;

import com.php25.common.flux.web.ReqIdString;
import com.php25.common.flux.web.ReqIdsString;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import com.php25.mediamicroservice.client.bo.res.StringRes;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageService {

    /**
     * 保存base64Image图片，并返回保存的文件名
     */
    public Mono<StringRes> save(@Valid Base64ImageBo base64ImageReq);

    /**
     * 通过图片id查询图片信息
     */
    public Mono<ImgBoRes> findOne(@Valid ReqIdString idStringReq);


    /**
     * 批量查询图片信息
     */
    public Mono<ImgBoListRes> findAll(@Valid ReqIdsString idsStringReq);
}
