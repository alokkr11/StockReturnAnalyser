
package com.crio.warmup.stock.portfolio;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {


  private RestTemplate restTemplate;
  private StockQuotesService stockQuotesService;

  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  // TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  // Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  // into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  // clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  // CHECKSTYLE:OFF



  public PortfolioManagerImpl() {}


  public PortfolioManagerImpl(RestTemplate restTemplate, StockQuotesService stockQuotesService) {
    this.restTemplate = restTemplate;
    this.stockQuotesService = stockQuotesService;
  }

  public PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    // this.restTemplate = restTemplate;
    this.stockQuotesService = stockQuotesService;
  }


  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  // CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  // Extract the logic to call Tiingo third-party APIs to a separate function.
  // Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
    Candle[] candles = restTemplate.getForObject(buildUri(symbol, from, to), TiingoCandle[].class);
    return Arrays.asList(candles);
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?" + "startDate="
        + startDate + "&endDate=" + endDate + "&token=" + getToken();
    return uriTemplate;
  }


  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) throws JsonProcessingException {
    // TODO Auto-generated method stub

    List<AnnualizedReturn> annualizedReturnsList = new ArrayList<>();

    for (PortfolioTrade portfolioTrade : portfolioTrades) {

      List<Candle> candlesList = stockQuotesService.getStockQuote(portfolioTrade.getSymbol(),
          portfolioTrade.getPurchaseDate(), endDate);
      if (candlesList != null) {
        Double buyPrice = candlesList.get(0).getOpen();
        Double sellPrice = candlesList.get(candlesList.size() - 1).getClose();
        LocalDate sellDate = candlesList.get(candlesList.size() - 1).getDate();

        annualizedReturnsList
            .add(calculateAnnualizedReturns(sellDate, portfolioTrade, buyPrice, sellPrice));
      }


    }

    Collections.sort(annualizedReturnsList, getComparator());
    return annualizedReturnsList;
  }

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate, PortfolioTrade trade,
      Double buyPrice, Double sellPrice) {

    Double totalReturn = (sellPrice - buyPrice) / buyPrice;
    Double years = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate) / 365d;
    Double annualized_returns = Math.pow((1 + totalReturn), (1 / years)) - 1;
    return new AnnualizedReturn(trade.getSymbol(), annualized_returns, totalReturn);
  }

  public static String getToken() {
    return "837ccaae4dabe76554a1e06d3d7b349e5b1605d6";
  }



  // ¶TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  // Modify the function #getStockQuote and start delegating to calls to
  // stockQuoteService provided via newly added constructor of the class.
  // You also have a liberty to completely get rid of that function itself, however, make sure
  // that you do not delete the #getStockQuote function.

}
