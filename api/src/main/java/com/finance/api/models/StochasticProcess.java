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
public class StochasticProcess<D extends Comparable> implements Iterable<TimeSeries<D>> {
    private List<TimeSeries<D>> realizations;

    public void add(TimeSeries<D> series){
        realizations.add(series);
    }

    public TimeSeries<D> getSample(int i){
        return realizations.get(i);
    }

    public List<D> getTime(){
        TimeSeries<D> series = realizations.stream().findAny().get();
        return series.getTimes();
    }

    public int sampleSize(){
        return realizations.size();
    }

    public int timeLength(){
        TimeSeries<D> series = realizations.stream().findAny().get();
        return series.size();
    }

    @Override
    public Iterator<TimeSeries<D>> iterator() {
        return realizations.iterator();
    }

    public TimeSeries<Integer> mapAcrossTime(Function<TimeSeries<D>, Double> statistic){
        TimeSeries<Integer> result = new TimeSeries<>();
        int k = 0;
        for(TimeSeries<D> xs : realizations){
            double estimate = statistic.apply(xs);
            result.add(k, estimate);
            ++k;
        }
        return result;
    }

    public List<Double> valuesAtT(D t){
        return realizations.stream().map(xs -> xs.get(t)).collect(Collectors.toList());
    }

    public TimeSeries<Integer> mapAcrossSpace(Function<TimeSeries<? extends Comparable>, Double> statistic){
        TimeSeries<Integer> result = new TimeSeries<>();
        List<D> times = realizations.stream().findAny().get().getTimes();
        for(int i = 0; i < timeLength(); ++i){
            D t = times.get(i);
            TimeSeries<Integer> indexedSeries = TimeSeries.indexedSeries(valuesAtT(t));
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

    public StochasticProcess<D> applyOnTimeSeries(Function<TimeSeries<D>, TimeSeries<D>> function){
        List<TimeSeries<D>> result = new LinkedList<>();
        for(TimeSeries<D> ts : realizations){
            TimeSeries<D> mapped = function.apply(ts);
            result.add(mapped.doubleSeries());
        }
        return new StochasticProcess<>(result);
    }

    // public StochasticProcess<D, V> invertedProcess(){}

}
