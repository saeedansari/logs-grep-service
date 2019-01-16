package com.search.domain;

import java.time.LocalDateTime;

public class LogLine {

  public LogLine(LocalDateTime dateTime, String log) {
    this.dateTime = dateTime;
    this.log = log;
  }

  private LocalDateTime dateTime;
  private  String log;

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public String getLog() {
    return log;
  }

}
