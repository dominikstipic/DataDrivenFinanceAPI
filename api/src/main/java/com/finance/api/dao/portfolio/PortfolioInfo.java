package com.finance.api.dao.portfolio;

import com.finance.api.dto.Matrix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PortfolioInfo {
    private List<Double> weights = new LinkedList<>();
    private List<Double> returns = new LinkedList<>();
    private List<Double> risks = new LinkedList<>();

    private Matrix covMatrix;

    public void addWeight(double weight){
        weights.add(weight);
    }

    public void addReturn(double expectedReturn){
        returns.add(expectedReturn);
    }

    public void addRisk(double risk){
        risks.add(risk);
    }


}
