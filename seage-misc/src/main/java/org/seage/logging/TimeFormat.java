package org.seage.logging;

import java.util.concurrent.TimeUnit;

public class TimeFormat {
  private TimeFormat() {}

  /** Time duration format: (DD:HH:mm:ss). */
  public static String getTimeDurationBreakdown(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Duration must be greater than zero!");
    }

    long days = TimeUnit.MILLISECONDS.toDays(millis);
    millis -= TimeUnit.DAYS.toMillis(days);
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

    return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
  }
}
