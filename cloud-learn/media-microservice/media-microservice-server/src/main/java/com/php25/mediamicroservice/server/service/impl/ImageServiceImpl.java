package com.php25.mediamicroservice.server.service.impl;

import com.j256.simplemagic.ContentInfoUtil;
import com.php25.common.core.exception.ServiceException;
import com.php25.common.core.util.DigestUtil;
import com.php25.common.jdbc.service.BaseServiceImpl;
import com.php25.mediamicroservice.server.dto.ImgDto;
import com.php25.mediamicroservice.server.model.Img;
import com.php25.mediamicroservice.server.repository.ImgRepository;
import com.php25.mediamicroservice.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:12
 * @Description:
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService, InitializingBean {
    @Value("${base_assets_upload_path}")
    private String resourcePath;

    @Value("${base_assets_upload_url}")
    private String resourceUrl;

    private BaseServiceImpl<ImgDto, Img, String> baseService;

    @Autowired
    private ImgRepository imgRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.baseService = new BaseServiceImpl<>(ImgDto.class, Img.class, imgRepository);
    }

    @Override
    public String save(String base64Image) {
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
            throw new ServiceException("写入文件出错", e);
        }
        return name;
    }

    @Override
    public Optional<ImgDto> findOne(String id) {
        return baseService.findOne(id);
    }

    @Override
    public Optional<List<ImgDto>> findAll(List<String> ids) {
        return baseService.findAll(ids);
    }
}
