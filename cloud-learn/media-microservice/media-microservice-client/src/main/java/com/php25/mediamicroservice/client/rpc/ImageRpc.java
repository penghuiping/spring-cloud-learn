package com.php25.mediamicroservice.client.rpc;

import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.common.flux.StringRes;
import com.php25.mediamicroservice.client.bo.req.Base64ImageReq;
import com.php25.mediamicroservice.client.bo.res.ImgRes;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageRpc {

    /**
     * 保存base64Image图片，并返回保存的文件名
     */
    public Mono<StringRes> save(Mono<Base64ImageReq> base64ImageReqMono);

    /**
     * 通过图片id查询图片信息
     */
    public Mono<ImgRes> findOne(Mono<IdStringReq> idStringReqMono);


    /**
     * 批量查询图片信息
     */
    public Flux<ImgRes> findAll(Mono<IdsStringReq> idsStringReqMono);
}
