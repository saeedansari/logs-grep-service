package com.search.service;

import com.search.domain.LogLine;
import com.search.domain.SearchRequest;
import com.search.util.TimeUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrepSearch extends RecursiveAction {

  private static final Logger LOG = Logger.getLogger(GrepSearch.class.getName());
  private static final String RESULT_APPENDER = "_result";
  private static final int BUFFER_SIZE = 1024 * 1024;
  private static final String DATE_REGEX = "\\d{1,2}\\/(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\/((19|2[0-9])[0-9]{2}):((2[0-3]|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9]))";

  private SearchRequest searchRequest;
  private File fileName;

  public GrepSearch(final File file, SearchRequest searchRequest) {
    this.searchRequest = searchRequest;
    this.fileName = file;
  }

  @Override
  protected void compute() {
    try {
      if (fileName.isFile()) {
        search(fileName, this.searchRequest);
      } else {
        final File[] files = fileName.listFiles();
        List<GrepSearch> actions = new ArrayList<>();
        for (File file : files) {
          if (file.getName().indexOf(RESULT_APPENDER) < 0) {
            actions.add(new GrepSearch(file, this.searchRequest));
          }
        }
        ForkJoinTask.invokeAll(actions);
      }

    } catch (IOException e) {
      LOG.log(Level.SEVERE, e.getMessage());
    }
  }


  private void search(File file, SearchRequest request) throws IOException {
    boolean resultFileCreated = false;
    BufferedWriter bw = null;
    try (BufferedReader br = new BufferedReader(new FileReader(file),
        BUFFER_SIZE)) {
      String line;
      while ((line = br.readLine()) != null) {
        final Optional<LogLine> logLine = createLogLine(line);
        if (logLine.isPresent()) {
          final LogLine ll = logLine.get();
          if (ll.getDateTime().isAfter(request.getTo())) {
            break;
          }
          if (TimeUtil.isInDateTimeRange(request.getFrom(), request.getTo(), ll.getDateTime())) {
            if (!resultFileCreated) {
              bw = new BufferedWriter(new FileWriter(file.getName() + RESULT_APPENDER));
              resultFileCreated = true;
            }
            if (matchRegex(ll.getLog(), request.getRegex())) {
              bw.write(line + "\n");
            }
          }
        }

      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (bw != null) {
        bw.close();
      }
    }
  }


  /**
   * @param line a line in the log file
   * @return LogLine object which contains DateTime and rest of the line
   */
  public static Optional<LogLine> createLogLine(String line) {
    Pattern pattern = Pattern.compile(DATE_REGEX);
    final Matcher matcher = pattern.matcher(line);
    int start = 0, end = 0;
    if (matcher.find()) {
      start = matcher.start();
      end = matcher.end();
    }
    String dateTimeStr = line.substring(start, end);
    String restOfLine = line.substring(end);
    if (dateTimeStr.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new LogLine(TimeUtil.parseDateTime(dateTimeStr), restOfLine));
  }

  public static boolean matchRegex(String line, String regEx) {
    Pattern pattern = Pattern.compile(regEx);
    final Matcher matcher = pattern.matcher(line);
    return matcher.find();
  }
}


