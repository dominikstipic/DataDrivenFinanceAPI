package com.finance.api;

import com.finance.api.dto.Estimator;
import com.finance.api.dto.Matrix;
import com.finance.api.models.StochasticProcess;
import com.finance.api.models.TimeSeries;
import com.finance.api.qualifiers.DataProvider;
import com.finance.api.services.DataProcessingService;
import com.finance.api.services.DataService;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Path("/stats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsController {

    @Inject
    @DataProvider("yahoo")
    private DataService dataService;
    @Inject
    private DataProcessingService<Date> dataProcessingService;

    @GET
    @Path("/{ticker}/{attribute}/returns")
    public Response getReturns(@PathParam("ticker") String ticker,
                               @PathParam("attribute") String attribute,
                               @QueryParam("start") String startDate,
                               @QueryParam("end") String endDate,
                               @QueryParam("type") String dataType,
                               @QueryParam("frequency") String frequency){
        TimeSeries<Date> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
        if(series == null){
            return Response.notAcceptable(null).build();
        }
        TimeSeries<Date> returns = dataProcessingService.assetReturns(series);
        return Response.ok(returns).build();
    }

    @GET
    @Path("/{ticker}/{attribute}/returns/cumulative")
    public Response getCumulativeReturns(@PathParam("ticker") String ticker,
                               @PathParam("attribute") String attribute,
                               @QueryParam("start") String startDate,
                               @QueryParam("end") String endDate,
                               @QueryParam("type") String dataType,
                               @QueryParam("frequency") String frequency){
        TimeSeries<Date> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
        if(series == null){
            return Response.notAcceptable(null).build();
        }
        TimeSeries<Date> returns = dataProcessingService.assetCumulativeReturns(series);
        return Response.ok(returns).build();
    }

    @GET
    @Path("/{ticker}/{attribute}/average")
    public Response getAverage(@PathParam("ticker") String ticker,
                               @PathParam("attribute") String attribute,
                               @QueryParam("start") String startDate,
                               @QueryParam("end") String endDate,
                               @QueryParam("type") String dataType,
                               @QueryParam("frequency") String frequency){
        TimeSeries<Date> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
        if(series == null ){
            return Response.notAcceptable(null).build();
        }
        Estimator<Double> averaged = dataProcessingService.timeAverage(series);
        return Response.ok(averaged).build();
    }

    @GET
    @Path("/{ticker}/{attribute}/average/{groups}")
    public Response getAverageByGroups(@PathParam("ticker") String ticker,
                                       @PathParam("attribute") String attribute,
                                       @PathParam("groups") int n,
                                       @QueryParam("start") String startDate,
                                       @QueryParam("end") String endDate,
                                       @QueryParam("type") String dataType,
                                       @QueryParam("frequency") String frequency){
        TimeSeries<Date> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
        if(series == null){
            return Response.notAcceptable(null).build();
        }
        TimeSeries<Integer> averaged = dataProcessingService.timeAverage(series, n);
        return Response.ok(averaged).build();
    }

    @POST
    @Path("/{attribute}/corr")
    public Response getPortfolioCorr(List<String> tickers,
                                  @PathParam("attribute") String attribute,
                                  @QueryParam("start") String startDate,
                                  @QueryParam("end") String endDate,
                                  @QueryParam("type") String dataType,
                                  @QueryParam("frequency") String frequency){
        StochasticProcess<Date> portfolio = dataService.getProcess(tickers, startDate, endDate, attribute);
        Estimator<Matrix> correlations = dataProcessingService.calcCorrelation(portfolio);
        return Response.ok(correlations).build();
    }

    @POST
    @Path("/{attribute}/corr/{windowSize}")
    public Response getPortfolioCorrForGroups(List<String> tickers,
                                              @PathParam("attribute") String attribute,
                                              @PathParam("windowSize") int windowSize,
                                              @QueryParam("start") String startDate,
                                              @QueryParam("end") String endDate,
                                              @QueryParam("type") String dataType,
                                              @QueryParam("frequency") String frequency){
        StochasticProcess<Date> portfolio = dataService.getProcess(tickers, startDate, endDate, attribute);
        Estimator<Matrix> correlations = dataProcessingService.calcCorrelation(portfolio, windowSize);
        return Response.ok(correlations).build();
    }

    @POST
    @Path("/{attribute}/returns/corr")
    public Response getReturnsPortfolioCorr(List<String> tickers,
                                     @PathParam("attribute") String attribute,
                                     @QueryParam("start") String startDate,
                                     @QueryParam("end") String endDate,
                                     @QueryParam("type") String dataType,
                                     @QueryParam("frequency") String frequency){
        StochasticProcess<Date> portfolio = dataService.getProcess(tickers, startDate, endDate, attribute);
        Function<TimeSeries<Date>, TimeSeries<Date>> toReturnsFunction = ts -> dataProcessingService.assetReturns(ts);
        StochasticProcess<Date> returnsPortfolio = portfolio.applyOnTimeSeries(toReturnsFunction);
        Estimator<Matrix> correlations = dataProcessingService.calcCorrelation(returnsPortfolio);
        return Response.ok(correlations).build();
    }



}
