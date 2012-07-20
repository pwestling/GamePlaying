import java.util.Arrays;

public class GADDAG {
  
  public static Integer idCounter = 0;
  private final int id;
  GADDAG[] children;
  char[] transitions;
  char[] end;
  byte numChildren = 0;
  byte endSize = 0;
  
  public GADDAG() {
    children = new GADDAG[0];
    transitions = new char[0];
    end = new char[0];
    synchronized (idCounter) {
      id = idCounter;
      idCounter++ ;
    }
  }
  
  public GADDAG put(char transitionChar, GADDAG node) {
    GADDAG child = this.get(transitionChar);
    if (child == null) {
      children = ensureSpace(children, numChildren);
      transitions = ensureSpace(transitions, numChildren);
      transitions[numChildren] = transitionChar;
      children[numChildren] = node;
      numChildren++ ;
      return node;
    } else {
      return child;
    }
  }
  
  public GADDAG put(char transitionChar) {
    return this.put(transitionChar, new GADDAG());
  }
  
  public GADDAG get(char transitionChar) {
    for (int i = 0; i < numChildren; i++ ) {
      if (transitions[i] == transitionChar) {
        return children[i];
      }
    }
    return null;
  }
  
  public int getID() {
    return id;
  }
  
  public boolean contains(String query) {
    return containsRecur(query.charAt(0) + "@" + query.substring(1));
  }
  
  private boolean containsRecur(String query) {
    char c = query.charAt(0);
    if (query.length() == 1 && this.hasAsEnd(c)) {
      return true;
    }
    GADDAG child = this.get(c);
    if (child != null) {
      return child.containsRecur(query.substring(1));
    }
    return false;
  }
  
  public void putEndSet(char endChar) {
    end = ensureSpace(end, endSize);
    end[endSize] = endChar;
    endSize++ ;
  }
  
  public boolean hasAsEnd(char endChar) {
    return contains(end, endChar);
  }
  
  private boolean contains(char[] array, char endChar) {
    for (char c : array) {
      if (c == endChar)
        return true;
    }
    return false;
  }
  
  public GADDAG[] getChildren() {
    return Arrays.copyOf(children, numChildren);
  }
  
  public char[] getTransitions() {
    return Arrays.copyOf(transitions, numChildren);
  }
  
  private <T> T[] ensureSpace(T[] array, int insertionPoint) {
    if (insertionPoint >= array.length) {
      return Arrays.copyOf(array, array.length * 2);
    }
    return array;
    
  }
  
  private char[] ensureSpace(char[] array, int insertionPoint) {
    if (insertionPoint >= array.length) {
      return Arrays.copyOf(array, array.length * 2);
    }
    return array;
  }
  
  @Override
  public String toString() {
    return id + " trans:" + Arrays.toString(this.getTransitions()) + " end:" + Arrays.toString(this.getEndSet());
  }
  
  public char[] getEndSet() {
    return Arrays.copyOf(end, endSize);
  }
}
