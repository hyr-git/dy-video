package com.shuyao.image.video;

import java.io.File;

public class ImageToVideoConverter {

    public static void convertImagesToVideo(String inputPathPattern, String outputPath) {
        try {
            // FFmpeg命令：30帧/秒，H.264编码，输出MP4
            String command = "ffmpeg -framerate 30 -i " + inputPathPattern +
                            " -c:v libx264 -pix_fmt yuv420p -y " + outputPath;

            ProcessBuilder builder = new ProcessBuilder(command.split(" "));
            builder.directory(new File("."));
            Process process = builder.start();
            process.waitFor(); // 等待转换完成

            System.out.println("视频生成成功: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 示例：将 images_%03d.png 序列合成视频
       // convertImagesToVideo("images/image_%03d.png", "output.mp4");
        convertImagesToVideo("D:\\image\\out\\0720-xiaohongshu(5)", "D:\\image\\output.mp4");

    }
}