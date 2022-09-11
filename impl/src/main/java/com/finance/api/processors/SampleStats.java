package com.finance.api.processors;

import com.finance.api.dto.Matrix;
import com.finance.api.dto.Sample;
import java.util.LinkedList;
import java.util.List;

public class SampleStats {

    public static double pearsonCorrelation(Sample xs, Sample ys){
        return xs.corr(ys);
    }

    public static Matrix pearsonCorrelation(List<Sample> xs){
        Matrix result = new Matrix(xs.size(), xs.size());
        for(int i = 0; i < xs.size(); ++i){
            for(int j = 0; j < xs.size(); ++j){
                Sample a = xs.get(i);
                Sample b = xs.get(j);
                double corr = a.corr(b);
                result.add(i, j, corr);
            }
        }
        return result;
    }


}
