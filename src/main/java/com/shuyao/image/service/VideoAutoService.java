package com.shuyao.image.service;

import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.dto.VideoMergeDTO;

import java.io.IOException;

public interface VideoAutoService {

    String videoAutoMergeSimple(VideoMergeDTO videoMergeDTO) throws IOException;
}
