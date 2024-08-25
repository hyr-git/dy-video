package com.shuyao.image.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Image2VideoDTO",description = "图片转视频DTO")
public class Image2VideoDTO {

    @ApiModelProperty(name = "imageParentDirPath",value = "图片文件夹路径",required = true,example= "D:\\image\\out")
    @NotEmpty(message = "图片文件夹[imageParentDirPath]不能为空")
    private String imageParentDirPath;

    @ApiModelProperty(name = "mp3Path",value = "音频文件路径",required = true,example= "D:\\image\\source\\out.mp3")
    @NotEmpty(message = "音频文件[mp3Path]路径不能为空")
    private String mp3Path;

    //源文件路径
    @ApiModelProperty(name = "mp4SavePath",value = "源文件路径",required = true,example = "D:\\image\\output\\out.mp4")
    @NotEmpty(message = "源文件路径[mp4SavePath]不能为空")
    private String mp4SavePath;





}
