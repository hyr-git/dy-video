package com.shuyao.image.dto;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class VideoMergeSimpleDTO {

    //源文件视频父级文件所在的目录 "D:\\showFile\\viedu";
    private String videoSourceFolder;

    //生成视频所在文件牡蛎 "D:\\showFile\\out.mp4"; // 替换为输出文件的路径
    private String videoOutputPath;

}
