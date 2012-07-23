package scrabble;

import java.util.ArrayList;
import java.util.Iterator;

public class Move implements Iterable<Play> {
  
  ArrayList<Play> plays;
  int score;
  
  public Move() {
    plays = new ArrayList<Play>();
    
  }
  
  public Move(Move other) {
    this.plays = new ArrayList<Play>(other.plays);
  }
  
  public void addPlay(Character c, int i, int j) {
    plays.add(new Play(i, j, c));
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public int getScore() {
    return score;
  }
  
  public int size() {
    return plays.size();
  }
  
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    for (Play p : plays) {
      out.append(p.tile + " " + p.i + " " + p.j + "\n");
    }
    return out.toString();
  }
  
  @Override
  public Iterator<Play> iterator() {
    return plays.iterator();
  }
}

class Play {
  
  public final int i;
  public final int j;
  public final Character tile;
  
  public Play(int i, int j, Character tile) {
    super();
    this.i = i;
    this.j = j;
    this.tile = tile;
  }
  
}
