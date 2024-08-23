package com.shuyao.image.service;

import com.shuyao.image.dto.VideoMergeBatchDTO;
import com.shuyao.image.dto.VideoMergeSimpleDTO;

import java.io.IOException;

public interface VideoAutoService {

    String videoAutoMergeSimple(VideoMergeSimpleDTO videoMergeSimpleDTO) throws IOException;

    String videoAutoMergeBatch(VideoMergeBatchDTO videoMergeBatchDTO) throws IOException;
}
