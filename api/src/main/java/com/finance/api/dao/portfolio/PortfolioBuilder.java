package com.finance.api.dao.portfolio;

import com.finance.api.dto.Matrix;

import java.util.List;

public interface PortfolioBuilder{

    void reset();
    PortfolioInfo build();
    PortfolioBuilder addWeight(double weight);

    PortfolioBuilder addWeights(List<Double> weights);

    PortfolioBuilder setExpectedReturn(double expectedReturn);

    PortfolioBuilder setExpectedRisk(double expectedRisk);

    PortfolioBuilder setCovMatrix(Matrix covMatrix);

}
