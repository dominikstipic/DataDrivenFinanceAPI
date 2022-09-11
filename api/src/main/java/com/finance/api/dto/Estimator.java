package com.finance.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Estimator<T>{
    String name;

    List<T> value;
}
