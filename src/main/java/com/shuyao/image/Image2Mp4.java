package com.shuyao.image;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.helper.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.IplImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * 通过读取指定文件夹的图片生成MP4文件
 * 无法支持中文,即中文字符
 * @version V1.0
 * @description: 图片合成MP4
 * @since 2020/5/16 18:00
 **/
@Slf4j
public class Image2Mp4 {

    private static int width =  1080;
    private static int height = 1442;

    private static double videoFrameRate = 0.3;

    static String mp3Path ="D:\\showFile\\000000001.MP3";

    static String imgBatchFile = "D:\\showFile011\\";

    static String img = "D:\\showFile012\\0820-scrit23-jojso(1)\\";

    public static void main(String[] args) throws Exception {
        //合成的MP4
        String mp4SaveFolder = "D:\\showFile\\video21\\";
        //图片地址 这里面放了22张图片


        //createVideoWithAudioByImgFolder(mp4SavePath,img,mp3Path);
        batchCreateVideoByParentFolder(mp4SaveFolder, imgBatchFile,mp3Path);


    }


    /******
     * 批量指定一个父目录的文件夹，批量将制定文件夹的图片合成MP4
     * @param mp4SaveFolder
     * @param imageParentDirPath
     * @param mp3Path
     * @throws Exception
     */
    public static void batchCreateVideoByParentFolder(String mp4SaveFolder,  String imageParentDirPath,String mp3Path) throws Exception {
        //合成的MP4
        //String mp4SavePath = "D:\\showFile\\viedu\\";
        //图片地址 这里面放了22张图片


        //读取所有图片
        File file = new File(imageParentDirPath);
        if(file.isDirectory()){
            File[] files = file.listFiles();

            String saveFolder = "";

            for (File viedImageFile: files) {
                if(viedImageFile.isDirectory()){
                    File[] imageFiles = viedImageFile.listFiles();
                    if (imageFiles.length > 0) {


                        saveFolder = mp4SaveFolder +File.separator+viedImageFile.getName();
                        File outSaveFolder = new File(saveFolder);
                        if(!outSaveFolder.exists()){
                            outSaveFolder.mkdirs();
                        }

                        //createViewMp4(mp4SavePath+viedImageFile.getName()+".mp4", imageFiles);
                        createVideoWithAudio(saveFolder+File.separator+viedImageFile.getName()+".mp4",mp3Path, imageFiles);

                    }
                }
            }
        }
    }

    /****
     * 将指定文件夹的图片合成MP4
     * @param mp4SavePath
     * @param imageFileDir
     * @param audioPath
     * @throws Exception
     */
    public static void createVideoWithAudioByImgFolder(String mp4SavePath,String imageFileDir,String audioPath) throws Exception {
        File file = new File(imageFileDir);
        if(null != file && file.isDirectory()){
            File[] imageFiles = file.listFiles();

            createVideoWithAudio(mp4SavePath, audioPath, imageFiles);
        }

    }


    private static void createVideoWithAudio(String mp4SavePath,String audioPath,  File[] imageFiles) throws Exception {
        List<File> fileList = new ArrayList<>();
        for (File file1: imageFiles) {
            fileList.add(file1);
        }
        createVideoWithMp3(mp4SavePath, audioPath,fileList, width, height);
    }


    private static void createVideoMp4(String mp4SavePath,String audioPath,  File[] imageFiles) throws Exception {
        List<File> fileList = new ArrayList<>();
        for (File file1: imageFiles) {
            fileList.add(file1);
        }
        String mp4SavePathName = mp4SavePath+"001.mp4";
        createViewMp4(mp4SavePathName, fileList, width, height);
        String outPutName = mp4SavePath+"23.mp4";
        mergeAudioAndVideo(mp4SavePathName, audioPath, outPutName);
    }

    private static void createViewMp4(String mp4SaveDir, String audioPath, File[] imageFiles) throws Exception {
        List<File> fileList = new ArrayList<>();
        for (File file1: imageFiles) {
            fileList.add(file1);
        }
        String mp4SavePathName = mp4SaveDir+"001.mp4";
        createViewMp4(mp4SavePathName, fileList, width, height);
        String outPutName = mp4SaveDir+"23.mp4";

        mergeAudioAndVideo(mp4SavePathName, audioPath, outPutName);
    }


