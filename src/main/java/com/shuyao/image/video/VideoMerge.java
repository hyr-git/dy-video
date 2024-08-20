package com.shuyao.image.video;

import org.apache.commons.io.FileUtils;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class VideoMerge {



    public  static void sfghdgh() throws IOException {
        String videoPath1 = "D:\\showFile\\viedu\\001.mp4";
        String videoPath2 = "D:\\showFile\\viedu\\002.mp4";

        FFmpegFrameGrabber grabber1 = new FFmpegFrameGrabber(videoPath1);
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        FFmpegLogCallback.set();
        grabber1.start();
        FFmpegFrameGrabber grabber2 = new FFmpegFrameGrabber(videoPath2);
        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
        FFmpegLogCallback.set();
        grabber2.start();

        List<Frame> frames1 = new ArrayList<>();
        List<Frame> frames2 = new ArrayList<>();

        Frame frame1;
        while ((frame1 = grabber1.grabFrame()) != null) {
            frames1.add(frame1);
        }

        Frame frame2;
        while ((frame2 = grabber2.grabFrame()) != null) {
            frames2.add(frame2);
        }
        dg(grabber1,frames1,frames2);

        grabber1.stop();
        grabber2.stop();
    }

    public static void dg(FFmpegFrameGrabber grabber1 ,List<Frame> frames1, List<Frame> frames2) throws IOException {
        String outputPath = "D:\\showFile\\viedu\\output.mp4";
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputPath, frames1.get(0).imageWidth, frames1.get(0).imageHeight);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        recorder.setFrameRate(grabber1.getFrameRate());
        recorder.start();

        for (Frame frame : frames1) {
            recorder.record(frame);
        }

        for (Frame frame : frames2) {
            recorder.record(frame);
        }

        recorder.stop();


        File outputFile = new File(outputPath);
        FileUtils.copyFile(outputFile, new File("D:\\showFile\\viedu\\destination.mp4"));

    }



    /****
     *
     * @param path
     * @return
     */
    public static List<File> getAllFilesAndDir(String path){
        File root = new File(path);
        List<File> files = new ArrayList<>();
        if(!root.isDirectory()){
            files.add(root);
        }else{
            File[] subFiles = root.listFiles();
            for(File f : subFiles){
                files.addAll(getAllFilesAndDir(f.getAbsolutePath()));
            }
        }
        return files;
    }


    public static void main(String[] args ) throws Exception {
        sfghdgh();
    }


    //测试
    public static void ggdgdg() {//定义 需要复制文件的路径
        String srcPath = "/Users/srcPath";
        String destName ="mergedVideo.mp4";
        String destFullPath ="/Users/destFileDir" + destName ;
        List<File> files = getAllFiles (srcPath);
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() & o2.isFile()){
                    return -1;
                }

                if (o1.isFile() && o2.isDirectory()){
                    return 1;
                }

                Integer o1Name =Integer.valueOf(o1.getName()) ;
                Integer o2Name =Integer.valueOf(o2.getName());
                return o1Name . compareTo( o2Name) ;
            }
        });
        for (File file : files) {
            System. out. println(file . getName());
        }
        //boolean flag = MergeFiles(files, destFullPath) ;
       // System. out. println("合并完成的标记: "+ flag);
    }


    /***遍历某一 个路径的所有文件，不包括子文件夹
     *@pa rampath
     *@return
     */
    public static List<File> getAllFiles(String path) {
        File root = new File(path);//文件路径是文件夹，获取文件夹下的所有文件
        if( root . getName() .contains("DS_ Store")) {
            System.out.println(root.getName());
        }
        if (root. isDirectory()) {
                //过滤
                //排除文件名含有".DS_ Store"的文件
                return Arrays.asList(root.listFiles()) .stream() . filter(a -> !a. getName() . endsWith(".DS_ Store"))
                        .collect(Collectors.toList());
        }
        return new ArrayList();

    }

    /******
    public static boolean MergeFiles (List<File> listFiles, String destFullPath) throws FileNotFoundException {
        boolean flag = true;
        BufferedOutputStream bos = null;
        BufferedReader br = null;
        try {
            FileInputStream fis;
//打开与目标文件对象的通道
            FileInputStream fos = new FileOutputStream(destFullPath);
            byte buffer[] = new byte[1024 * 1024 * 2];//-次读取2M数据， 将读取到的数据保存到byte字节数组中
            for(File srcFile : listFiles) {//涉及合并文件， 最好限制同名文件(根据需求判断是否需要)
//判断台并文件的格式
                if (srcFile.isFile() & !srcFile.getName().endsWith(".DS_ Store")) {
                    System.out.println(srcFile.getName() + "是合法文件，开始合并");
                    try {//打开与源文件对象的通道
                        fis = new FileInputStream(srcFile);
                        int len = 0;
                        while ((len = fis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);//buffer从指定字 节数组写入。buffer: 数据中的起始偏移量，len
                        }
                        fis.close();
                    } finally {
                        if (br != null) {
                            br.close();
                        }
                    } else{
                        System.out.println(srcFile.getName() + "文件不满足合并格式! ");
                    }
                    fos.flush();
                    fos.close();
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                    flag = false;
                } finally{
                    try

                }
            }



    ****/






















}
