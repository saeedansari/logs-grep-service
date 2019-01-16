package com.search.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResultParser {

  private static final Logger LOG = Logger.getLogger(ResultParser.class.getName());
  private static final int BUFFER_SIZE = 1024 * 1024;
  private static final String RESULT_APPENDER = "_result";
  private File directory;

  public ResultParser(File file) {
    this.directory = file;
  }


  public void printResult() {
    final List<File> files = Stream.of(this.directory.listFiles())
        .filter(f -> f.getName().indexOf(RESULT_APPENDER) > 0).collect(Collectors.toList());
    for (File file : files) {
      try (BufferedReader br = new BufferedReader(new FileReader(file),
          BUFFER_SIZE)) {
        String line;
        while ((line = br.readLine()) != null) {
          System.out.println(line);
        }
        file.delete();
      } catch (IOException e) {
        LOG.log(Level.SEVERE, "Failed to read from file");
      }
    }
  }

}