package com.shuyao.image.service.impl;

import com.shuyao.image.dto.VideoMergeBatchDTO;
import com.shuyao.image.dto.VideoMergeSimpleDTO;
import com.shuyao.image.service.VideoAutoService;
import com.shuyao.image.video.FFmpegUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class VideoAutoServiceImpl implements VideoAutoService {

    @Override
    public String videoAutoMergeSimple(VideoMergeSimpleDTO videoMergeSimpleDTO) throws IOException {
        String msg = "success";
        try{
            FFmpegUtil.createSimpleVideoByFolder(videoMergeSimpleDTO.getVideoSourceFolder(),videoMergeSimpleDTO.getVideoOutputPath());
        }catch (Exception e){
            msg = e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    public String videoAutoMergeBatch(VideoMergeBatchDTO videoMergeBatchDTO) throws IOException {
        String msg = "success";
        try{
            String videoSourceParentFolder = videoMergeBatchDTO.getVideoSourceParentFolder();
            String videoOutputFolder = videoMergeBatchDTO.getVideoOutputFolder();

            File file = new File(videoSourceParentFolder);
            if(null == file || !file.exists() || !file.isDirectory()){
                throw new RuntimeException("[videoSourceParentFolder] is not a folder");
            }

            File outDir = new File(videoOutputFolder);
            if(!outDir.exists()){
                outDir.mkdirs();
            }

            //获取所有的子目录视频文件夹
            File[] listFiles = file.listFiles();
            for (File fileFolder : listFiles){
                if(null != fileFolder && fileFolder.isDirectory()) {
                    String videoSourceFolder = fileFolder.getAbsolutePath();
                    String videoOutputFilePath = videoOutputFolder + File.separator + fileFolder.getName() + ".mp4";
                    FFmpegUtil.createSimpleVideoByFolder(videoSourceFolder, videoOutputFilePath);
                }
            }        }catch (Exception e){
            msg = e.getMessage();
            e.printStackTrace();
        }
        return msg;
    }
}
