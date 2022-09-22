package com.finance.api.services;

import com.finance.api.dto.Estimator;
import com.finance.api.dto.Matrix;
import com.finance.api.dto.Sample;
import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;
import com.finance.api.processors.ProcessStats;
import com.finance.api.processors.SampleStats;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataProcessingServiceImpl<T extends Comparable> implements DataProcessingService<T>{

    @Override
    public TimeSeries<T> assetReturns(TimeSeries<T> series) {
        return series.getReturns();
    }

    @Override
    public TimeSeries<T> meanEnsembleAverage(StochasticProcess<T> process) {
        return ProcessStats.ensembleMoment(process, 1);
    }

    @Override
    public Estimator<Double> timeAverage(TimeSeries<T> series) {
        double value = ProcessStats.timeAveraging.apply(series);
        return new Estimator<>("timeAverage", List.of(value));
    }

    @Override
    public TimeSeries<Integer> timeAverage(TimeSeries<? extends Comparable> series, int windowSize) {
        List<? extends TimeSeries<? extends Comparable>> decomposed = series.windowDecomposition(windowSize);
        List<TimeSeries<Integer>> indexed = decomposed.stream().map(TimeSeries::indexedTimeSeries).collect(Collectors.toList());
        StochasticProcess<Integer> process = new StochasticProcess<>(indexed);
        Function<TimeSeries<? extends Comparable>, Double> mean = ts -> ts.toSample().mean();
        return process.mapAcrossSpace(mean);
    }

    @Override
    public Estimator<Matrix> calcCorrelation(StochasticProcess<T> process, int windowSize) {
        List<List<Sample>> map = process.decomposeRealizationsTimeFirst(windowSize);
        List<Matrix> matrix = new LinkedList<>();
        for (List<Sample> ithSamples : map) {
            Matrix corr = SampleStats.pearsonCorrelation(ithSamples);
            matrix.add(corr);
        }
        return new Estimator<>("correlations", matrix);
    }

    @Override
    public Estimator<Matrix> calcCorrelation(StochasticProcess<T> process) {
        int T = process.timeLength();
        return calcCorrelation(process, T);
    }
}
