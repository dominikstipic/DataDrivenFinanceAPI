package com.finance.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finance.api.dto.Sample;
import com.finance.api.serializers.TimeSeriesSerializer;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ToString
@NoArgsConstructor
@JsonSerialize(using = TimeSeriesSerializer.class)
public class TimeSeries<D extends Comparable> implements Iterator<Map.Entry<D, Double>>, Iterable<Map.Entry<D, Double>> {

    private int currentIteratorIndex = 0;
    @JsonIgnore
    private List<D> times = new LinkedList<>();

    private final Map<D, Double> series = new HashMap<>();

    //////////// METHODS /////////////

    public TimeSeries<D> copy(){
        TimeSeries<D> result = new TimeSeries<>();
        for(D t : times){
            Double value = series.get(t);
            result.add(t, value);
        }
        return result;
    }
    public TimeSeries(List<D> ts, List<Double> vs){
        times = ts;
        for(int i = 0; i < ts.size(); ++i){
            series.put(ts.get(i), vs.get(i));
        }
    }
    public static TimeSeries<Integer> indexedSeries(List<Double> values){
        List<Integer> idx = IntStream.range(0, values.size()).boxed().collect(Collectors.toList());
        return new TimeSeries<>(idx, values);
    }

    public void add(D time, Double value){
        series.put(time, value);
        times.add(time);
        Collections.sort(times);
    }

    public Double get(D time){
        return series.get(time);
    }

    public Double get(int idx){
        D t = times.get(idx);
        return series.get(t);
    }

    public int size(){
        return series.size();
    }

    @JsonIgnore
    public TimeSeries<D> getReturns(){
        TimeSeries<D> returns = new TimeSeries<>();
        for(int i = 1; i < times.size(); ++i){
            D prev = times.get(i-1);
            D current = times.get(i);
            double prevValue = series.get(prev);
            double currentValue = series.get(current);
            double currentReturn = (currentValue - prevValue) / prevValue;
            returns.add(current, currentReturn);
        }
        return returns;
    }

    public List<D> getTimes() {
        return times;
    }

    public List<Double> getValues() {
        return times.
                stream().
                map(series::get).
                collect(Collectors.toList());
    }

    public List<TimeSeries<D>> windowDecomposition(int windowSize) {
        List<TimeSeries<D>> series = new LinkedList<>();
        TimeSeries<D> current = new TimeSeries<>();
        int k = 0;
        for(D t : times){
            Double value = get(t);
            current.add(t, value);
            ++k;
            if(k == windowSize){
                series.add(current);
                current = new TimeSeries<>();
                k = 0;
            }
        }
        return series;
    }

    public List<TimeSeries<D>> uniformDecomposition(int n) {
        n = size() / n;
        return windowDecomposition(n);
    }

    public TimeSeries<Integer> indexedTimeSeries() {
        TimeSeries<Integer> indexed = new TimeSeries<>();
        int k = 0;
        for(D d : this.times){
            Double value = series.get(d);
            indexed.add(k, value);
            ++k;
        }
        return indexed;
    }

    public Sample toSample() {
        return new Sample(getValues().
                stream().
                map(Number::doubleValue).
                collect(Collectors.toList()));
    }

    public TimeSeries<D> doubleSeries(){
        TimeSeries<D> ts = new TimeSeries<>();
        for (D date : times) {
            double value = series.get(date);
            ts.add(date, value);
        }
        return ts;
    }

    ///////////////////// ITERATORS ///////////////////////////////
    @Override
    public boolean hasNext() {
        return currentIteratorIndex < this.times.size();
    }

    @Override
    public Map.Entry<D, Double> next() {
        if(hasNext()){
            D date = this.times.get(currentIteratorIndex);
            Double value = this.series.get(date);
            ++currentIteratorIndex;
            return Map.entry(date, value);
        }
        else{
            throw new NoSuchElementException("The iterator has no more elements!");
        }
    }

    @Override
    public Iterator<Map.Entry<D, Double>> iterator() {
        return this.copy();
    }
}
