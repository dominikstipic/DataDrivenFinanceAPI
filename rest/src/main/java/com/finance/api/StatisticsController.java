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

@Path("/stats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsController {

    @Inject
    @DataProvider("yahoo")
    private DataService dataService;
    @Inject
    private DataProcessingService<Date, Integer> dataProcessingService;

    @GET
    @Path("/{ticker}/{attribute}/returns")
    public Response getReturns(@PathParam("ticker") String ticker,
                               @PathParam("attribute") String attribute,
                               @QueryParam("start") String startDate,
                               @QueryParam("end") String endDate,
                               @QueryParam("type") String dataType,
                               @QueryParam("frequency") String frequency){
        TimeSeries<Date, Number> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
        if(series == null){
            return Response.notAcceptable(null).build();
        }
        TimeSeries<Date, Double> returns = dataProcessingService.assetReturns(series);
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
        TimeSeries<Date, Number> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
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
        TimeSeries<Date, Number> series = dataService.getTimeSeries(ticker, startDate, endDate, attribute);
        if(series == null){
            return Response.notAcceptable(null).build();
        }
        TimeSeries<Integer, Double> averaged = dataProcessingService.timeAverage(series, n);
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
        StochasticProcess<Date, Number> portfolio = dataService.getProcess(tickers, startDate, endDate, attribute);
        List<Matrix> correlations = dataProcessingService.calcCorrelation(portfolio);
        return Response.ok(correlations).build();
    }

    @POST
    @Path("/{attribute}/corr/{n}")
    public Response getPortfolioCorrForGroups(List<String> tickers,
                                     @PathParam("attribute") String attribute,
                                     @PathParam("n") int n,
                                     @QueryParam("start") String startDate,
                                     @QueryParam("end") String endDate,
                                     @QueryParam("type") String dataType,
                                     @QueryParam("frequency") String frequency){
        StochasticProcess<Date, Number> portfolio = dataService.getProcess(tickers, startDate, endDate, attribute);
        List<Matrix> correlations = dataProcessingService.calcCorrelation(portfolio, n);
        return Response.ok(correlations).build();
    }

}
