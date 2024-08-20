package com.shuyao.image.dto;


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
public class ImageDTO {

    //源文件路径
    private String sourceDirPath;

    //输出文件路径
    private String outDirPath;

    //新生成的文件夹数量
    private int newDirNum;

    //重复率
    private double  repeatRate;

    //新文件夹名称
    private String newDirFileName;

    //是否替换文件名称(主要处理中文)
    private boolean renameImageFlag;

}
