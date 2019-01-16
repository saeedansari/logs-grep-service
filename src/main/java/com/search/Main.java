package com.search;

import com.search.domain.SearchRequest;
import com.search.service.GrepSearch;
import com.search.service.ResultParser;
import com.search.util.LogGrepProperties;
import com.search.util.TimeUtil;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {

  private static final String FORMAT = "Input format should be \n "
      + "--from 2018-12-01-12:34:00 --to 2018-12-01-12:34:00 --regex  {regex}";

  public static void main(String[] args) {
    LogGrepProperties prop = new LogGrepProperties();
    try {
      parseInput(prop);
    } catch (Exception e) {
      System.out.println(FORMAT);
      parseInput(prop);
    }

  }

  private static void parseInput(LogGrepProperties prop) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter from date");
    String from = sc.next();
    final LocalDateTime fromDateTime = TimeUtil.parseInputDate(from);
    System.out.println("Enter to date");
    String to = sc.next();
    final LocalDateTime toDateTime = TimeUtil.parseInputDate(to);
    System.out.println("Enter regex");
    String regEx = sc.next();
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    final String logDirectory = prop.getProperty("LOG_DIRECTORY");
    final File file = Paths.get(logDirectory).toFile();
    SearchRequest searchRequest = new SearchRequest(fromDateTime, toDateTime, regEx);
    forkJoinPool.invoke(new GrepSearch(file, searchRequest));
    ResultParser resultParser = new ResultParser(file);
    resultParser.printResult();
  }
}
