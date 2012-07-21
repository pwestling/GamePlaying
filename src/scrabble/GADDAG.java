package scrabble;
import java.util.Arrays;

public class GADDAG {
  
  public static Integer idCounter = 0;
  private final int id;
  GADDAG[] children;
  byte[] transitions;
  byte[] end;
  byte numChildren = 0;
  byte endSize = 0;
  
  public GADDAG() {
    children = new GADDAG[1];
    transitions = new byte[1];
    end = new byte[1];
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
      transitions[numChildren] = charToByte(transitionChar);
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
    end[endSize] = charToByte(endChar);
    endSize++ ;
  }
  
  public boolean hasAsEnd(char endChar) {
    return contains(end, endChar);
  }
  
  private boolean contains(byte[] array, char endChar) {
    byte endByte = (byte) endChar;
    for (byte b : array) {
      if (b == endByte)
        return true;
    }
    return false;
  }
  
  public GADDAG[] getChildren() {
    return Arrays.copyOf(children, numChildren);
  }
  
  public char[] getTransitions() {
    return charsFromBytes(transitions);
  }
  
  private <T> T[] ensureSpace(T[] array, int insertionPoint) {
    if (insertionPoint >= array.length) {
      return Arrays.copyOf(array, array.length * 2);
    }
    return array;
    
  }
  
  private byte[] ensureSpace(byte[] array, int insertionPoint) {
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
    return charsFromBytes(end);
  }
  
  private char[] charsFromBytes(byte[] bytes) {
    char[] out = new char[bytes.length];
    for (int i = 0; i < bytes.length; i++ ) {
      out[i] = (char) bytes[i];
    }
    return out;
  }
  
  private byte charToByte(char c) {
    return (byte) (c & 0x00FF);
  }
}
