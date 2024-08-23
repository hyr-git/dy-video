package com.shuyao.image;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.shuyao.image.dto.ImageDTO;
import com.shuyao.image.utils.ChineseWordUtils;
import com.shuyao.image.utils.JaccardSimilarityUtils;
import com.shuyao.image.utils.VideoFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/*****
 * 将文件夹中的图片随机生成N个文件夹，每个文件夹中的图片不重复
 */
@Slf4j
public class ImageRandomSelector {


    public static Map<String,List<String>> newFolderPictureMap = new HashMap<>();

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);


    //文件夹a、文件1使用1次，文件2使用1次，文件3使用1次
    //记录那个文件夹中的哪个图片使用次数
    public static Map<String,Map<String,Integer>> sourceFolderPictureUsedMap = new HashMap<>();

    private static int retryCount = 0;

    private static String PATH_FILE_SPLIT =  "#-#";

    private  boolean renameImageFlag = true;

    public static void main(String[] args) throws IOException {
        //读取指定文件的数据
        String sourceDirPath = null;
        String outDirPath = "d:\\showViewDir";
        String newDirFileName = null;
        int newViewFolders = 100;  // 假设 N 为 5，您可以根据实际情况修改
        double repeatRate = 0.5;  // 假设重复率为 50%，您可以根据实际情况修改
        File file = new File("D:\\showFile\\test.txt");
        if(!file.exists()){
            throw new ExportException("D:\\showFile\\test.txt 源文件不存在");
        }

        List<String> strings = FileUtil.readLines(file, "UTF-8");
        for (int i = 0; i <strings.size() ; i++) {
            String line = strings.get(i);
            log.info(line);

            if(line.startsWith("sourceDirPath=")){
                String[] split = line.split("=");
                sourceDirPath = split[1];
            }


            if(line.startsWith("outDirPath=")){
                String[] split = line.split("=");
                outDirPath = split[1];
            }

            if(line.startsWith("repeatRate=")){
                String[] split = line.split("=");
                if(split.length>1 && split[1] != null && !split[1].equals("")){
                    repeatRate = Double.parseDouble(split[1]);
                }
            }

            if(line.startsWith("newDirNum=")){
                String[] split = line.split("=");
                if(split.length>1 && split[1] != null && !split[1].equals("")){
                    newViewFolders = Integer.parseInt(split[1]);
                }
            }

            if(line.startsWith("newDirFileName=")){
                String[] split = line.split("=");
                if(split.length>1 && split[1] != null && !split[1].equals("")){
                    newDirFileName = split[1];
                }
            }


        }
        if(!StringUtils.hasLength(sourceDirPath)) {
            throw new ExportException("源文件[sourceDirPath=]源文件路径不存在");
        }

        ImageDTO   imageDTO = ImageDTO.builder().renameImageFlag(true).build();
        generateNewFolders(sourceDirPath,outDirPath,newDirFileName,newViewFolders, repeatRate);
    }



    public static void generateNewFolders(String sourceDirPath,String outDirPath,String newDirFileName,int newViewFolders, double repeatRate) throws IOException {

        ImageRandomSelector imageRandomSelector = new ImageRandomSelector();

        log.info("generateNewFolders: " + sourceDirPath);

        if(StringUtils.isEmpty(newDirFileName)){
            newDirFileName = DateUtil.format(new Date(), "yyyy-MM-dd-HH")+"-";
        }else{
            newDirFileName = DateUtil.format(new Date(), "MMdd")+"-"+newDirFileName;
        }

        //一、获取指定文件夹下的所有文件
        File folder = new File(sourceDirPath);
        if(folder == null || !folder.exists() || !folder.isDirectory()){
            log.error("文件夹为空: " + sourceDirPath);
        }

        File[] fileFolders = folder.listFiles();

        if (fileFolders == null) {
            log.error("文件夹为空: " + sourceDirPath);
            return ;
        }

        VideoFileUtils.sortFilesByNameNo(fileFolders);

        //二、按照指定数量生成指定的文件夹
        for (int i = 0; i < newViewFolders; i++) {
            List<String> randomImageFolders =  checkRepeatRandomImageFolders(fileFolders, newDirFileName+"("+(i+1)+")", repeatRate);

            //返回的数据为空，或者返回的数据小于指定的数量，则认为已经没有匹配的数据。
            if(CollectionUtil.isEmpty(randomImageFolders) || randomImageFolders.size() < fileFolders.length){
                log.error("已经不存在：randomImageFolders:{},i={}",randomImageFolders,i);
                break;
            }
        }

        //计算出重复的总
        int length = fileFolders.length;
        int repeatCount = (int) Math.ceil(length * repeatRate);


        //三、批量生产图片文件
        for (Map.Entry<String, List<String>> entry : newFolderPictureMap.entrySet()) {

            String newViewFolderName = entry.getKey();
            File fileDir = new File(outDirPath + "\\" + newViewFolderName);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }

            List<String> randomImageFolders = entry.getValue();
            log.info("newViewFolderName:{},values:{}",newViewFolderName,randomImageFolders);
            imageRandomSelector.createBatchRandomImageFolders(randomImageFolders,sourceDirPath,outDirPath,newViewFolderName);
        }
    }


    private void  createBatchRandomImageFolders (List<String> randomImageFolders,String sourceDirPath,String outDirPath,String newViewFolderName) {
        randomImageFolders.stream().forEach(item->{
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    //文件名称-图片名称
                    String[] split = item.split(PATH_FILE_SPLIT);
                    String tempName = split[0];
                    String tempImageName = split[1];

                    //log.info("tempName: " + tempName + " tempImageName: " + tempImageName);
                    //老的文件路径
                    File oldFile = new File(sourceDirPath + "\\" + tempName + "\\" , tempImageName);

                    int number = Integer.parseInt(tempName);


                    if(renameImageFlag){
                        tempImageName = ChineseWordUtils.replaceAndMatched(tempImageName);
                    }

                    //新的文件路径
                    File newFile = new File(outDirPath + "\\" + newViewFolderName + "\\" ,
                            String.format("%03d", number)+"-"+ tempImageName);
                    //文件复制
                    try {
                        FileCopyUtils.copy(oldFile, newFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(run);
        });
    }


    private void  createRandomImageFolders (List<String> randomImageFolders,String sourceDirPath,String outDirPath,String newViewFolderName) {
        randomImageFolders.stream().forEach(item->{
            //文件名称-图片名称
            String[] split = item.split(PATH_FILE_SPLIT);
            String tempName = split[0];
            String tempImageName = split[1];

            //log.info("tempName: " + tempName + " tempImageName: " + tempImageName);
            //老的文件路径
            File oldFile = new File(sourceDirPath + "\\" + tempName + "\\" , tempImageName);


            //新的文件路径
            File newFile = new File(outDirPath + "\\" + newViewFolderName + "\\" , tempImageName);
            //文件复制
            try {
                FileCopyUtils.copy(oldFile, newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /****
     * 校验是否重复
     * @param fileFolders
     * @param newFolderName
     * @param repeatRate
     * @return
     */
    public static List<String>  checkRepeatRandomImageFolders(File[] fileFolders,String newFolderName, double repeatRate) {
        List<String> randomImageFolders = checkAndGetRandomImageFoldersCopy(fileFolders, repeatRate);

        //返回的数据为空，或者返回的数据小于指定的数量，则认为已经没有匹配的数据。
        if(CollectionUtil.isEmpty(randomImageFolders) || randomImageFolders.size() < fileFolders.length){
            return randomImageFolders;
        }

        if(newFolderPictureMap.isEmpty()){
            newFolderPictureMap.put(newFolderName,randomImageFolders);
        }else{
            //计算出重复的总
            int length = fileFolders.length;
            int repeatCount = (int) Math.ceil(length * repeatRate);

            //校验去重率是否大于制定的值
            Optional<List<String>> optionalList = newFolderPictureMap.values().stream().filter(item ->
                            JaccardSimilarityUtils.calculateJaccard(item, randomImageFolders).compareTo(Double.valueOf(repeatRate)) > 0)
                    .findFirst();
            //重新计算
            if(!optionalList.isPresent()){
                newFolderPictureMap.put(newFolderName,randomImageFolders);
            }else{
                if(retryCount++ <= repeatCount*10000){
                    checkRepeatRandomImageFolders(fileFolders,newFolderName, repeatRate);
                }else{
                    throw new RuntimeException("文件夹下图片重复次数超过指定次数："+repeatCount+",已经生成多次："+newFolderPictureMap.values().size());
                }
            }
        }
        /*if(newFolderPictureMap.isEmpty()){
            newFolderPictureMap.put(newFolderName,randomImageFolders);
        }else{
*/
        //防止map中的value过大导致后续报错。
        //subListCheckRepeat(fileFolders,randomImageFolders,newFolderName,repeatRate);
           /* //校验去重率是否大于制定的值
            Optional<List<String>> optionalList = newFolderPictureMap.values().stream().filter(item ->
                    JaccardSimilarityTool.calculateJaccard(item, randomImageFolders).compareTo(Double.valueOf(repeatRate)) > 0)
                    .findFirst();
            //重新计算
            if(!optionalList.isPresent()){
                newFolderPictureMap.put(newFolderName,randomImageFolders);
            }else{
                checkRepeatRandomImageFolders(fileFolders,newFolderName, repeatRate);
            }
        }*/
        return randomImageFolders;
    }


    public static List<String>  checkAndGetRandomImageFolders(File[] fileFolders, double repeatRate) {

        Map<Integer, String> selectedImageMap = new HashMap<>();
        Random random = new Random();


        //计算出重复的总
        int length = fileFolders.length;

        //向上取整获取总记录数
        int repeatCount = (int) Math.ceil(length * repeatRate);



        // 如果是第一次测时候需要进行查询处理，后面每次则只需要进行随机选择即可
        //记录选择的文件夹-图片名称
        List<String> selectedFolderImageNoList = new ArrayList<>();

        String currentFolderName = "";
        //遍历每个文件夹，随机选择一个图片
        for (int i = 1; i <= fileFolders.length; i++) {
            File currentFolder = fileFolders[i-1];
            currentFolderName = currentFolder.getName();
            //flag = true，表示不进行文件类型的校验
            //List<File> imageList = getImageFiles(currentFolder,true);
            List<File> imageList = getImageFiles(currentFolder, false);
            if (imageList.isEmpty()) {
                log.info("文件夹 " + currentFolder + " 中没有图片！");
                continue;
            }

            //TODO 优化随机函数
            int selectedIndex = random.nextInt(imageList.size());
            File selectedImage = imageList.get(selectedIndex);

            String selectedImageName = selectedImage.getName();

            Map<String, Integer> imageUsedMap = sourceFolderPictureUsedMap.getOrDefault(currentFolderName, new HashMap<>());
            if(imageUsedMap.isEmpty()){
                imageUsedMap.put(selectedImageName, 1);
                sourceFolderPictureUsedMap.put(currentFolderName, imageUsedMap);
            }else{
                //已经存在对应文件夹下的图片，则进行计数
                Integer fileCount = imageUsedMap.getOrDefault(selectedImageName, 0);
                //如果没有超过指定的重复次数,则计数+1
                if(fileCount< repeatCount){
                    imageUsedMap.put(selectedImageName, fileCount+1);
                    sourceFolderPictureUsedMap.put(currentFolderName, imageUsedMap);
                }else {
                    //如果超过指定的重复次数，则重新选择没有选择过的一个文件
                    Optional<File> optional = imageList.stream()//.map(item -> item.getName())
                            .filter(item -> !imageUsedMap.containsKey(item.getName())).findFirst();
                    if(optional.isPresent()){
                        selectedImage = optional.get();
                        imageUsedMap.put(selectedImage.getName(), 1);
                        sourceFolderPictureUsedMap.put(currentFolderName, imageUsedMap);
                    }else {
                        Optional<Map.Entry<String, Integer>> entryOptional = imageUsedMap.entrySet().stream()
                                .filter(item -> item.getValue() < repeatCount).findFirst();

                        //获取使用数量最少的一个文件
                        if(entryOptional.isPresent() ){
                            selectedImageName = entryOptional.get().getKey();
                            imageUsedMap.put(selectedImage.getName(), 1);
                            sourceFolderPictureUsedMap.put(currentFolderName, imageUsedMap);
                        }else{
                            log.info("@@"+sourceFolderPictureUsedMap.toString());
                            sourceFolderPictureUsedMap.entrySet().forEach(item->{
                                Map<String, Integer> itemMap = item.getValue();
                                List<Map.Entry<String, Integer>> collect = itemMap.entrySet().stream()
                                        .filter(it -> it.getValue() > repeatCount*repeatCount).collect(Collectors.toList());
                                log.info("@@=erererr==="+collect.toString());
                                int size = newFolderPictureMap.values().size();
                                if(collect.size()>repeatCount*10){
                                    //单个超过4次的直接过滤
                                    throw new RuntimeException("文件夹下图片重复次数超过指定次数："+size);
                                }
                            });
                            //校验通过之后才会加否则抛出异常，
                            imageUsedMap.put(selectedImageName, fileCount+1);
                            sourceFolderPictureUsedMap.put(currentFolderName, imageUsedMap);
                           /* int selectedIndex01 = random.nextInt(imageList.size());
                            File selectedImage01 = imageList.get(selectedIndex01);

                            imageUsedMap.put(selectedImage01.getName(),  imageUsedMap.get(selectedImage01.getName())+1);
                            sourceFolderPictureUsedMap.put(currentFolderName, imageUsedMap);*/

                            /*//重复使用的文件大于4则直接提示
                            List<Map<String, Integer>> mapList = sourceFolderPictureUsedMap.values().stream().filter(item ->
                                    item.values().stream().filter(it -> it >= repeatCount).count() > repeatCount*4).collect(Collectors.toList());
                            log.info("@@===="+mapList.toString());
                            int size = selectedFolderImageNoList.size();
                            if(null != mapList){
                                //单个超过4次的直接过滤
                                throw new RuntimeException("文件夹下图片重复次数超过指定次数");
                            }*/


                           /* //重复使用的文件大于4则直接提示
                            List<Map<String, Integer>> mapList = sourceFolderPictureUsedMap.values().stream().filter(item ->
                                    item.values().stream().filter(it -> it >= repeatCount).count() > repeatCount*4).collect(Collectors.toList());
                            log.info("@@===="+mapList.toString());
                            int size = selectedFolderImageNoList.size();
                            if(null != mapList){
                                //单个超过4次的直接过滤
                                throw new RuntimeException("文件夹下图片重复次数超过指定次数,能够生成的图片文件数量为："+size);
                            }else{
                                imageUsedMap.put(selectedImage.getName(),  fileCount+1);
                                sourceFolderPictureUsedMap.put(currentFolderName, imageUsedMap);
                            }*/
                        }
                    }
                }
            }
            //存在则校验数据的次数是否已经达到阈值，否则重新选择

          /*  // 检查重复率
            if (isImageNameRepeated(selectedImageMap, selectedImageName, repeatRate*3)) {
                i--;  // 重新选择当前文件夹的图片
                continue;
            }

            selectedImageMap.put(i, selectedImageName);*/

            // 将选中的图片复制到目标文件夹
            //将当前选择的文件目录以及图片路径放入list中
            selectedFolderImageNoList.add(currentFolderName + PATH_FILE_SPLIT + selectedImageName);
        }
        return selectedFolderImageNoList;
    }


    public static List<String>  checkAndGetRandomImageFoldersCopy(File[] fileFolders, double repeatRate) {

        Map<Integer, String> selectedImageMap = new HashMap<>();
        Random random = new Random();


        //记录选择的文件夹-图片名称
        List<String> selectedFolderImageNoList = new ArrayList<>();
        File currentFolder = null;
        List<File> imageList = null;
        //遍历每个文件夹，随机选择一个图片
        for (int i = 1; i <= fileFolders.length; i++) {
            currentFolder = fileFolders[i-1];

            //flag = true，表示不进行文件类型的校验
            imageList = getImageFiles(currentFolder,true);
            //List<File> imageList = getImageFiles(currentFolder, false);

            if (imageList.isEmpty()) {
                log.info("文件夹 " + currentFolder + " 中没有图片！");
                continue;
            }

            //TODO 优化随机函数
            int selectedIndex = random.nextInt(imageList.size());
            File selectedImage = imageList.get(selectedIndex);

            // 检查重复率
            if (isImageNameRepeated(selectedImageMap, selectedImage.getName(), repeatRate*3)) {
                i--;  // 重新选择当前文件夹的图片
                continue;
            }

            selectedImageMap.put(i, selectedImage.getName());

            // 将选中的图片复制到目标文件夹
            //将当前选择的文件目录以及图片路径放入list中
            selectedFolderImageNoList.add(currentFolder.getName() + PATH_FILE_SPLIT + selectedImage.getName());
        }
        return selectedFolderImageNoList;
    }


    private static List<File> getImageFiles(File folder,Boolean flag) {
        if(flag){
            File[] files = folder.listFiles();
            if (files!= null) {
                return Arrays.stream(files).filter(file -> !file.isDirectory()).collect(Collectors.toList());
            }
            return new ArrayList<>();
        }else {
            return getImageFiles(folder);
        }
    }

    private static List<File> getImageFiles(File folder) {
        List<File> imageFiles = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files!= null) {
            for (File file : files) {
                if (isImageFile(file)) {
                    imageFiles.add(file);
                }
            }
        }
        return imageFiles;
    }

    private static boolean isImageFile(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif");
    }

    private static boolean isImageNameRepeated(Map<Integer, String> selectedImageNames, String selectedImageName, double repeatRate) {
        int countNum = 0;
        //long countNum = selectedImageNames.values().stream().filter(item -> item.equals(selectedImageName)).count();//.collect(Collectors.toList());
        for (String image : selectedImageNames.values()) {
            if (image.equals(selectedImageName)) {
                countNum++;
            }
        }
        return (double) countNum / selectedImageNames.size() > repeatRate*8;
    }

    private static boolean isRepeated(Map<Integer, File> selectedImages, File selectedImage, double repeatRate) {
        int count = 0;
        for (File image : selectedImages.values()) {
            if (image.equals(selectedImage)) {
                count++;
            }
        }
        return (double) count / selectedImages.size() > repeatRate;
    }

}