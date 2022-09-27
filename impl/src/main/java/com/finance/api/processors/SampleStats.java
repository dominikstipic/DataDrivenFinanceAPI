package com.finance.api.processors;

import com.finance.api.dto.Matrix;
import com.finance.api.dto.Sample;
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

    private static Matrix calculateAssociation(List<Sample> samples, boolean correlation){
        int n = samples.size();
        Matrix sigma = new Matrix(n, n);
        for(int i = 0; i < n; ++i){
            Sample xi = samples.get(i);
            for(int j = 0; j < n; ++j){
                Sample xj = samples.get(j);
                double value = correlation? xi.corr(xj) : xi.cov(xj);
                sigma.add(i, j, value);
            }
        }
        return sigma;
    }

    public static Matrix correlationMatrix(List<Sample> samples){
        return calculateAssociation(samples, true);
    }
    public static Matrix covarianceMatrix(List<Sample> samples){
        return calculateAssociation(samples, false);
    }

    public static double multiSampleVariance(List<Sample> samples){
        int n = samples.size();
        Matrix covMatrix = covarianceMatrix(samples);
        double var = 0.0;
        for(int i = 0; i < n-1; ++i){
            var += covMatrix.get(i,i);
            for(int j = i + 1; j < n; ++j){
                var += 2*covMatrix.get(i, j);
            }
        }
        return var;
    }


}
