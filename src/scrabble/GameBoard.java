package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GameBoard {
  
  public static final int width = 15;
  public static final int height = 15;
  
  Square[][] board = new Square[width][height];
  
  public static GameBoard buildGameBoard(Scanner scan) {
    GameBoard gameBoard = new GameBoard();
    for (int i = 0; i < width; i++ ) {
      for (int j = 0; j < height; j++ ) {
        gameBoard.set(i, j, Square.makeSquare(scan.next()));
      }
    }
    return gameBoard;
  }
  
  public void set(int i, int j, Square s) {
    board[i][j] = s;
  }
  
  @Override
  public String toString() {
    Formatter out = new Formatter();
    for (int i = 0; i < width; i++ ) {
      for (int j = 0; j < height; j++ ) {
        out.format("%s", board[i][j].getCode());
      }
      out.format("\n");
    }
    return out.out().toString();
  }
  
  public static void main(String[] args) throws FileNotFoundException {
    File input = new File(args[0]);
    GameBoard board = GameBoard.buildGameBoard(new Scanner(input));
    System.out.println(board);
  }
}

class Square {
  
  int letterMultiplier;
  int wordMultiplier;
  Set<Character> legalSet;
  Character tile;
  
  public static Square makeSquare(String code) {
    Square square = null;
    if (code.matches("[a-zA-Z].*")) {
      System.out.println("Found a tiled square");
      square = new Square();
      square.setTile(code.charAt(0));
      return square;
    }
    if (code.matches("[2-3]w")) {
      square = new Square(1, Integer.valueOf(code.substring(0, 1)));
      return square;
    }
    if (code.matches("[2-3]l")) {
      square = new Square(Integer.valueOf(code.substring(0, 1)), 1);
      return square;
    }
    if (code.matches("_+")) {
      square = new Square();
      return square;
    }
    throw new IllegalArgumentException("Invalid Code");
  }
  
  public Square() {
    this(1, 1);
  }
  
  public Square(int letterMultiplier, int wordMultiplier) {
    this(letterMultiplier, wordMultiplier, new HashSet<Character>());
  }
  
  public Square(int letterMultiplier, int wordMultiplier, Set<Character> legalSet) {
    this(letterMultiplier, wordMultiplier, legalSet, null);
  }
  
  public Square(int letterMultiplier, int wordMultiplier, Set<Character> legalSet, Character tile) {
    super();
    this.letterMultiplier = letterMultiplier;
    this.wordMultiplier = wordMultiplier;
    this.legalSet = legalSet;
    this.tile = tile;
  }
  
  public Character getTile() {
    return tile;
  }
  
  public void setTile(Character tile) {
    this.tile = tile;
  }
  
  public int getLetterMultiplier() {
    return letterMultiplier;
  }
  
  public int getWordMultiplier() {
    return wordMultiplier;
  }
  
  public boolean legal(Character c) {
    return legalSet.contains(c);
  }
  
  public String getCode() {
    if (this.tile != null) {
      return tile.toString() + "_";
    }
    if (this.letterMultiplier > 1) {
      return letterMultiplier + "l";
    }
    if (this.wordMultiplier > 1) {
      return wordMultiplier + "w";
    }
    return "__";
  }
}
