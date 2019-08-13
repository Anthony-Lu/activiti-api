package com.fairy.activiti.util;

import java.util.ArrayList;
import java.util.List;


public class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return org.springframework.util.StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(str);
    }

    public static String formatLike(String str) {
        if (isNotEmpty(str)) {
            return "%" + str + "%";
        }
        return "";
    }

    public static List<String> filterWhite(List<String> list) {
        List<String> resultList = new ArrayList<String>();
        for (String l : list) {
            if (isNotEmpty(l)) {
                resultList.add(l);
            }
        }
        return resultList;
    }

}
