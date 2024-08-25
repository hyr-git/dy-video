package com.shuyao.image.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ImageDTO",description = "图片处理")
public class ImageDTO {

    //源文件路径
    @ApiParam(name = "sourceDirPath",value = "源文件路径",required = true)
    private String sourceDirPath;

    //输出文件路径
    @ApiParam(name = "outDirPath",value = "输出文件路径",required = true)
    private String outDirPath;

    //新生成的文件夹数量
    @ApiParam(name = "newDirNum",value = "新生成的文件夹数量",required = true)
    private int newDirFolderNum;

    //重复率
    @ApiParam(name = "repeatRate",value = "重复率",required = true)
    private double  repeatRate;

    //新文件夹名称
    @ApiParam(name = "newDirFileName",value = "新文件夹名称",required = true)
    private String newDirFileName;

    //是否替换文件名称(主要处理中文)
    @ApiParam(name = "renameImageFlag",value = "是否替换文件名称(主要处理中文)",required = true)
    private boolean renameImageFlag;

}
