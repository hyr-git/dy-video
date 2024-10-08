package com.shuyao.image.controller;

import com.shuyao.image.base.R;
import com.shuyao.image.dto.VideoMergeBatchDTO;
import com.shuyao.image.dto.VideoMergeSimpleDTO;
import com.shuyao.image.service.VideoAutoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/video")
@Api(value = "videoAutoMerge" ,tags= "视频自动合并处理")
public class VideoAutoMergeController {

    @Autowired
    private VideoAutoService videoAutoService;

    @PostMapping("/simpleAutoMerge")
    @ApiOperation(value = "单个文件视频合并")
    public R simpleAutoMerge(@RequestBody @Validated VideoMergeSimpleDTO videoMergeDTO) throws IOException {
        String imageAutoCreate = videoAutoService.videoAutoMergeSimple(videoMergeDTO);
        return R.ok(imageAutoCreate);
    }

    @PostMapping("/batchAutoMerge")
    @ApiOperation(value = "批量视频合并")
    /*@ApiOperation(value = "batchAutoMerge", notes = "批量视频合并", produces = "application/json",
            consumes = "application/json", response = String.class)*/
   /* @ApiImplicitParam(name="videoMergeDTO", value = "视频合批量并入参",
            dataTypeClass = VideoMergeBatchDTO.class,
            paramType="body", required = true)*/
    public R<String> batchAutoMerge(@RequestBody @Validated VideoMergeBatchDTO videoMergeDTO) throws IOException {
        String imageAutoCreate = videoAutoService.videoAutoMergeBatch(videoMergeDTO);
        return R.ok(imageAutoCreate);
    }

}
