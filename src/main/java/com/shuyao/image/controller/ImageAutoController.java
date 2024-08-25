package com.shuyao.image.controller;

import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.service.ImageAutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@Api(tags= "图片自动生成")
public class ImageAutoController {


    @Autowired
    private ImageAutoService imageAutoService;

    @PostMapping("/autoCreate")
    @ApiOperation(value = "图片自动生成")
    public String imageAutoCreate(@RequestBody ImageDTO imageDTO) throws IOException {
        String imageAutoCreate = imageAutoService.imageAutoCreate(imageDTO);
        return "success";
    }

}
