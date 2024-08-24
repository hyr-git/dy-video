package com.shuyao.image.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@ApiModel(value = "视频合并入参",description = "视频合并入参描述")
public class VideoMergeSimpleDTO {

    //源文件视频父级文件所在的目录 "D:\\showFile\\viedu";
    @ApiModelProperty(value = "源文件视频父级文件所在的目录",required = true,example = "D:\\showFile\\viedu", name = "videoSourceFolder2")
    private String videoSourceFolder;

    //生成视频所在文件路径 "D:\\showFile\\out.mp4"; // 替换为输出文件的路径
    @ApiModelProperty(value = "生成视频所在文件路径",name="生成视频所在文件路径", required = true, example = "D:\\showFile\\out.mp4")
    private String videoOutputPath;

}
