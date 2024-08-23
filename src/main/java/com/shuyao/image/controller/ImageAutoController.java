package com.shuyao.image.controller;

import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.service.ImageAutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class ImageAutoController {


    @Autowired
    private ImageAutoService imageAutoService;

    @PostMapping("/autoCreate")
    public String imageAutoCreate(ImageDTO imageDTO){

        String imageAutoCreate = imageAutoService.imageAutoCreate(imageDTO);
        return "success";
    }

}
