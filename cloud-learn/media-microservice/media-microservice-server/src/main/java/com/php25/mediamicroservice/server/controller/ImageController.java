package com.php25.mediamicroservice.server.controller;

import com.php25.common.flux.ControllerException;
import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.ImgBo;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import com.php25.mediamicroservice.server.dto.ImgDto;
import com.php25.mediamicroservice.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
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
    public Mono<String> save(@Valid Mono<Base64ImageBo> base64ImageReqMono) {
        return base64ImageReqMono.map(base64ImageReq -> {
            return imageService.save(base64ImageReq.getContent());
        });
    }

    @Override
    @PostMapping("/findOne")
    public Mono<ImgBo> findOne(@Valid Mono<IdStringReq> idStringReqMono) {
        return idStringReqMono.map(idStringReq -> {
            Optional<ImgDto> imgDtoOptional = imageService.findOne(idStringReq.getId());
            if (imgDtoOptional.isPresent()) {
                ImgBo imgBo = new ImgBo();
                BeanUtils.copyProperties(imgDtoOptional.get(), imgBo);
                return imgBo;
            } else {
                throw new ControllerException("无法通过" + String.format("无法通过%s找到对应的图片", idStringReqMono) + "找到对应的图片");
            }
        });
    }

    @Override
    @PostMapping("/findAll")
    public Flux<ImgBo> findAll(@Valid Mono<IdsStringReq> idsStringReqMono) {
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
        }).flatMapMany(Flux::fromIterable);
    }
}
