package scrabble;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class SimpleHandler extends Handler {
  
  public SimpleHandler() {
    super();
    this.setLevel(Level.ALL);
  }
  
  @Override
  public void close() throws SecurityException {
    System.err.close();
    
  }
  
  @Override
  public void flush() {
    System.err.flush();
    
  }
  
  @Override
  public void publish(LogRecord record) {
    System.err.println(record.getMessage());
    
  }
  
}
