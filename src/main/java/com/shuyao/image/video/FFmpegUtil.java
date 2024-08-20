package com.shuyao.image.video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/****
 * ffmpeg -i "concat:D:\showFile\viedu\001.mp4 | D:\showFile\viedu\002.mp4" -c copy D:\showFile\viedu\output034333.mp4
 *
 *
 *
 *
 * (for %I in (*.mp4) do @echo file '%I') > files.txt && ffmpeg -f concat -safe 0 -i files.txt -c copy D:\showFile\12output.mp4
 */

public class FFmpegUtil {
    /**
     * 合并视频和音频
     * @param videoPath 视频文件路径
     * @param audioPath 音频文件路径
     * @param outputPath 合并后的视频文件输出路径
     * @throws IOException
     */
    public static void mergeVideoAndAudio(String videoPath, String audioPath, String outputPath) throws IOException {
        // ffmpeg命令
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(videoPath);
        command.add("-i");
        command.add(audioPath);
        command.add("-filter_complex");
        command.add("[0:a]volume=0.5,apad[A];[1:a]volume=0.5[B];[A][B]amix=inputs=2:duration=first:dropout_transition=2");
        command.add("-c:v");
        command.add("copy");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("experimental");
        command.add("-b:a");
        command.add("192k");
        command.add(outputPath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        // 读取命令执行结果
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        // 关闭输入流
        reader.close();
        // 等待命令执行完成
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String videoPath = "video.mp4";
        String audioPath = "audio.mp3";
        String outputPath = "output.mp4";
        try {
            FFmpegUtil.mergeVideoAndAudio(videoPath, audioPath, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
