package scrabble;

import java.util.List;
import java.util.Map;

public class ScrabblePlayer {
  
  List<Character> rack;
  GameBoard board;
  GADDAG lexicon;
  
  public static Map<Character, Integer> tileScores;
  
  public ScrabblePlayer(List<Character> rack, GameBoard board, GADDAG lexicon) {
    super();
    this.rack = rack;
    this.board = board;
    this.lexicon = lexicon;
  }
  
}
