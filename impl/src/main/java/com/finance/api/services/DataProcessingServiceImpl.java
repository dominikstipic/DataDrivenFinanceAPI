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

public class DataProcessingServiceImpl<T extends Comparable, P extends Comparable> implements DataProcessingService<T, P>{

    @Override
    public TimeSeries<T, Double> assetReturns(TimeSeries<T, Number> series) {
        return series.getReturns();
    }

    @Override
    public TimeSeries<P, Double> meanEnsembleAverage(StochasticProcess<P, Number> process) {
        return ProcessStats.ensembleMoment(process, 1);
    }

    @Override
    public Estimator<Double> timeAverage(TimeSeries<T, Number> series) {
        double value = ProcessStats.timeAveraging.apply(series);
        return new Estimator<>("timeAverage", List.of(value));
    }

    @Override
    public TimeSeries<Integer, Double> timeAverage(TimeSeries<T, Number> series, int windowSize) {
        List<TimeSeries<T, Number>> decomposed = series.windowDecomposition(windowSize);
        List<TimeSeries<Integer, Number>> indexed = decomposed.stream().map(TimeSeries::indexedTimeSeries).collect(Collectors.toList());
        StochasticProcess<Integer, Number> process = new StochasticProcess<>(indexed);
        Function<TimeSeries<?, Number>, Double> mean = ts -> ts.toSample().mean();
        return process.mapAcrossSpace(mean);
    }

    @Override
    public Estimator<Matrix> calcCorrelation(StochasticProcess<T, Number> process, int windowSize) {
            List<List<Sample>> map = process.decomposeRealizationsTimeFirst(windowSize);
            List<Matrix> matrix = new LinkedList<>();
        for (List<Sample> ithSamples : map) {
            Matrix corr = SampleStats.pearsonCorrelation(ithSamples);
            matrix.add(corr);
        }
            return new Estimator<>("correlations", matrix);
    }

    @Override
    public Estimator<Matrix> calcCorrelation(StochasticProcess<T, Number> process) {
        int T = process.timeLength();
        return calcCorrelation(process, T);
    }
}
