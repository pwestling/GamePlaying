import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GADDAG {
  
  private final String symbol;
  Map<String, GADDAG> children;
  
  public GADDAG(String symbol) {
    this.symbol = symbol;
    this.children = new HashMap<String, GADDAG>();
  }
  
  public void put(String symbol, GADDAG child) {
    children.put(symbol, child);
  }
  
  public void put(GADDAG child) {
    children.put(child.getSymbol(), child);
  }
  
  public GADDAG get(String symbol) {
    return children.get(symbol);
  }
  
  public Set<String> symbolSet() {
    return children.keySet();
  }
  
  public String getSymbol() {
    return symbol;
  }
  
  public GADDAG putIfNull(String value) {
    if (this.get(value) == null) {
      this.put(new GADDAG(value));
    }
    return this.get(value);
  }
  
  public int maxWidth() {
    int sum = 0;
    for (GADDAG child : children.values()) {
      sum += child.maxWidth();
    }
    return children.values().size() > sum ? children.values().size() : sum;
  }
  
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    ArrayList<StringBuilder> list = new ArrayList<StringBuilder>();
    makePrettyString(list, 0);
    for (StringBuilder sb : list) {
      result.append(sb.toString() + "\n");
    }
    return result.toString();
  }
  
  private void makePrettyString(ArrayList<StringBuilder> list, int i) {
    if (list.size() < i + 1) {
      list.add(new StringBuilder());
    }
    list.get(i).append(this.getSymbol() + " | ");
    for (GADDAG child : children.values()) {
      child.makePrettyString(list, i + 1);
    }
  }
}
