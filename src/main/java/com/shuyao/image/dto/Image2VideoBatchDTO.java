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
@ApiModel(value = "Image2VideoBatchDTO",description = "图片转视频批量DTO")
public class Image2VideoBatchDTO {

    //源文件路径
    @ApiModelProperty(name = "mp4SaveFolder",value = "源文件路径",required = true,example = "D:\\image\\source")
    @NotEmpty(message = "源文件路径[mp4SaveFolder]不能为空")
    private String mp4SaveFolder;

    //输出文件路径
    @ApiModelProperty(name = "imageParentDirPath",value = "输出文件路径",required = true,example= "D:\\image\\out")
    @NotEmpty(message = "输出文件路径[imageParentDirPath]不能为空")
    private String imageParentDirPath;

    //新文件夹名称
    @ApiModelProperty(name = "mp3Path",value = "音频文件路径",required = true,example= "D:\\image\\source\\out.mp3")
    @NotEmpty(message = "音频文件路径[mp3Path]不能为空")
    private String mp3Path;

}
