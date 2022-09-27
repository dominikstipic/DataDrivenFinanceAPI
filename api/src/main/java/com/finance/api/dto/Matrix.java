package com.finance.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Matrix {
    private double[][] data;

    public Matrix(int nRows, int nCols) {
        this.data = new double[nRows][nCols];
    }

    public int nRows(){
        return data.length;
    }

    public int nCols(){
        return data[0].length;
    }

    public void add(int i, int j, double value){
        data[i][j] = value;
    }

    public double get(int i, int j){
        return data[i][j];
    }

    @JsonIgnore
    public List<List<Double>> getAsList() {
        List<List<Double>> ds = new ArrayList<>();
        for(int i = 0; i < nRows(); ++i){
            List<Double> xs = new ArrayList<>();
            for(int j = 0; j < nCols(); ++j){
                xs.add(data[i][i]);
            }
            ds.add(xs);
        }
        return ds;
    }

}
