package com.shuyao.image.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Slf4j
@ApiModel(value = "视频合批量并入参")
public class VideoMergeBatchDTO {

    //源文件视频父级文件所在的目录 "D:\\showFile\\viedu";
    @ApiModelProperty(value="源文件视频父级文件所在的目录",required = true,example = "D:\\showFile\\viedu")
    @NotEmpty(message = "源文件视频父级文件所在的目录[videoSourceParentFolder]不能为空")
    private String videoSourceParentFolder;

    //生成视频所在文件牡蛎 "D:\\showFile\\"; // 替换为输出文件的路径
    @ApiModelProperty(value="生成视频所在文件目录",required = true,example = "D:\\showFile\\")
    @NotEmpty(message = "生成视频所在文件目录[videoSourceParentFolder]不能为空")
    private String videoOutputFolder;
}
