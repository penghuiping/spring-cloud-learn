package com.php25.mediaservice.server.service.impl;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.php25.common.util.DigestUtil;
import com.php25.mediaservice.server.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:12
 * @Description:
 */
@Service
public class ImageServiceImpl implements ImageService {
    private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Value("${base_assets_upload_path}")
    private String resourcePath;

    @Value("${base_assets_upload_url}")
    private String resourceUrl;


    @Override
    public String save(String base64Image) {
        //获取文件类型
        byte[] arr = DigestUtil.decodeBase64(base64Image);
        ContentInfoUtil util = new ContentInfoUtil();
        ContentInfo info = util.findMatch(arr);
        String name = DigestUtil.SHAStr(base64Image) + "." + info.getFileExtensions()[0];
        String absolutePath = resourcePath + "/" + name;

        //写入文件
        Path path1 = Paths.get(absolutePath);
        try {
            if (!Files.exists(path1))
                Files.write(path1, arr);
        } catch (IOException e) {
            logger.error("写入文件出错", e);
            throw new RuntimeException(e);
        }
        return name;
    }
}