    private static void mergeAudioAndVideo(String videoPath, String audioPath, String outPut) throws Exception {

        //没有音频文件 直接返回原文件
        if (null == audioPath){
            new File(videoPath).renameTo(new File(outPut));
            return;
        }

        FFmpegFrameRecorder recorder = null;
        FrameGrabber grabberVideo = null;
        FrameGrabber grabberAudio = null;
        try {
            //抓取视频帧
            grabberVideo = new FFmpegFrameGrabber(videoPath);
            //抓取音频帧
            grabberAudio = new FFmpegFrameGrabber(audioPath);
            grabberVideo.start();
            grabberAudio.start();
            //创建录制
            recorder = new FFmpegFrameRecorder(outPut,
                    grabberVideo.getImageWidth(), grabberVideo.getImageHeight(),
                    grabberAudio.getAudioChannels());

            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

            recorder.setFormat("mp4");
            recorder.setFrameRate(grabberVideo.getFrameRate());
            recorder.setSampleRate(grabberAudio.getSampleRate());
            // 视频质量，0表示无损
            recorder.setVideoQuality(0);
            recorder.start();


            Frame frame1;
            Frame frame2 = null;
            //先录入视频
            while ((frame1 = grabberVideo.grabFrame()) != null) {
                recorder.record(frame1);
            }


            //然后录入音频
            while ((frame2 = grabberAudio.grabFrame()) != null) {
                recorder.record(frame2);
            }
            //然后录入音频
            audioEntry(frame2, grabberVideo, grabberAudio, recorder);

            grabberVideo.stop();
            grabberAudio.stop();
            recorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (recorder == null) {
                    recorder.release();
                }
                if (grabberVideo == null) {
                    grabberVideo.release();
                }
                if (grabberAudio == null) {
                    grabberAudio.release();
                }
            } catch (FrameRecorder.Exception e) {
                e.printStackTrace();
            }
        }
    }


    /*****
     * 图片和音频同时生成MP4视频
     * @param mp4SavePath
     * @param mp3Path
     * @param fileList
     * @param width
     * @param height
     * @throws Exception
     */
    private static void createVideoWithMp3(String mp4SavePath, String mp3Path,List<File> fileList, int width, int height) throws Exception {

        //视频宽高最好是按照常见的视频的宽高  16：9  或者 9：16
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(mp4SavePath, width, height);
        //设置视频编码层模式,MP4支持多种编码格式，如：AV_CODEC_ID_MPEG4、AV_CODEC_ID_H264、AV_CODEC_ID_MJPEG、AV_CODEC_ID_FLV1等
        //AV_CODEC_ID_H264 格式才能在html上播放
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        //文件格式mp4
        recorder.setFormat("mp4");
        //每一秒多少帧，即1s会记录多少张照片，设置视频为25帧每秒
        recorder.setFrameRate(videoFrameRate);
        //设置视频的比特率 (比特率越高，视频越清晰，文件越大)
        //视频每秒大小，值越大图片转过来的压缩率就越小，视频就越清晰，文件也越大
        recorder.setVideoBitrate(80000);
        //设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        //先默认吧，这个应该属于设置视频的处理模式，一般使用默认即可(不可变-固定)音频比特率
        recorder.setAudioOption("crf","0");
        //最高质量
        recorder.setAudioQuality(0);
        //设置音频采样率(Hz)
        recorder.setAudioBitrate(192000);
        //设置音频通道数
        recorder.setSampleRate(44100);
        //设置音频编码层模式：双通道-立体声
        recorder.setAudioChannels(2);
        try {
            //开始录制
            recorder.start();
            // --------------》开始处理图片
            OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
            for(File file : fileList){
                /*if(".mp3".equalsIgnoreCase(file.getName())){
                    viewName = file.getPath();
                    continue;
                    添加音频
                    recorder.setAudioChannels(2);
                    recorder.setSampleRate(44100);
                    recorder.startAudio();
                    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
                    grabber.start();
                    Frame frame = null;
                    while ((frame = grabber.grabFrame()) != null)
                        recorder.record(frame);

                    grabber.stop();
                    grabber.release();
                    recorder.stop();
                    recorder.start();
                    recorder.record(frame);
                    recorder.stop();
                    recorder.start();
                    recorder.record(frame);
                    recorder.stop();
                }*/

                //此处非常吃内存，如果图片过多，会内存溢出
                IplImage image = opencv_imgcodecs.cvLoadImage(file.getPath());
                Frame frame = converter.convert(image);
                //录制
                recorder.record(frame);
                //释放内存
                opencv_core.cvReleaseImage(image);
            }
            // --------------》结束处理图片

            // -------------->开始处理音频
            if(StringUtils.isNotBlank(mp3Path)){
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(mp3Path);
                grabber.start();//开始录制音频
                audioEntry(null,grabber,recorder,recorder.getTimestamp());
                // ----------->结束处理音频
                grabber.stop();
                grabber.release();
            }

            /*Java2DFrameConverter converter = new Java2DFrameConverter();
            for(File file : fileList){
                *//*if(".mp3".equals(file.getName())){

                }*//*
                BufferedImage read = null;
                try{
                    log.info("---------"+file.getName());
                    read = ImageIO.read(file);
                    for (int i=0;i<recorder.getFrameRate();i++){
                        recorder.record(converter.getFrame(read));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后一定要结束并释放资源
            recorder.stop();
            recorder.release();
        }
    }


    private static void audioEntry(Frame frame, FrameGrabber grabberVideo, FrameGrabber grabberAudio,FrameRecorder recorder ) throws Exception {
        long grabber1Timestamp = grabberVideo.getTimestamp();
        while(( (frame =  grabberAudio.grabFrame()) !=null) && grabber1Timestamp > 0){
            //若视频长度小于音频时长，则截取音频帧
            if(grabber1Timestamp <= grabberAudio.getTimestamp()) break;
            recorder.record(frame);
        }
        long differ = grabber1Timestamp - grabberAudio.getTimestamp();
        //如果视频时长大于音频时长，则循环录入
        if(differ>0){
            grabberVideo.setTimestamp(differ);
            audioEntry(frame,grabberVideo,grabberAudio,recorder);
        }
    }


    /****
     * 录制音频
     * @param frame  视频帧
     * @param mp3Grabber  音频解码器
     * @param recorder   解码器
     * @param recordTimestamp  视频总时长
     * @throws FrameRecorder.Exception
     */
    private static void audioEntry(Frame frame, FFmpegFrameGrabber mp3Grabber, FFmpegFrameRecorder recorder, long recordTimestamp) throws Exception {
        while(( (frame =  mp3Grabber.grabFrame()) !=null) && recordTimestamp > 0){
            //果然时长时长小于音频时长，则截取音频帧
            long timestamp = mp3Grabber.getTimestamp();
            if(recordTimestamp <= mp3Grabber.getTimestamp()) break;
            recorder.record(frame);
            //timestamp -= mp3Grabber.getSampleRate();
        }
        long difference = recordTimestamp - mp3Grabber.getTimestamp();
        mp3Grabber.setTimestamp(0);
        //如果视频时长大于音频时长，则循环录入
        if(difference>0){
            audioEntry(frame,mp3Grabber,recorder,difference);
        }
        //音频录制完成，关闭
    }


    private static String createViewMp4(String mp4SavePath, List<File> fileList, int width, int height) throws Exception {
        String viewName="";

        //视频宽高最好是按照常见的视频的宽高  16：9  或者 9：16
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(mp4SavePath, width, height);
        //文件格式
        recorder.setFormat("mp4");
        //设置视频为25帧每秒
        recorder.setFrameRate(videoFrameRate);

        //设置视频编码层模式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

        //设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);

        try {
            recorder.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            for(File file : fileList){
                /*if(".mp3".equals(file.getName())){

                }*/
                BufferedImage read = null;
                try{
                    log.info("---------"+file.getName());
                    read = ImageIO.read(file);
                    for (int i=0;i<recorder.getFrameRate();i++){
                        recorder.record(converter.getFrame(read));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后一定要结束并释放资源
            recorder.stop();
            recorder.release();
        }
        return viewName;
    }


    private static void createMp4(String mp4SavePath, Map<Integer, File> imgMap, int width, int height) throws FrameRecorder.Exception {
        String viewName="";

        //视频宽高最好是按照常见的视频的宽高  16：9  或者 9：16
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(mp4SavePath, width, height);
        //文件格式
        recorder.setFormat("mp4");
        //设置视频为25帧每秒
        recorder.setFrameRate(videoFrameRate);

        //设置视频编码层模式
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);

        //设置视频图像数据格式
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);

        try {
            recorder.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            //录制一个22秒的视频
            for (int i = 0; i < 22; i++) {
                BufferedImage read = ImageIO.read(imgMap.get(i));
                //一秒是25帧 所以要记录25次
                for (int j = 0; j < 25; j++) {
                    recorder.record(converter.getFrame(read));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后一定要结束并释放资源
            recorder.stop();
            recorder.release();
        }
    }
}