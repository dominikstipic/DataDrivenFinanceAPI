package com.finance.api.processors;

import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessStats {

    public static final Function<TimeSeries<? extends Comparable>, Double> timeAveragingMean
            = xs -> timeAveragingMoment(xs, 1);

    public static <D extends Comparable<D>> double timeAveragingMoment(TimeSeries<D> series, int n){
        double sum;
        if(n > 1){
            sum =  series.getValues().
                    stream().
                    mapToDouble(x -> Math.pow(x, n)).
                    sum();
        }
        else{
            sum = series.getValues().stream().reduce(0.0, Double::sum);
        }
        int N = series.size();
        return sum / N;
    }

    public static <D extends Comparable<D>> TimeSeries<D> ensembleMoment(StochasticProcess<D> process, int k){
        TimeSeries<D> results = new TimeSeries<>();
        int N = process.sampleSize();
        for(D t : process.getTime()){
            List<Double> samplesAtTimeT = process.valuesAtT(t).
                    stream().
                    map(Number::doubleValue).
                    collect(Collectors.toList());
            double sum = samplesAtTimeT.stream().reduce(0.0, (a,b) -> Math.pow(a, k) + Math.pow(b, k));
            double mean = sum / N;
            results.add(t, mean);
        }
        return results;
    }

}
