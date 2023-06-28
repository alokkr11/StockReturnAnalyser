package com.crio.warmup.stock.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
// Implement the Candle interface in such a way that it matches the parameters returned
// inside Json response from Alphavantage service.

// Reference - https:www.baeldung.com/jackson-ignore-properties-on-serialization
// Reference - https:www.baeldung.com/jackson-name-of-property

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlphavantageCandle implements Candle {
  @JsonProperty("1. open")
  private Double open;
  @JsonProperty("4. close")
  private Double close;
  @JsonProperty("2. high")
  private Double high;
  @JsonProperty("3. low")
  private Double low;
  @JsonIgnore
  private LocalDate date;

  @Override
  public Double getOpen() {
    // TODO Auto-generated method stub
    return open;
  }

  @Override
  public Double getClose() {
    // TODO Auto-generated method stub
    return close;
  }

  @Override
  public Double getHigh() {
    // TODO Auto-generated method stub
    return high;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  @Override
  public Double getLow() {
    // TODO Auto-generated method stub
    return low;
  }

  @Override
  public LocalDate getDate() {
    // TODO Auto-generated method stub
    return date;
  }
}


