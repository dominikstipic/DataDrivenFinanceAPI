package com.finance.api.utils;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static <T> List<List<T>> invert(List<List<T>> xs){
        int nRows = xs.size();
        int nCols = xs.get(0).size();
        List<List<T>> ys = new LinkedList<>();
        for(int i = 0; i < nRows; ++i){
            List<T> row = new LinkedList<>();
            for(int j = 0; j < nCols; ++j){
                T value = xs.get(j).get(i);
                row.add(value);
            }
            ys.add(row);
        }
        return ys;
    }
    @SafeVarargs
    public static <T> List<T> toList(T ... ts){
        return new LinkedList<>(Arrays.asList(ts));
    }
}
