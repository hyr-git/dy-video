package com.shuyao.image.service.impl;

import com.shuyao.image.Image2Mp4;
import com.shuyao.image.ImageRandomSelector;
import com.shuyao.image.dto.Image2VideoBatchDTO;
import com.shuyao.image.dto.Image2VideoDTO;
import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.service.ImageAutoService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageAutoServiceImpl implements ImageAutoService {

    public String imageAutoSimpleCreate(ImageDTO imageDTO) throws IOException {
        String msg = "success";
        try {
            ImageRandomSelector.generateNewFolders(imageDTO.getSourceDirFolder(),imageDTO.getOutDirPath()
                    ,imageDTO.getNewDirFileName(),imageDTO.getNewDirFolderNum(),imageDTO.getRepeatRate());
        }catch (Exception e){
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }


    /****
     * 文件夹批量生成视频
     * @param imageDTO
     * @return
     * @throws IOException
     */
    public String imageAutoBatchCreate(ImageDTO imageDTO) throws IOException {
        String msg = "success";
        try {
            ImageRandomSelector.generateNewFolders(imageDTO.getSourceDirFolder(),imageDTO.getOutDirPath()
                    ,imageDTO.getNewDirFileName(),imageDTO.getNewDirFolderNum(),imageDTO.getRepeatRate());
        }catch (Exception e){
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }


    /****
     * 多个文件夹批量生成视频
     * @param image2VideoBatchDTO
     * @return
     * @throws Exception
     */
    public String batchCreateVideoByParentFolder(Image2VideoBatchDTO image2VideoBatchDTO) throws Exception {
        String msg = "success";
        try{
            Image2Mp4.batchCreateVideoByParentFolder(image2VideoBatchDTO.getMp4SaveFolder(),image2VideoBatchDTO.getImageParentDirPath()
                    ,image2VideoBatchDTO.getMp3Path());
        }catch (Exception e){
        e.printStackTrace();
        msg = e.getMessage();
        }
        return msg;
    }

    /****
     * 多个文件夹批量生成视频
     * @param image2VideoDTO
     * @return
     * @throws Exception
     */
    public String createVideoWithAudioByImgFolder(Image2VideoDTO image2VideoDTO) throws Exception {
        String msg = "success";
        try{
            Image2Mp4.createVideoWithAudioByImgFolder(image2VideoDTO.getMp4SavePath(),image2VideoDTO.getImageParentDirPath()
                    ,image2VideoDTO.getMp3Path());
        }catch (Exception e){
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

}
