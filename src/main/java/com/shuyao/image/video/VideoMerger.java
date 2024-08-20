
package com.shuyao.image.video;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*****
 * 需要依赖于三方插件
 * 需要下载FFmpeg
 * https://blog.csdn.net/qq_42663692/article/details/130915201
 */
public class VideoMerger {
    public static void mergeVideos(List<File> videoFiles, String outputFilePath) {
        try {
            FFmpeg ffmpeg = new FFmpeg("D:\\Program Files\\ffmpeg7\\bin\\ffmpeg.exe"); // 替换为FFmpeg可执行文件的路径
            FFprobe ffprobe = new FFprobe("D:\\Program Files\\ffmpeg7\\bin\\ffprobe.exe"); // 替换为FFprobe可执行文件的路径
            FFmpegBuilder builder = new FFmpegBuilder();

            for (File file : videoFiles) {
                builder.addInput(file.getAbsolutePath());
            }
            builder.addOutput(outputFilePath)
                    .setFormat("mp4")
                    .setVideoCodec("copy")
                    .setAudioCodec("copy")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg,ffprobe);
            executor.createJob(builder).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<File> getVideoFiles(String directory) {
        List<File> videoFiles = new ArrayList<>();
        File dir = new File(directory);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile() && isVideoFile(file)) {
                videoFiles.add(file);
            }
        }
        return videoFiles;
    }

    public static boolean isVideoFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String[] videoExtensions = {"mp4", "avi", "mov"};
        for (String videoExt : videoExtensions) {
            if (extension.equalsIgnoreCase(videoExt)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String directory = "D:\\showFile\\viedu"; // 替换为视频文件所在的目录
        String outputFilePath = "D:\\showFile\\02output.mp4"; // 替换为输出文件的路径

        List<File> videoFiles = VideoMerger.getVideoFiles(directory);
        VideoMerger.mergeVideos(videoFiles, outputFilePath);

        System.out.println("视频合并完成！");
    }

}
