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
    for (int j = 0; j < height; j++ ) {
      for (int i = 0; i < width; i++ ) {
        gameBoard.set(i, j, Square.makeSquare(scan.next()));
        if (gameBoard.get(i, j).hasTile()) {
          System.out.println("Square " + i + ":" + j + " has tile " + gameBoard.getTile(i, j));
        }
        
      }
    }
    return gameBoard;
  }
  
  public void set(int i, int j, Square s) {
    board[j][i] = s;
  }
  
  public Square get(int i, int j) {
    return board[j][i];
  }
  
  public boolean hasTile(int i, int j) {
    try {
      return board[j][i].hasTile();
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }
  
  public Character getTile(int i, int j) {
    return board[j][i].getTile();
  }
  
  public void computeAnchors() {
    for (int i = 0; i < width; i++ ) {
      for (int j = 0; j < height; j++ ) {
        this.get(i, j).setAnchor(isValidAnchor(i, j));
      }
    }
  }
  
  private boolean isValidAnchor(int i, int j) {
    if (!this.hasTile(i, j)) {
      if (i == width / 2 && j == height / 2) {
        return true;
      }
      if (hasTile(i - 1, j) || hasTile(i + 1, j) || hasTile(i, j + 1) || hasTile(i, j - 1)) {
        return true;
      }
    }
    return false;
  }
  
  public void computeCrossSets(GADDAG lexicon) {
    GADDAG current = lexicon;
    for (int j = 0; j < height; j++ ) {
      for (int i = width - 1; i >= 0; i-- ) {
        if (!hasTile(i, j)) {
          computeUpDownCrossSet(i, j, current);
        }
      }
    }
  }
  
  private void computeUpDownCrossSet(int i, int j, GADDAG current) {
    if (hasTile(i - 1, j) && hasTile(i + 1, j)) {
      // implement
    } else if (hasTile(i - 1, j)) {
      int x = i - 1;
      while (hasTile(x, j)) {
        current = current.get(getTile(x, j));
        if (current == null) {
          System.err.println("Cross set for " + i + ":" + j + " with tile " + getTile(i, j) + " broken");
          throw new RuntimeException("Invalid board: Error at " + x + ":" + j);
        }
        x-- ;
      }
      current = current.get('@');
      if (current != null) {
        get(i, j).getLegalSet().addAll(current.getEndSet());
      }
      
    } else if (hasTile(i + 1, j)) {
      int x = i + 1;
      while (hasTile(x + 1, j)) {
        x++ ;
      }
      while (x > i) {
        current = current.get(getTile(x, j));
        if (current == null) {
          System.err.println("Cross set for " + i + ":" + j + " with tile " + getTile(i, j) + " broken");
          throw new RuntimeException("Invalid board: Error at " + x + ":" + j);
        }
        x-- ;
      }
      get(i, j).getLegalSet().addAll(current.getEndSet());
    }
    
  }
  
  @Override
  public String toString() {
    Formatter out = new Formatter();
    for (int i = 0; i < width; i++ ) {
      for (int j = 0; j < height; j++ ) {
        out.format("%s ", board[i][j].getCode());
      }
      out.format("\n");
    }
    return out.out().toString();
  }
  
  public static void main(String[] args) throws FileNotFoundException {
    File input = new File(args[0]);
    GameBoard board = GameBoard.buildGameBoard(new Scanner(input));
    System.out.println(board);
    GADDAG lexicon = GADDAGBuilder.buildGADDAG(new Scanner(new File(args[1])));
    board.computeAnchors();
    board.computeCrossSets(lexicon);
    Scanner in = new Scanner(System.in);
    while (in.hasNext()) {
      int i = in.nextInt();
      int j = in.nextInt();
      System.out.println(board.get(i, j).getLegalSet());
    }
  }
}

class Square {
  
  int letterMultiplier;
  int wordMultiplier;
  Set<Character> legalSet;
  Character tile;
  boolean isAnchor;
  
  public static Square makeSquare(String code) {
    Square square = null;
    if (code.matches("[a-zA-Z].*")) {
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
    setTile(tile);
    this.isAnchor = false;
  }
  
  public boolean isAnchor() {
    return isAnchor;
  }
  
  public void setAnchor(boolean isAnchor) {
    this.isAnchor = isAnchor;
  }
  
  public Character getTile() {
    return tile;
  }
  
  public boolean hasTile() {
    return tile != null;
  }
  
  public void setTile(Character tile) {
    if (tile == null) {
      this.tile = null;
    } else {
      this.tile = Character.toLowerCase(tile);
    }
  }
  
  public int getLetterMultiplier() {
    return letterMultiplier;
  }
  
  public int getWordMultiplier() {
    return wordMultiplier;
  }
  
  public boolean legal(Character c) {
    if (legalSet.isEmpty()) {
      return true;
    }
    return legalSet.contains(c);
  }
  
  public Set<Character> getLegalSet() {
    return legalSet;
  }
  
  public void setLegalSet(Set<Character> legalSet) {
    this.legalSet = legalSet;
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
