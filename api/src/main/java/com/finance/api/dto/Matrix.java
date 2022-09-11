package com.finance.api.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Matrix {

    private String name;

    private List<List<Double>> X;

    public Matrix(List<List<Double>> xs) {
        this.X = xs;
    }

    public Matrix(int nRows) {
        for(int i = 0; i < nRows; ++i){
            for(int j = 0; j < nRows; ++j){
                X.add(new ArrayList<>());
            }
        }
    }

    public void addRow(List<Double> row){
        X.add(row);
    }

    public List<List<Double>> getX() {
        return X;
    }

}
