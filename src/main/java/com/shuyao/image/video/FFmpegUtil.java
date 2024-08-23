package com.shuyao.image.video;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.shuyao.image.utils.VideoFileUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/****
 * ffmpeg -i "concat:D:\showFile\viedu\001.mp4 | D:\showFile\viedu\002.mp4" -c copy D:\showFile\viedu\output034333.mp4
 *
 * ffmpeg -i concat -safe 0 D:\showFile\viedu\temp.txt -c copy D://showFile//viedu//out.mp4
 *ffmpeg -f concat -safe 0 -i D:\私活\dy-viedo\4a10e579-42a4-4b59-ab3b-093bbb51a85btemp.txt -c copy D:\showFile\viedu\out.mp4
 *ffmpeg -i concat -safe 0 d://temp.txt -c copy D://showFile//viedu//out.mp4
 * (for %I in (*.mp4) do @echo file '%I') > files.txt && ffmpeg -f concat -safe 0 -i files.txt -c copy D:\showFile\12output.mp4
 */

@Slf4j
public class FFmpegUtil {

    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String USERDIR = System.getProperty("user.dir");

    /****
     *
     * @param fileList
     * @param txtFile
     * @return
     */
    public static String createConcatTxtFile(List<String> fileList, String txtFile) {

        StringBuilder context = new StringBuilder();
        for (String file : fileList) {
            context.append("file '").append(file).append("'\r\n");
        }

        FileUtil.writeUtf8String(context.toString(), txtFile);
        return txtFile;
    }


    /*****
     * 单个文件夹的视频片段合成一个视频
     * @param videoSourceFolder 视频文件所在的目录 "D:\\showFile\\viedu";
     * @param outputFilePath 生成视频所在文件路径 "D:\\showFile\\12-put.mp4"; // 替换为输出文件的路径
     * @return
     * @throws IOException
     */
    public static void createSimpleVideoByFolder(String videoSourceFolder, String outputFilePath) throws IOException {
        List<File> fileList = VideoFileUtils.getVideoFiles(videoSourceFolder);
        VideoFileUtils.sortFilesByNameNo(fileList);

        List<String> filePathList = fileList.stream().map(File::getAbsolutePath)
                .collect(Collectors.toList());

        log.info("filePathList==================={}", filePathList);

        try {
            mergeVideo(filePathList, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void mergeVideo(List<String> fileList, String outputPath) throws IOException {
        //地址放在txt文件
        String txtFile = USERDIR + SEPARATOR +"temp.txt";
        txtFile = "D:\\showFile\\viedu\\temp.txt";
        String txtPath = createConcatTxtFile(fileList, txtFile);

        mergeVideo(txtPath, outputPath);
    }

    /**
     * 合并视频和音频
     * @param outputPath 合并后的视频文件输出路径
     * @throws IOException
     */
    public static void mergeVideo(String textPath, String outputPath) throws IOException {
        // ffmpeg命令
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-f");
        command.add("concat");
        command.add("-safe");
        command.add("0");
        command.add("-i");
        /*command.add("-i");
        command.add("concat");
        command.add("-safe");
        command.add("0");*/
        command.add(textPath);
        command.add("-c");
        command.add("copy");
        command.add(outputPath);
        log.info("cmd====={}",command.toString());

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

        log.info("cmd====={}",command);

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


    public static void main(String[] args) throws IOException {
      /*  String videoPath = "video.mp4";
        String audioPath = "audio.mp3";
        String outputPath = "output.mp4";
        try {
            FFmpegUtil.mergeVideoAndAudio(videoPath, audioPath, outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        createSimpleVideoByFolder("D://showFile//viedu","D://showFile//viedu//out.mp4");
    }
}
