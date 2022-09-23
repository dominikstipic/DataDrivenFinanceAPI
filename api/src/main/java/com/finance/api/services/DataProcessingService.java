package com.finance.api.services;

import com.finance.api.dto.Estimator;
import com.finance.api.dto.Matrix;
import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;

import java.util.List;


public interface DataProcessingService<T extends Comparable> {
    TimeSeries<T> assetReturns(TimeSeries<T> series);

    TimeSeries<T> assetCumulativeReturns(TimeSeries<T> series);

    TimeSeries<T> meanEnsembleAverage(StochasticProcess<T> process);

    Estimator<Double> timeAverage(TimeSeries<T> series);

    TimeSeries<Integer> timeAverage(TimeSeries<? extends Comparable> series, int windowSize);

    Estimator<Matrix> calcCorrelation(StochasticProcess<T> process, int windowSize);

    Estimator<Matrix> calcCorrelation(StochasticProcess<T> process);

}
