package com.shuyao.image.service;

import com.shuyao.image.dto.Image2VideoBatchDTO;
import com.shuyao.image.dto.Image2VideoDTO;
import com.shuyao.image.dto.ImageDTO;

import java.io.IOException;

public interface ImageAutoService {

    String imageAutoSimpleCreate(ImageDTO imageDTO) throws IOException;

    String imageAutoBatchCreate(ImageDTO imageDTO) throws IOException;


    /****
     * 多个文件夹批量生成视频
     * @param image2VideoDTO
     * @return
     * @throws Exception
     */
    String batchCreateVideoByParentFolder(Image2VideoBatchDTO image2VideoBatchDTO) throws Exception;


    /****
     * 单个文件夹生成视频
     * @param image2VideoDTO
     * @return
     * @throws Exception
     */
    String createVideoWithAudioByImgFolder(Image2VideoDTO image2VideoDTO) throws Exception;
}
