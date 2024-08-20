package com.shuyao.image.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JaccardSimilarityUtils {

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

        Bag<String> bagB = new HashBag<>();
        bagB.addAll(pointList2);
        return calculateJaccard(bagA,bagB);
    }
}