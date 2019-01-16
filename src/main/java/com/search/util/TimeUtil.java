package com.search.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeUtil {

  private static Logger logger = Logger.getLogger(TimeUtil.class.getName());

  private static final int STANDARD_SIZE = 20;
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter
      .ofPattern("d/MMM/yyyy:HH:mm:ss");
  private static final DateTimeFormatter dateTimeFormatterSec = DateTimeFormatter
      .ofPattern("d/MMM/yyyy:HH:mm:s");
  private static final DateTimeFormatter inputDateTimeFormatter = DateTimeFormatter
      .ofPattern("yyyy-MM-dd-HH:mm:ss");

  public static boolean isInDateTimeRange(LocalDateTime from, LocalDateTime to,
      LocalDateTime logDateTime) {
    return logDateTime.isAfter(from) && logDateTime.isBefore(to);
  }


  public static LocalDateTime parseDateTime(String dateTimeStr) {
    LocalDateTime logDateTime = LocalDateTime.now();
    try {
      if (dateTimeStr.length() == STANDARD_SIZE) {
        logDateTime = LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
      } else {
        logDateTime = LocalDateTime.parse(dateTimeStr, dateTimeFormatterSec);
      }
    } catch (DateTimeParseException e) {
      logger.log(Level.SEVERE, "Error in dateTimeStr " + dateTimeStr);
    }
    return logDateTime;
  }

  public static LocalDateTime parseInputDate(String dateTime) {
    return LocalDateTime.parse(dateTime, inputDateTimeFormatter);
  }



}
