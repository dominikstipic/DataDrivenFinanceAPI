package com.finance.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Matrix {
    private double[][] xss;

    public Matrix(List<List<Double>> xs) {
        int nRows = xs.size();
        int nCols = xs.get(0).size();
        this.xss = new double[nRows][nCols];
    }

    public Matrix(int nRows, int nCols) {
        this.xss = new double[nRows][nCols];
    }

    public int nRows(){
        return xss.length;
    }

    public int nCols(){
        return xss[0].length;
    }

    public void add(int i, int j, double value){
        xss[i][j] = value;
    }

    @JsonIgnore
    public List<List<Double>> getAsList() {
        List<List<Double>> ds = new ArrayList<>();
        for(int i = 0; i < nRows(); ++i){
            List<Double> xs = new ArrayList<>();
            for(int j = 0; j < nCols(); ++j){
                xs.add(xss[i][i]);
            }
            ds.add(xs);
        }
        return ds;
    }

}
