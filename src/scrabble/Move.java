package scrabble;

import java.util.ArrayList;
import java.util.List;

public class Move {
  
  List<Character> tiles;
  List<Integer> iCoords;
  List<Integer> jCoords;
  int score;
  
  public Move() {
    tiles = new ArrayList<Character>();
    iCoords = new ArrayList<Integer>();
    jCoords = new ArrayList<Integer>();
    
  }
  
  public Move(Move other) {
    this.tiles = new ArrayList<Character>(other.tiles);
    this.iCoords = new ArrayList<Integer>(other.iCoords);
    this.jCoords = new ArrayList<Integer>(other.jCoords);
  }
  
  public void addPlay(Character c, int i, int j) {
    tiles.add(c);
    iCoords.add(i);
    jCoords.add(j);
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public int getScore() {
    return score;
  }
  
  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    for (int i = 0; i < tiles.size(); i++ ) {
      out.append(tiles.get(i) + " " + iCoords.get(i) + " " + jCoords.get(i) + "\n");
    }
    return out.toString();
  }
}
