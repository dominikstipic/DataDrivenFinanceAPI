package com.finance.api.dao.portfolio;

import com.finance.api.dto.Matrix;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class PortfolioBuilderImpl implements PortfolioBuilder {
    private PortfolioInfo info = new PortfolioInfo();

    @Override
    public void reset() {
        this.info = new PortfolioInfo();
    }

    @Override
    public PortfolioInfo build() {
        return info;
    }


    @Override
    public PortfolioBuilder addWeight(double weight) {
        info.addWeight(weight);
        return this;
    }

    @Override
    public PortfolioBuilder addWeights(List<Double> weights) {
        info.setWeights(weights);
        return this;
    }

    @Override
    public PortfolioBuilder setExpectedReturn(double expectedReturn) {
        info.addReturn(expectedReturn);
        return this;
    }

    @Override
    public PortfolioBuilder setExpectedRisk(double expectedRisk) {
        info.addRisk(expectedRisk);
        return this;
    }

    @Override
    public PortfolioBuilder setCovMatrix(Matrix covMatrix) {
        info.setCovMatrix(covMatrix);
        return this;
    }
}
