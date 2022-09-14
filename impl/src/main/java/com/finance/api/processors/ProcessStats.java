package com.finance.api.processors;

import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessStats {

    public static final Function<TimeSeries<?, ?>, Double> timeAveraging
            = xs -> timeAveragingMoment(xs, 1);

    public static <D extends Comparable<D>, V extends Number> double timeAveragingMoment(TimeSeries<D, V> series, int n){
        double sum =  series.getValues().
                             stream().
                             mapToDouble(x -> Math.pow(x.doubleValue(), n)).
                             sum();
        int N = series.size();
        return sum / N;
    }

    public static <D extends Comparable<D>, V extends Number> TimeSeries<D, Double> ensembleMoment(StochasticProcess<D, V> process, int k){
        TimeSeries<D, Double> results = new TimeSeries<>();
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
