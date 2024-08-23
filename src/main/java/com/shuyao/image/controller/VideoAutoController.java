package com.shuyao.image.controller;

import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.dto.VideoMergeDTO;
import com.shuyao.image.service.ImageAutoService;
import com.shuyao.image.service.VideoAutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/video")
public class VideoAutoController {


    @Autowired
    private VideoAutoService videoAutoService;

    @PostMapping("/simpleAutoMerge")
    public String simpleAuto(@RequestBody VideoMergeDTO videoMergeDTO) throws IOException {
        String imageAutoCreate = videoAutoService.videoAutoMergeSimple(videoMergeDTO);
        return "success";
    }

}
