package com.php25.mediaservice.client.rpc;

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
    public String save(String base64Image);
}
