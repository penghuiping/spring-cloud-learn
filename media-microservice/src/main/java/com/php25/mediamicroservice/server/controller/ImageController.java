package com.php25.mediamicroservice.server.controller;

import com.php25.common.flux.web.JSONController;
import com.php25.common.flux.web.JSONResponse;
import com.php25.mediamicroservice.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotBlank;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author penghuiping
 * @date 2019/9/23 13:37
 */
@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController extends JSONController {

    @Autowired
    private ImageService imageService;


    @PostMapping("/save")
    public JSONResponse save(@NotBlank String base64String) {
        return succeed(imageService.save(base64String));
    }

    @GetMapping(path = "/{id}", produces = "image/png;image/jpg;image/jpeg")
    public Flux<DataBuffer> findOne(@PathVariable @javax.validation.constraints.Pattern(
            regexp = "([0-9A-Za-z]+)@([0-9]+)x([0-9]+).(jpg|png|jpeg)",
            message = "图片id必须符合正则[0-9A-Za-z]+@[0-9]+x([0-9]+.(jpg|png|jpeg),例如:pic800x600.jpg") String id, ServerHttpResponse response) {
        Path path = imageService.findOne(id);
        return DataBufferUtils.readInputStream(() -> Files.newInputStream(path), response.bufferFactory(), 1024)
                .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);

    }


}
