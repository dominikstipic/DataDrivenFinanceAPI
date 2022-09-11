package com.finance.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/")
public class ApplicationController extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
                HypothesisTestingController.class,
                DataRetrieverController.class,
                StatisticsController.class
        );
    }
}
