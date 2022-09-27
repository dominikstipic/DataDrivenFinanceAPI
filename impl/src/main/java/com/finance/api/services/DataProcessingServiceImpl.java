package com.finance.api.services;

import com.finance.api.dao.portfolio.PortfolioBuilder;
import com.finance.api.dao.portfolio.PortfolioBuilderImpl;
import com.finance.api.dao.portfolio.PortfolioInfo;
import com.finance.api.dto.Estimator;
import com.finance.api.dto.Matrix;
import com.finance.api.dto.Sample;
import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;
import com.finance.api.processors.ProcessStats;
import com.finance.api.processors.SampleStats;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataProcessingServiceImpl<T extends Comparable> implements DataProcessingService<T>{

    @Override
    public TimeSeries<T> assetReturns(TimeSeries<T> series) {
        return series.getReturns();
    }

    @Override
    public TimeSeries<T> assetCumulativeReturns(TimeSeries<T> series) {
        TimeSeries<T> returns = series.getReturns();
        TimeSeries<T> cumReturns = new TimeSeries<>();
        double prod = 1;
        for(Map.Entry<T, Double> e: returns){
            T t = e.getKey();
            double r = e.getValue();
            prod *= (1+r);
            cumReturns.add(t, prod);
        }
        return cumReturns;
    }

    @Override
    public TimeSeries<T> meanEnsembleAverage(StochasticProcess<T> process) {
        return ProcessStats.ensembleMoment(process, 1);
    }
    @Override
    public Estimator<Double> timeAverage(TimeSeries<T> series) {
        double value = ProcessStats.timeAveragingMean.apply(series);
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

    @Override
    public PortfolioInfo getPortfolioInfo(StochasticProcess<T> process, List<Double> weights) {
        PortfolioBuilder builder = new PortfolioBuilderImpl();
        List<Sample> samples = process.
                getRealizations().
                stream().
                map(TimeSeries::toSample).
                collect(Collectors.toList());
        double portfolioReturn = IntStream.
                range(0, samples.size()).
                mapToDouble(i -> samples.get(i).mean() * weights.get(i)).
                sum();
        double portfolioRisk = SampleStats.multiSampleVariance(samples);
        Matrix covMatrix = SampleStats.covarianceMatrix(samples);
        return builder.
                addWeights(weights).
                setExpectedReturn(portfolioReturn).
                setExpectedRisk(portfolioRisk).
                setCovMatrix(covMatrix).
                build();
    }

    @Override
    public PortfolioInfo getPortfolioInfo(TimeSeries<T> process) {
        PortfolioBuilder builder = new PortfolioBuilderImpl();
        Sample sample = process.toSample();
        return builder.setExpectedReturn(sample.mean()).
               setExpectedRisk(sample.std()).build();
    }
}
