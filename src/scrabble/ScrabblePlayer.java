package scrabble;

import java.util.List;
import java.util.Map;

public class ScrabblePlayer {
  
  List<Character> rack;
  GameBoard board;
  
  public static Map<Character, Integer> tileScores;
  
  public ScrabblePlayer(List<Character> rack, GameBoard board) {
    super();
    this.rack = rack;
    this.board = board;
  }
  
}
