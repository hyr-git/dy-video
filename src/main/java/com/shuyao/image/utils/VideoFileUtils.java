package com.shuyao.image.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static File[] sortFilesByFieldName(File[] files){

        // 按文件名排序
        Arrays.sort(files, (o1, o2) -> {
            String name1 = fieldNumText(o1.getName());
            String name2 = fieldNumText(o2.getName());
            return name1.compareTo(name2);
            /*String name1 = o1.getName();
            String name2 = o2.getName();

            // 提取数字部分进行比较
            int num1 = extractNumber(name1);
            int num2 = extractNumber(name2);
            return Integer.compare(num1, num2);*/
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
            String name1 = fieldNumText(o1.getName());
            String name2 = fieldNumText(o2.getName());
            return name1.compareTo(name2);
            /*// 提取数字部分进行比较
            int num1 = extractNumber(name1);
            int num2 = extractNumber(name2);
            return Integer.compare(num1, num2);*/
        });
        return fileList;
    }

    /***
     * 如果文件命中中有数字，则按照数字排序
     * @param fileList
     * @return
     */
    public static List<File> sortFilesByFieldName(List<File> fileList){
        // 按文件名排序
        fileList.sort((o1, o2) -> {
            String name1 = fieldNumText(o1.getName());
            String name2 = fieldNumText(o2.getName());
            return name1.compareTo(name2);
        });
        return fileList;
    }


    public static void main(String[] args) {
        String directory = "D:\\showFile\\0824-0824-脚本X22(1)"; // 替换为视频文件所在的目录
        List<File> fileList = VideoFileUtils.getVideoFiles(directory);
        VideoFileUtils.sortFilesByFieldName(fileList);
        for (File file : fileList) {
            String fieldNumText = fieldNumText(file.getName());
            System.out.println(file.getName()+"======"+fieldNumText);
        }

        //将文件名1 (8).mp4称修改001 (008).mp4
        //extractNumbers("123abc456def");

       /* String input = "123abc456def";
        String paddedInput = input.replaceAll("\\d+", "%09d"); // 假设数字不会大于999,999,999
        System.out.println(String.format(paddedInput, 0)); // 需要将格式化字符串中的 %09d 替换为实际数字
        //System.out.println(String.format(paddedInput, 0)); // 需要将格式化字符串中的 %09d 替换为实际数字*/


        String text ="水浒传 第11集.mp4";
        text = text.replaceAll("(\\d+)","0000$1");
        text = text.replaceAll("0*([0-9]{5})","$1");
                System.out.println(text);

    }

    /***
     * Java通过正则对字符串中的数字补零
     * @param text
     * @return
     */
    private static String fieldNumText(String text){
        text = text.replaceAll("(\\d+)","0000$1");
        text = text.replaceAll("0*([0-9]{5})","$1");
        return text;
    }

    /****
     * 1（20）提取文件名中的数字部分，进行排序处理
     * @param input
     * @return
     */
    private static List<Integer> extractNumbers(String input) {
        List<Integer> numbers = new ArrayList<>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(input);

        while(m.find()) {
            numbers.add(Integer.parseInt(m.group()));
            //System.out.println(m.group());
        }
        return numbers;
    }
}
