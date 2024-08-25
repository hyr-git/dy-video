package com.shuyao.image.service;

import com.shuyao.image.dto.ImageDTO;

import java.io.IOException;

public interface ImageAutoService {

    String imageAutoCreate(ImageDTO imageDTO) throws IOException;
}
