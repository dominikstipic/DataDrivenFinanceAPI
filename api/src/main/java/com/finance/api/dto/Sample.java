package com.finance.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sample implements Iterable<Double>{
    private List<Double> sample = new LinkedList<>();

    public Sample(Number ... ds){
        for(Number d : ds)
            sample.add(d.doubleValue());
    }

    public static final BiFunction<Sample, Integer, Sample> powerSample
            = (sample, n) -> sample.mapped(d -> Math.pow(d, n));

    public void add(double d){
        sample.add(d);
    }

    public int size(){
        return sample.size();
    }

    public void map(Function<Double, Double> mapper){
        sample = sample.stream().map(mapper).collect(Collectors.toList());
    }
    public Sample mapped(Function<Double, Double> mapper){
        Sample newSample = new Sample(this.sample);
        newSample.map(mapper);
        return newSample;
    }
    public double sum(){
        return sample.stream().reduce(0.0, Double::sum);
    }
    public double mean(){
        return sum()/size();
    }

    public double var(){
        double secondMoment = powerSample.apply(this, 2).mean();
        double mean = mean();
        return secondMoment - mean*mean;
    }

    public double get(int idx){
        return sample.get(idx);
    }

    public double std(){
        return Math.sqrt(var());
    }
    public void centralize(){
        double mean = mean();
        mapped(d -> d - mean);
    }

    public Sample centralized(){
        Sample sample = new Sample(this.sample);
        sample.centralize();
        return sample;
    }

    public void normalize(){
        double m = mean();
        double s = std();
        map(d -> (d - m)/s);
    }

    public Sample normalized(){
        Sample sample = new Sample(this.sample);
        sample.normalize();
        return sample;
    }

    public Sample crossProduct(Sample other){
        Sample result = new Sample();
        for(double d1 : this){
            for(double d2 : other){
                result.add(d1*d2);
            }
        }
        return result;
    }

    public double cov(Sample other){
        double m1 = mean();
        double m2 = other.mean();
        double sum = 0;
        for(int i = 0; i < other.size(); ++i){
            sum += get(i)*other.get(i);
        }
        double M = sum / other.size();
        return M - m1*m2;
    }

    public double corr(Sample other){
        double S1 = std();
        double S2 = other.std();
        double cov = cov(other);
        return cov/(S1*S2);
    }

    @Override
    public Iterator<Double> iterator() {
        return sample.iterator();
    }

}
