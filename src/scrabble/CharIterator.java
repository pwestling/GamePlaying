package scrabble;

import java.util.Iterator;

public class CharIterator implements Iterator<Character>, Iterable<Character> {
  
  int counter = 96;
  int limit = 121;
  
  @Override
  public boolean hasNext() {
    return counter < limit;
  }
  
  @Override
  public Character next() {
    counter++ ;
    return Character.toChars(counter)[0];
  }
  
  @Override
  public void remove() {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Iterator<Character> iterator() {
    return this;
  }
  
  public static CharIterator iter() {
    return new CharIterator();
  }
  
}
