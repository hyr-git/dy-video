package com.shuyao.image.service.impl;

import com.shuyao.image.ImageRandomSelector;
import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.service.ImageAutoService;
import org.springframework.stereotype.Service;

@Service
public class ImageAutoServiceImpl implements ImageAutoService {

    public String imageAutoCreate(ImageDTO imageDTO){
        ImageRandomSelector imageRandomSelector = new ImageRandomSelector();

        // 读取文件信息
       // ImageRandomSelector.generateNewFolders(sourceDirPath,outDirPath,newDirFileName,newViewFolders, repeatRate);
        return "success";
    }
}
