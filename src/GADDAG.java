import java.util.Arrays;

public class GADDAG {
  
  private final char symbol;
  GADDAG[] children;
  int numChildren = 0;
  
  public GADDAG(char symbol) {
    this.symbol = symbol;
    this.children = new GADDAG[5];
  }
  
  public void put(GADDAG child) {
    ensureSpace();
    children[numChildren] = child;
    numChildren++ ;
  }
  
  private void ensureSpace() {
    if (numChildren >= children.length) {
      children = Arrays.copyOf(children, numChildren * 2);
    }
    
  }
  
  public GADDAG get(char symbol) {
    for (GADDAG child : children) {
      if (child != null && child.getSymbol() == symbol) {
        return child;
      }
    }
    return null;
  }
  
  public char getSymbol() {
    return symbol;
  }
  
  public GADDAG putIfNull(char value) {
    if (this.get(value) == null) {
      this.put(new GADDAG(value));
    }
    return this.get(value);
  }
  
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder(String.valueOf(symbol));
    out.append("[");
    for (GADDAG child : children) {
      if (child != null)
        out.append(child.toString());
    }
    out.append("]");
    return out.toString();
  }
}
