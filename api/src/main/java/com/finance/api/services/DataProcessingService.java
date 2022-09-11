package com.finance.api.services;

import com.finance.api.dto.Estimator;
import com.finance.api.dto.Matrix;
import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;

import java.util.List;


public interface DataProcessingService<T extends Comparable, P extends Comparable> {
    TimeSeries<T, Double> assetReturns(TimeSeries<T, Number> series);

    TimeSeries<P, Double> meanEnsembleAverage(StochasticProcess<P, Number> process);

    Estimator<Double> timeAverage(TimeSeries<T, Number> series);

    TimeSeries<Integer, Double> timeAverage(TimeSeries<T, Number> series, int windowSize);


    Estimator<Matrix> calcCorrelation(StochasticProcess<T, Number> process, int windowSize);

    Estimator<Matrix> calcCorrelation(StochasticProcess<T, Number> process);

}
