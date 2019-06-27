package calendar.common;

public class GeneralHelper {

  public static boolean withinRange(int value, int start, int end) {
    return (value - start) * (value - end) >= 0;
  }
}
