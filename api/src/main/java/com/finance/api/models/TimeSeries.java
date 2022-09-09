package com.finance.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.stream.Collectors;

@Data
@ToString
@NoArgsConstructor
public class TimeSeries<D extends Comparable, V extends Number> {
    @JsonIgnore
    private List<D> times = new LinkedList<>();
    private Map<D, V> series = new HashMap<>();

    public void add(D time, V value){
        series.put(time, value);
        times.add(time);
        Collections.sort(times);
    }

    public V get(D time){
        return series.get(time);
    }

    public V get(int idx){
        D t = times.get(idx);
        return series.get(t);
    }

    public int size(){
        return series.size();
    }

    @JsonIgnore
    public TimeSeries<D, Double> getReturns(){
        TimeSeries<D, Double> returns = new TimeSeries<>();
        for(int i = 1; i < times.size(); ++i){
            D prev = times.get(i-1);
            D current = times.get(i);
            double prevValue = series.get(prev).doubleValue();
            double currentValue = series.get(current).doubleValue();
            double currentReturn = (currentValue - prevValue) / prevValue;
            returns.add(current, currentReturn);
        }
        return returns;
    }
}
