package com.php25.mediamicroservice.server.service;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.php25.common.core.exception.Exceptions;
import com.php25.common.core.util.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: penghuiping
 * @date: 2019/1/4 13:12
 * @description:
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${base_assets_upload_path}")
    private String resourcePath;

    @Value("${base_assets_upload_url}")
    private String resourceUrl;

    private static final Pattern pattern = Pattern.compile("([0-9A-Za-z]+)@([0-9]+)x([0-9]+).(jpg|png|jpeg)");

    @Autowired
    private LockRegistry lockRegistry;

    @Override
    public Mono<String> save(String base64StringContent) {
        return Mono.fromCallable(() -> {
            //获取文件类型
            byte[] arr = DigestUtil.decodeBase64(base64StringContent);
            ContentInfoUtil util = new ContentInfoUtil();
            ContentInfo info = util.findMatch(arr);
            String name = DigestUtil.SHAStr(base64StringContent) + "." + info.getFileExtensions()[0];
            String absolutePath = resourcePath + "/" + name;

            //写入文件
            Path path1 = Paths.get(absolutePath);
            try {
                if (!Files.exists(path1)) {
                    Files.write(path1, arr);
                }
            } catch (IOException e) {
                throw Exceptions.throwIllegalStateException("写入文件出错", e);
            }
            return name;
        }).subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<Path> findOne(String id) {
        return Mono.fromCallable(() -> {
            Matcher matcher = pattern.matcher(id);
            if (matcher.find()) {
                String picName = matcher.group(1);
                String longLength = matcher.group(2);
                String widthLength = matcher.group(3);
                String picType = matcher.group(4);
                log.info("图片名:{}.{},长度:{},宽度:{}", picName, picType, longLength, widthLength);
                String originPicName = String.format("%s.%s", picName, picType);
                String absolutePath = resourcePath + "/" + originPicName;
                String newAbsolutePath = String.format("%s/%s", resourcePath, id);
                Path path = Paths.get(newAbsolutePath);
                //排查文件是否存在
                if (Files.exists(path)) {
                    //存在
                    return path;
                }
                Lock lock = lockRegistry.obtain("imageServiceFindOne");
                try {
                    lock.lock();
                    //排查文件是否存在
                    if (!Files.exists(path)) {
                        //读出所有的字节
                        Thumbnails.of(absolutePath).size(Integer.parseInt(longLength), Integer.parseInt(widthLength)).toFile(newAbsolutePath);
                    }
                } catch (Exception e) {
                    log.error("无法转换图片尺寸", e);
                    throw Exceptions.throwIllegalStateException("无法转换图片尺寸:" + newAbsolutePath, e);
                } finally {
                    lock.unlock();
                }
                //图片一定存在
                return path;
            } else {
                throw Exceptions.throwImpossibleException();
            }
        }).subscribeOn(Schedulers.elastic());

    }
}
