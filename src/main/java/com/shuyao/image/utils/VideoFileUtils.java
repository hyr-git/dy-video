package com.shuyao.image.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoFileUtils {

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

    // 提取文件名中的数字部分
    private static int extractNumber(String name) {
        int number = 0;
        for (int i = 0; i < name.length(); i++) {
            if (Character.isDigit(name.charAt(i))) {
                number = number * 10 + (name.charAt(i) - '0');
            }
        }
        return number;
    }

    /***
     * 如果文件命中中有数字，则按照数字排序
     * @param files
     * @return
     */
    public static File[] sortFilesByNameNo(File[] files){

        // 按文件名排序
        Arrays.sort(files, (o1, o2) -> {
            String name1 = o1.getName();
            String name2 = o2.getName();
            // 提取数字部分进行比较
            int num1 = extractNumber(name1);
            int num2 = extractNumber(name2);
            return Integer.compare(num1, num2);
        });
        return files;
    }


    /***
     * 如果文件命中中有数字，则按照数字排序
     * @param fileList
     * @return
     */
    public static List<File> sortFilesByNameNo(List<File> fileList){
        // 按文件名排序
        fileList.sort((o1, o2) -> {
            String name1 = o1.getName();
            String name2 = o2.getName();
            // 提取数字部分进行比较
            int num1 = extractNumber(name1);
            int num2 = extractNumber(name2);
            return Integer.compare(num1, num2);
        });
        return fileList;
    }


    public static void main(String[] args) {
        String directory = "D:\\showFile\\viedu"; // 替换为视频文件所在的目录
        List<File> fileList = VideoFileUtils.getVideoFiles(directory);
        VideoFileUtils.sortFilesByNameNo(fileList);
        for (File file : fileList) {
            System.out.println(file.getName());
        }
    }
}
