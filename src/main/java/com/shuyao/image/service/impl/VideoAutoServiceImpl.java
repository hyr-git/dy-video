package com.shuyao.image.service.impl;

import com.shuyao.image.ImageRandomSelector;
import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.dto.VideoMergeDTO;
import com.shuyao.image.service.ImageAutoService;
import com.shuyao.image.service.VideoAutoService;
import com.shuyao.image.video.FFmpegUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VideoAutoServiceImpl implements VideoAutoService {

    public String videoAutoMergeSimple(VideoMergeDTO videoMergeDTO) throws IOException {
        FFmpegUtil.createSimpleVideoByFolder(videoMergeDTO.getVideoSourceFolder(),videoMergeDTO.getOutputFilePath());
        return "success";
    }
}
