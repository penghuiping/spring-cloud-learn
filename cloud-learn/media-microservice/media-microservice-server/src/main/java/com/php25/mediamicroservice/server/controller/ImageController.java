package com.php25.mediamicroservice.server.controller;

import com.php25.common.core.exception.Exceptions;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.ImgBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import com.php25.mediamicroservice.server.dto.ImgDto;
import com.php25.mediamicroservice.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/4 13:12
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/img")
public class ImageController implements ImageRpc {

    @Autowired
    private ImageService imageService;

    @Override
    @PostMapping("/save")
    public Mono<String> save(@RequestBody Mono<Base64ImageBo> base64ImageReqMono) {
        return base64ImageReqMono.map(base64ImageReq -> imageService.save(base64ImageReq.getContent()));
    }

    @Override
    @PostMapping("/findOne")
    public Mono<ImgBoRes> findOne(@RequestBody Mono<IdStringReq> idStringReqMono) {
        return idStringReqMono.map(idStringReq -> {
            Optional<ImgDto> imgDtoOptional = imageService.findOne(idStringReq.getId());
            if (imgDtoOptional.isPresent()) {
                ImgBo imgBo = new ImgBo();
                BeanUtils.copyProperties(imgDtoOptional.get(), imgBo);
                return imgBo;
            } else {
                throw Exceptions.throwIllegalStateException(String.format("无法通过%s找到对应的图片", idStringReqMono));
            }
        }).map(imgBo -> {
            ImgBoRes imgBoRes = new ImgBoRes();
            imgBoRes.setErrorCode(ApiErrorCode.ok.value);
            imgBoRes.setReturnObject(imgBo);
            return imgBoRes;
        });
    }

    @Override
    @PostMapping("/findAll")
    public Mono<ImgBoListRes> findAll(@RequestBody Mono<IdsStringReq> idsStringReqMono) {
        return idsStringReqMono.map(idsStringReq -> {
            Optional<List<ImgDto>> optionalImgDtos = imageService.findAll(idsStringReq.getIds());
            if (optionalImgDtos.isPresent() && !optionalImgDtos.get().isEmpty()) {
                return optionalImgDtos.get().stream().map(imgDto -> {
                    ImgBo imgBo = new ImgBo();
                    BeanUtils.copyProperties(imgDto, imgBo);
                    return imgBo;
                }).collect(Collectors.toList());
            } else {
                return new ArrayList<ImgBo>();
            }
        }).map(imgBos -> {
            ImgBoListRes imgBoListRes = new ImgBoListRes();
            imgBoListRes.setErrorCode(ApiErrorCode.ok.value);
            imgBoListRes.setReturnObject(imgBos);
            return imgBoListRes;
        });
    }
}
