package com.search.domain;

import java.time.LocalDateTime;

public class SearchRequest {

  public SearchRequest(LocalDateTime from, LocalDateTime to, String regex) {
    this.from = from;
    this.to = to;
    this.regex = regex;
  }

  private LocalDateTime from;
  private LocalDateTime to;
  private String regex;

  public LocalDateTime getFrom() {
    return from;
  }

  public LocalDateTime getTo() {
    return to;
  }

  public String getRegex() {
    return regex;
  }

}
