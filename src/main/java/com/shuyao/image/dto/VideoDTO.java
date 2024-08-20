package com.shuyao.image.dto;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class VideoDTO {

    //源文件路径
    private String sourceDirPath;

    //输出文件路径
    private String outDirPath;

    //像素宽带
    private int width;

    //像素的高度
    private int height;

    //每一秒多少帧，即1s会记录多少张照片，设置视频为25帧每秒
    private double frameRate;





    //新文件夹名称
    private String newDirFileName;

}
