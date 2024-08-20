package com.shuyao.image.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseWordUtils {

    private static String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正
    private static Pattern PATTERN = Pattern.compile(REGEX_CHINESE) ;


    public static Matcher wordMatched(String word){
        return PATTERN.matcher(word) ;
    }

    public static boolean isWordMatched(String word){
        Matcher match = wordMatched(word) ;
        return match.find();
    }

    public static String replaceAndMatched(String word){
        Matcher match = wordMatched(word) ;
        if(match.find()){
            return match.replaceAll("");
        }
        return word;
    }


    public static void main(String[] args) {
        String str = "nihaohzhong过osho";

        System.out.println(replaceAndMatched(str));
    }
}
