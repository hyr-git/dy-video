package com.shuyao.image.video;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import java.io.File;
import java.util.Arrays;

public class JavaCVVideoCreator {

    public static void createVideoFromImages(String imageDir, String outputPath, int frameRate) throws Exception {
        // 获取图片文件列表
        File[] imageFiles = new File(imageDir).listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
        if (imageFiles == null || imageFiles.length == 0) {
            throw new RuntimeException("未找到图片文件");
        }

        // 排序图片（按文件名升序）
        Arrays.sort(imageFiles);

        // 设置视频参数（宽高需与图片一致）
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputPath, 1920, 1080);
        recorder.setFormat("mp4");
        recorder.setFrameRate(frameRate);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.start();

        // 逐帧添加图片
        for (File imgFile : imageFiles) {
            Frame frame = new Frame();
            try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(imgFile)) {
                grabber.start();
                frame.image = grabber.grabImage().image; // 读取图片帧
                recorder.record(frame);
            }
        }

        recorder.stop();
        recorder.release();
        System.out.println("视频生成完成: " + outputPath);
    }

    public static void main(String[] args) throws Exception {
        createVideoFromImages("D:\\image\\out\\0720-xiaohongshu(5)", "D:\\\\image\\\\output.mp4", 30);
    }
}