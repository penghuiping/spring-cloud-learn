package com.php25.mediamicroservice.server.rpc;

import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.util.AssertUtil;
import com.php25.common.core.util.StringUtil;
import com.php25.mediamicroservice.client.bo.ImgBo;
import com.php25.mediaservice.client.bo.ImgDto;
import com.php25.mediamicroservice.client.rpc.ImageRpc;
import com.php25.mediaservice.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/1/4 13:12
 * @description:
 */
@Slf4j
@com.alibaba.dubbo.config.annotation.Service
public class ImageRpcImpl implements ImageRpc {

    @Autowired
    private ImageService imageService;

    @Override
    public ResultDto<String> save(String base64Image) {
        AssertUtil.hasText(base64Image, "base64Image parameter can't be empty");
        String fileName = imageService.save(base64Image);
        if (!StringUtil.isBlank(fileName)) {
            return new ResultDto<>(true, fileName);
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public ResultDto<ImgBo> findOne(String imageId) {
        AssertUtil.hasText(imageId, "imageId parameter can't be empty");
        Optional<ImgDto> imgDtoOptional = imageService.findOne(imageId);
        if (imgDtoOptional.isPresent()) {
            ImgBo imgBo = new ImgBo();
            BeanUtils.copyProperties(imgDtoOptional.get(), imgBo);
            return new ResultDto<>(true, imgBo);
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public ResultDto<List<ImgBo>> findAll(List<String> imageIds) {
        AssertUtil.notEmpty(imageIds, "The list of imageIds must contain at least one element");
        Optional<List<ImgDto>> optionalImgDtos = imageService.findAll(imageIds);
        if (optionalImgDtos.isPresent() && !optionalImgDtos.get().isEmpty()) {
            List<ImgBo> imgBos = optionalImgDtos.get().stream().map(imgDto -> {
                ImgBo imgBo = new ImgBo();
                BeanUtils.copyProperties(imgDto, imgBo);
                return imgBo;
            }).collect(Collectors.toList());
            return new ResultDto<>(true, imgBos);
        } else {
            return new ResultDto<>(false, null);
        }
    }
}
