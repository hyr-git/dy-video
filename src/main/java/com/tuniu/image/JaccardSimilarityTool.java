package com.tuniu.image;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JaccardSimilarityTool {

    public static double calculateJaccard(Bag<String> bag1, Bag<String> bag2) {
        Bag<String> union = new HashBag<>(bag1);
        union.addAll(bag2);
        Bag<String> intersection = new HashBag<>(bag1);
        intersection.retainAll(bag2);
        return intersection.size() / (double)union.size()*2;
    }

    public static Double calculateJaccard(List<String> pointList, List<String> pointList2) {
        Bag<String> bagA = new HashBag<>();
        bagA.addAll(pointList);
       /* pointList.stream().forEach(itemA->{
            bagA.add(String.valueOf(itemA.hashCode()));
        });*/

        Bag<String> bagB = new HashBag<>();
        bagB.addAll(pointList2);
        /*pointList2.stream().forEach(itemB->{
            bagB.add(String.valueOf(itemB.hashCode()));
        });*/
        return calculateJaccard(bagA,bagB);
    }

    public static void main(String[] args) {


        List<String> pointList = Arrays.asList("1-1.png","2-2.png","3-1.png","4-1.png","5-1.png","6-1.png","7-1.png","8-1.png","9-1.png");
        List<String> randomImageFolders = Arrays.asList("1-11.png","2-21.png","3-11.png","4-11.png","5-21.png","6-21.png","7-21.png","8-21.png","9-21.png","10-2.png");



        List<List<String>> allList = new ArrayList<>();


        for(int i=1; i<=1000; i++){
            pointList.stream().forEach(item->{
                item=item+"-";
            });
            allList.add(pointList);
        }

        boolean repeatFlag = false;

        for (List<String> list : allList) {
            if(JaccardSimilarityTool.calculateJaccard(list, randomImageFolders).compareTo(Double.valueOf(0.2)) > 0){
                repeatFlag = true;
                break;
            }
        }


        if(!repeatFlag){
            log.info("success");
        }else{
            log.info("falie");
        }







       /* int size = pointList.size();
        int pageSize  = 5;
        int pageNum  = (int)Math.ceil(size/Double.valueOf(pageSize));
        for (int i = 1; i <= pageNum; i++) {
            //小于100
            if(i < pageNum){
                List<String> sub = CollectionUtil.sub(pointList, (i-1) * pageSize, i*pageSize);
                allList.add(sub);
            }else {
                List<String> sub = CollectionUtil.sub(pointList, (i-1) * pageSize, size);
                allList.add(sub);
            }
        }

        double similarity = calculateJaccard(pointList, pointList2);
        System.out.println("Jaccard Similarity: " + similarity);*/
    }

}