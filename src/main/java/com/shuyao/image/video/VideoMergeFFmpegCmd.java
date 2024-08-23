package com.shuyao.image.video;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.shuyao.image.utils.VideoFileUtils;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.util.StringUtils;
import ws.schild.jave.DefaultFFMPEGLocator;
import ws.schild.jave.FFMPEGExecutor;

import java.io.*;
import java.rmi.server.ExportException;
import java.util.List;
import java.util.stream.Collectors;


/*****
 * 当前视频文件中执行cmd命令，合并视频文件
 * (for %I in (*.mp4) do @echo file '%I') > files.txt && ffmpeg -f concat -safe 0 -i files.txt -c copy D:\showFile\12output.mp4
 */
@Slf4j
public class VideoMergeFFmpegCmd {

    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String USERDIR = System.getProperty("user.dir");

    /**
     * ffmpeg -i concat a.mp4|b.mp4 -C copy out .mp4
     * 只会适用于ts和f1v等一 些格式。mp4格式整体有一 层容器，而不像ts这类格式可以直接拼接， 需要先解开容器再对提职的视频流进行拼接
     * 因此使用ffmpeg -f concat -safe 0 -i my1ist.txt -c copy output
     *
     * ffmpeg-amd64-2.7.3.exe -f concat -safe 0 -i D:\私活\dy-viedo\5b2797a6-6ff8-429f-a821-06aa00fb1e68temp.txt -c copy D:\showFile-out\0819-0989(1).mp4
     *
     * ffmpeg -f concat -safe 0 -i files.txt -c copy D:\showFile\12output.mp4
     */
    public static boolean concatVideo(List<String> files, String outputFile) throws IOException {
        //地址放在txt文件
        String txtFile = USERDIR + SEPARATOR + UUID.fastUUID() +"temp.txt";
        String txtPath = createConcatTxtFile(files, txtFile);

      /*  FFmpeg ffmpeg = new FFmpeg("D:\\Program Files\\ffmpeg7\\bin\\ffmpeg.exe"); // 替换为FFmpeg可执行文件的路径
        FFprobe ffprobe = new FFprobe("D:\\Program Files\\ffmpeg7\\bin\\ffprobe.exe"); // 替换为FFprobe可执行文件的路径
*/

        BufferedReader reader = null;

        //TODO 使用的是默认的，也可以使用自定义的ffmpeg 和ffprobe
        FFMPEGExecutor executor = new DefaultFFMPEGLocator().createExecutor();
        try {
            executor.addArgument("-f");
            executor.addArgument("concat");
            executor.addArgument("-safe");
            executor.addArgument("0");
            executor.addArgument("-i");
            executor.addArgument(txtPath);
            executor.addArgument("-c");
            executor.addArgument("copy");
            executor.addArgument(outputFile);
            executor.execute();
            reader = new BufferedReader(new InputStreamReader(executor.getErrorStream()));
            String msg = reader.lines().collect(Collectors.joining("\n"));
            log.info(msg);
        } catch (ExportException e) {
            log.error("合并视频失败", e);
            return false;
        } finally {
            if(null != reader){
                reader.close();
            }
        }


        File newFile = new File(outputFile);
        if (newFile.exists()) {
            //删除txt
            FileUtil.del(txtFile);
            return true;
        }
        FileUtil.del(txtFile);
        return false;
    }

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
            concatVideo(filePathList, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*****
     * 批量的读取多个文件夹，为每个文件夹生成一个视频文件
     * @param videoSourceParentFolder 视频文件所在的目录 "D:\\showFile\\viedu";
     * @return outputFileFolder 输出的文件路径 "D:\\showFile"; // 替换为输出文件的路径
     * @throws IOException
     */
    public static void createBatchVideoByFolder(String videoSourceParentFolder, String outputFileFolder) throws IOException {
        File fileParent = new File(videoSourceParentFolder);
        if (null != fileParent && fileParent.isDirectory()) {
            File[] files = fileParent.listFiles();
            String videoDirectorySource = null;
            if (StringUtils.endsWithIgnoreCase(outputFileFolder, ".mp4")) {
                outputFileFolder = outputFileFolder.replaceAll(".mp4", "") + File.separator;
            }

            File outSaveFolder = new File(outputFileFolder);
            if (!outSaveFolder.exists()) {
                outSaveFolder.mkdirs();
            }

            if (null != files && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        videoDirectorySource = file.getAbsolutePath();
                        createSimpleVideoByFolder(videoDirectorySource, outputFileFolder + File.separator + file.getName() + ".mp4");
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {

        String videoSourceParentFolderBatch = "D:\\showFile\\video21";
        String outputFilePathBatch = "D:\\showFile-out";

        boolean batchFlag = true;

        File file = new File("D:\\showFile\\videoBatch.txt");
        if (!file.exists()) {
            throw new ExportException("D:\\showFile\\videoBatch.txt 源文件不存在");
        }

        List<String> strings = FileUtil.readLines(file, "UTF-8");
        for (int i = 0; i < strings.size(); i++) {
            String line = strings.get(i);
            log.info(line);

            if (line.startsWith("videoSourceParentFolderBatch=")) {
                String[] split = line.split("=");
                videoSourceParentFolderBatch = split[1];
            }


            if (line.startsWith("outputFilePathBatch=")) {
                String[] split = line.split("=");
                outputFilePathBatch = split[1];
            }

            if (line.startsWith("batchFlag=")) {
                String[] split = line.split("=");
                if (split.length > 1 && split[1] != null && !split[1].equals("")) {
                    batchFlag = Boolean.valueOf(split[1]);
                }
            }

        }

        if (!StringUtils.hasLength(videoSourceParentFolderBatch)) {
            throw new ExportException("源文件[videoSourceParentFolderBatch=]源文件路径不存在");
        }

        if (!StringUtils.hasLength(outputFilePathBatch)) {
            throw new ExportException("输出文件[outputFilePathBatch=]文件路径不存在");
        }

        createBatchVideoByFolder(videoSourceParentFolderBatch, outputFilePathBatch);
    }

    /***
     * JAVA代码调用ffmpeg程序进行视频转码和推流
     * https://blog.csdn.net/nuoya989/article/details/135522114
     * @throws IOException
     */
    public static void dg() throws IOException {
        //创建FFmpeg对象
        FFmpeg ffmpeg = new FFmpeg("D:\\ffmpeg\\bin\\ffmpeg.exe");
        //参数是: ffmpeg命 令工具的安装路径
        //创建FFmpegBuilder对象，设置谁说/转码参数
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput("D:\\111. mp4")
        //输入视频源地址
                .overrideOutputFiles(true)
        //设置是否覆盖已存在的输出文件
                .addOutput("D:\\hls\\test . m3u8")
        //输出说地址
                .setFormat("hls")
        //網出流格式-f hls
                .setAudioCodec("copy")
        //设置音频转码copy 表示不转码
                .setVideoCodec("libx264")
        //设置视频转码copy 表示不转码
                .setVideoQuality(1)
        //设置视频质量，- -般为1路5(1代表质量最高)
                .setVideoFrameRate(25)
        //设置视频帧率
                .addExtraArgs("-t", "30")
        //设置推滤恃续时间
                .addExtraArgs("-hls_ list_ size", "5")
        //设置输出文件个数(hls 输出流特有的参数)
                .addExtraArgs("-hls_ flags", "50")
        //设置输出文件替换(hls 输出说特有的参数)
                .addExtraArgs("-hls_ time", "6")
        //设置输出文件每个时长多少秒hls納出说特有的参数)
                .done();
        //最后调用done()完成参数配置
        //创健FFmpegExecutor对象，用于执行ffpmeg转流任务
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        //执行ffpmeg转流任务，并添加J监听器Progressl istener,可以监听ffpmeg运行状态情况
        executor.createJob(builder, new ProgressListener() {
            @Override
            public void progress(Progress progress) {
                System.out.println("------------");
                System.out.println(progress);
            }
        }).run();
    }
}
