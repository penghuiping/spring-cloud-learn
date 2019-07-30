package com.php25.mediamicroservice.server.service;

import com.google.common.collect.Lists;
import com.j256.simplemagic.ContentInfoUtil;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.util.DigestUtil;
import com.php25.common.flux.ApiErrorCode;
import com.php25.common.flux.IdStringReq;
import com.php25.common.flux.IdsStringReq;
import com.php25.mediamicroservice.client.bo.Base64ImageBo;
import com.php25.mediamicroservice.client.bo.ImgBo;
import com.php25.mediamicroservice.client.bo.res.ImgBoListRes;
import com.php25.mediamicroservice.client.bo.res.ImgBoRes;
import com.php25.mediamicroservice.client.bo.res.StringRes;
import com.php25.mediamicroservice.client.service.ImageService;
import com.php25.mediamicroservice.server.model.Img;
import com.php25.mediamicroservice.server.repository.ImgRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
public class ImageServiceImpl implements ImageService {

    @Value("${base_assets_upload_path}")
    private String resourcePath;

    @Value("${base_assets_upload_url}")
    private String resourceUrl;

    @Autowired
    private ImgRepository imgRepository;

    @Override
    @PostMapping("/save")
    public Mono<StringRes> save(@RequestBody Base64ImageBo base64ImageReq1) {
        return Mono.just(base64ImageReq1).map(base64ImageReq -> {
            String base64Image = base64ImageReq.getContent();
            //获取文件类型
            var arr = DigestUtil.decodeBase64(base64Image);
            var util = new ContentInfoUtil();
            var info = util.findMatch(arr);
            String name = DigestUtil.SHAStr(base64Image) + "." + info.getFileExtensions()[0];
            String absolutePath = resourcePath + "/" + name;

            //写入文件
            var path1 = Paths.get(absolutePath);
            try {
                if (!Files.exists(path1)) {
                    Files.write(path1, arr);
                }
            } catch (IOException e) {
                throw Exceptions.throwIllegalStateException("写入文件出错", e);
            }
            return name;
        }).map(s -> {
            StringRes stringRes = new StringRes();
            stringRes.setErrorCode(ApiErrorCode.ok.value);
            stringRes.setReturnObject(s);
            return stringRes;
        });
    }

    @Override
    @PostMapping("/findOne")
    public Mono<ImgBoRes> findOne(@RequestBody IdStringReq idStringReq1) {
        return Mono.just(idStringReq1).map(idStringReq -> {
            Optional<Img> imgDtoOptional = imgRepository.findById(idStringReq.getId());
            if (imgDtoOptional.isPresent()) {
                ImgBo imgBo = new ImgBo();
                BeanUtils.copyProperties(imgDtoOptional.get(), imgBo);
                return imgBo;
            } else {
                throw Exceptions.throwIllegalStateException(String.format("无法通过%s找到对应的图片", idStringReq.getId()));
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
    public Mono<ImgBoListRes> findAll(@RequestBody IdsStringReq idsStringReq1) {
        return Mono.just(idsStringReq1).map(idsStringReq -> {
            Iterable<Img> imgIterable = imgRepository.findAllById(idsStringReq.getIds());
            List<Img> imgs = Lists.newArrayList(imgIterable);
            if (null != imgs && imgs.size() > 0) {
                return imgs.stream().map(imgDto -> {
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
