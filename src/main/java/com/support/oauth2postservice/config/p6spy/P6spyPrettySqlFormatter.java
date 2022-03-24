package com.support.oauth2postservice.config.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.internal.FormatStyle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class P6spyPrettySqlFormatter implements MessageFormattingStrategy {

  private static final String QUERY_FILE_NAME = "logs/query/p6spy-%s.log";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  @Override
  public String formatMessage(final int connectionId, final String now, final long elapsed,
                              final String category, final String prepared, final String sql, final String url) {
    ArrayDeque<String> callStack = new ArrayDeque<>();
    StackTraceElement[] stackTrace = new Throwable().getStackTrace();

    for (StackTraceElement stackTraceElement : stackTrace) {
      String trace = stackTraceElement.toString();
      if (trace.startsWith("io.p6spy") && !trace.contains("P6spyPrettySqlFormatter")) {
        callStack.push(trace);
      }
    }

    StringBuilder callStackBuilder = new StringBuilder();
    int order = 1;
    while (callStack.size() != 0) {
      callStackBuilder.append("\n\t\t").append(order++).append(". ").append(callStack.pop());
    }

    String message =
        "\n\n\tConnection ID: " + connectionId +
            "\n\tExecution Time: " + elapsed + " ms" +
            "\n\tCall Stack (number 1 is entry point): " + callStackBuilder +
            "\n----------------------------------------------------------------------------------------------------";

    return sqlFormat(sql, category, message);
  }

  private String sqlFormat(String sql, String category, String message) {
    if (sql.trim().isEmpty()) {
      return "";
    }

    if (Category.STATEMENT.getName().equals(category)) {
      String s = sql.trim().toLowerCase(Locale.ROOT);
      if (s.startsWith("create") || s.startsWith("alter") || s.startsWith("comment")) {
        sql = FormatStyle.DDL
            .getFormatter()
            .format(sql);
      } else {
        sql = FormatStyle.BASIC
            .getFormatter()
            .format(sql);
      }
    }

    final String finalSql = sql.toUpperCase();
    CompletableFuture.runAsync(() -> {
      File file = new File(String.format(QUERY_FILE_NAME, LocalDate.now()));
      try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))) {
        bufferedWriter.write(LocalDateTime.now().format(FORMATTER));
        bufferedWriter.write(finalSql + message + "\n");
      } catch (IOException e) {
        log.error("[SUPPORT-ERROR] :: exception {}", e);
      }
    });

    return "\n" + finalSql + message;
  }
}
