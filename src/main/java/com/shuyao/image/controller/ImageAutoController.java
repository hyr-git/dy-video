package com.shuyao.image.controller;

import com.shuyao.image.base.R;
import com.shuyao.image.dto.Image2VideoBatchDTO;
import com.shuyao.image.dto.Image2VideoDTO;
import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.service.ImageAutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@Api(tags= "图片处理(不建议中文路径、名称)")
public class ImageAutoController {


    @Autowired
    private ImageAutoService imageAutoService;


    @PostMapping("/autoBatchCreate")
    @ApiOperation(value = "通过大量文件图集批量生成指定的图片文件")
    public R<String> imageAutoBatchCreate(@RequestBody @Validated ImageDTO imageDTO) throws IOException {
        String imageAutoCreate = imageAutoService.imageAutoBatchCreate(imageDTO);
        return R.ok(imageAutoCreate);
    }



    @PostMapping("/image2SimpleVideo")
    @ApiOperation(value = "单个文件夹图片转换为视频(不能包含中文)")
    public R<String> createVideoWithAudioByImgFolder(@RequestBody @Validated Image2VideoDTO image2VideoDTO) throws Exception {
        String imageAutoCreate = imageAutoService.createVideoWithAudioByImgFolder(image2VideoDTO);
        return R.ok(imageAutoCreate);
    }

    @PostMapping("/image2BatchVideo")
    @ApiOperation(value = "批量将单个文件夹图片转换为视频(不能包含中文)")
    public R<String> batchCreateVideoByParentFolder(@RequestBody @Validated Image2VideoBatchDTO image2VideoBatchDTO) throws Exception {
        String imageAutoCreate = imageAutoService.batchCreateVideoByParentFolder(image2VideoBatchDTO);
        return R.ok(imageAutoCreate);
    }

}
