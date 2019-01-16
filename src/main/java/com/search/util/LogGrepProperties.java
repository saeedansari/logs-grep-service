package com.search.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LogGrepProperties {

  private final String PROPERTIES_FILE = "application.properties";
  Properties properties = new Properties();

  public LogGrepProperties() {
    InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
    try {
      properties.load(is);
    } catch (IOException e) {
      System.out.println("error");

    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }


}
