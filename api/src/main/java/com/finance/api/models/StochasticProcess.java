package com.finance.api.models;
import com.finance.api.dto.Sample;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StochasticProcess<D extends Comparable, V extends Number> implements Iterable<TimeSeries<D, V>> {
    private List<TimeSeries<D, V>> realizations;

    public void add(TimeSeries<D, V> series){
        realizations.add(series);
    }

    public TimeSeries<D, V> getSample(int i){
        return realizations.get(i);
    }

    public List<D> getTime(){
        TimeSeries<D, V> series = realizations.stream().findAny().get();
        return series.getTimes();
    }

    public int sampleSize(){
        return realizations.size();
    }

    public int timeLength(){
        TimeSeries<D, V> series = realizations.stream().findAny().get();
        return series.size();
    }

    @Override
    public Iterator<TimeSeries<D, V>> iterator() {
        return realizations.iterator();
    }

    public TimeSeries<Integer, Double> mapAcrossTime(Function<TimeSeries<D,V>, Double> statistic){
        TimeSeries<Integer, Double> result = new TimeSeries<>();
        int k = 0;
        for(TimeSeries<D,V> xs : realizations){
            double estimate = statistic.apply(xs);
            result.add(k, estimate);
            ++k;
        }
        return result;
    }

    public List<V> valuesAtT(D t){
        return realizations.stream().map(xs -> xs.get(t)).collect(Collectors.toList());
    }

    public TimeSeries<Integer, Double> mapAcrossSpace(Function<TimeSeries<?, V>, Double> statistic){
        TimeSeries<Integer, Double> result = new TimeSeries<>();
        List<D> times = realizations.stream().findAny().get().getTimes();
        for(int i = 0; i < timeLength(); ++i){
            D t = times.get(i);
            TimeSeries<Integer, V> indexedSeries = TimeSeries.indexedSeries(valuesAtT(t));
            double estimated = statistic.apply(indexedSeries);
            result.add(i, estimated);
        }
        return result;
    }

    public List<List<Sample>> decomposeRealizationsOmegaFirst(int windowSize){
        return  realizations.
                stream().
                map(realization -> realization.windowDecomposition(windowSize).
                        stream().map(TimeSeries::toSample).
                        collect(Collectors.toList())).
                collect(Collectors.toList());
    }

    public List<List<Sample>> decomposeRealizationsTimeFirst(int windowSize){
        List<List<Sample>> omegaFirst = decomposeRealizationsOmegaFirst(windowSize);
        return invert(omegaFirst);
    }

    public static <T> List<List<T>> invert(List<List<T>> xs){
        int nRows = xs.size();
        int nCols = xs.get(0).size();
        List<List<T>> ys = new LinkedList<>();
        for(int i = 0; i < nCols; ++i){
            List<T> row = new LinkedList<>();
            for (List<T> x : xs) {
                T value = x.get(i);
                row.add(value);
            }
            ys.add(row);
        }
        return ys;
    }

    // public StochasticProcess<D, V> invertedProcess(){}

}
