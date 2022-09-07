package com.finance.api.utils;

import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static <T> List<T> toList(T ... ts){
        List<T> list = new LinkedList<>();
        for(T t : ts)
            list.add(t);
        return list;
    }
}
