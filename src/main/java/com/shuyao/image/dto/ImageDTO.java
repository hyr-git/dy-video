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
import javax.validation.constraints.NotNull;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ImageDTO",description = "图片处理")
public class ImageDTO {

    //源文件路径
    @ApiModelProperty(name = "sourceDirFolder",value = "源文件路径",required = true,example = "D:\\image\\source")
    @NotEmpty(message = "源文件路径不能为空")
    private String sourceDirFolder;

    //输出文件路径
    @ApiModelProperty(name = "outDirPath",value = "输出文件路径",required = true,example= "D:\\image\\out")
    @NotEmpty(message = "输出文件路径不能为空")
    private String outDirPath;

    //新生成的文件夹数量
    @ApiModelProperty(name = "newDirNum",value = "新生成的文件夹数量",required = true,example = "2")
    @NotNull(message = "新生成的文件夹数量不能为空")
    private int newDirFolderNum;

    //重复率
    @ApiModelProperty(name = "repeatRate",value = "重复率",required = true ,example = "0.8")
    @NotNull(message = "重复率不能为空")
    private double  repeatRate;

    //新文件夹名称
    @ApiModelProperty(name = "newDirFileName",value = "新文件夹名称",required = true,example= "newDir")
    @NotEmpty(message = "新文件夹名称不能为空")
    private String newDirFileName;

    //是否替换文件名称(主要处理中文)
    @ApiModelProperty(name = "renameImageFlag",value = "是否替换文件名称(主要处理中文)",required = true)
    private boolean renameImageFlag;

}
