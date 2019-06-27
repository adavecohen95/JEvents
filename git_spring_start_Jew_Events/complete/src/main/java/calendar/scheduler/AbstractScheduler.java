package calendar.scheduler;

public class AbstractScheduler {


  public static final long runSpeed = 0x124F80L; // 20 minutes;
  protected long nextIteration = runSpeed;

  public void setNextIteration(long nextIteration) {
    this.nextIteration = nextIteration;
  }
  public long getNextIteration() {
    return nextIteration;
  }
}
