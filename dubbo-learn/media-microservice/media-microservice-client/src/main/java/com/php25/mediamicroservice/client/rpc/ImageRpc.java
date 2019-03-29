package com.php25.mediamicroservice.client.rpc;

import com.php25.common.core.dto.ResultDto;
import com.php25.mediamicroservice.client.bo.ImgBo;

import java.util.List;

/**
 * @Auther: penghuiping
 * @Date: 2018/6/22 11:09
 * @Description:
 */
public interface ImageRpc {

    /**
     * 保存base64Image图片，并返回保存的文件名
     *
     * @param base64Image
     * @return
     */
    public ResultDto<String> save(String base64Image);

    /**
     * 通过图片id查询图片信息
     *
     * @param imageId 图片id
     * @return 图片信息
     */
    public ResultDto<ImgBo> findOne(String imageId);


    /**
     * 批量查询图片信息
     *
     * @param imageIds 多个图片id
     * @return 图片列表信息
     */
    public ResultDto<List<ImgBo>> findAll(List<String> imageIds);
}
