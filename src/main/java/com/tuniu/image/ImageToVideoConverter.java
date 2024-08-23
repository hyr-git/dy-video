package com.tuniu.image;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageToVideoConverter {
    public static void convertImagesToVideo(String imageFolderPath, String videoOutputPath, int imageWidth, int imageHeight, double frameRate) throws Exception {
        File folder = new File(imageFolderPath);
        File[] imageFiles = folder.listFiles();

        // 按文件名排序
        if (imageFiles != null) {
            java.util.Arrays.sort(imageFiles);
        }

        // 创建FFmpegFrameRecorder用于记录视频
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoOutputPath, imageWidth, imageHeight);
        recorder.setFormat("mp4");
        recorder.setFrameRate(frameRate);
        recorder.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();
        /*//ImageConverter converter = new ImageConverter();
        converter.setOutputFrameRate(frameRate);

        for (File file : imageFiles) {
            if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                Frame image = converter.convert(new Frame(file));
                recorder.record(image);
                image.dispose();
            }
        }*/

        recorder.stop();
        recorder.release();
        System.out.println("视频生成完毕: " + videoOutputPath);
    }

    public static void main(String[] args) {
        /*try {
        try {
            convertImagesToVideo("D:\\showFile008\\0819-脚本23-L马文静(500)", "D:\\showFile008\\0819-脚本23-L马文静(500)\\video.mp4",
                    640, 480, 30.0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String str = "nihaohosho";
        String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正
        Pattern pat = Pattern.compile(REGEX_CHINESE) ;
        Matcher mat = pat.matcher(str) ;
        System.out.println(mat.find());
        System. out.println (mat.replaceAll(""));
    }
}