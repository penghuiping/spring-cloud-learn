package com.php25.mediaservice.server.rpc;

import com.php25.common.core.dto.ResultDto;
import com.php25.common.core.util.StringUtil;
import com.php25.mediaservice.client.dto.ImgDto;
import com.php25.mediaservice.client.rpc.ImageRpc;
import com.php25.mediaservice.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

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
        Assert.hasText(base64Image, "base64Image parameter can't be empty");
        String fileName = imageService.save(base64Image);
        if (!StringUtil.isBlank(fileName)) {
            return new ResultDto<>(true, fileName);
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public ResultDto<ImgDto> findOne(String imageId) {
        Assert.hasText(imageId, "imageId parameter can't be empty");
        Optional<ImgDto> imgDtoOptional = imageService.findOne(imageId);
        if (imgDtoOptional.isPresent()) {
            return new ResultDto<>(true, imgDtoOptional.get());
        } else {
            return new ResultDto<>(false, null);
        }
    }

    @Override
    public ResultDto<List<ImgDto>> findAll(List<String> imageIds) {
        Assert.notEmpty(imageIds, "The list of imageIds must contain at least one element");
        Optional<List<ImgDto>> optionalImgDtos = imageService.findAll(imageIds);
        if (optionalImgDtos.isPresent() && !optionalImgDtos.get().isEmpty()) {
            return new ResultDto<>(true, optionalImgDtos.get());
        } else {
            return new ResultDto<>(false, null);
        }
    }
}
