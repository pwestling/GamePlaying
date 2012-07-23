package scrabble;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameBoard {
  
  public Logger log = Logger.getLogger("GameBoard");
  
  public static final int width = 15;
  public static final int height = 15;
  
  boolean flip = false;
  
  Square[][] board = new Square[width][height];
  GADDAG lexicon;
  
  public static GameBoard buildGameBoard(Scanner board, Scanner lexicon) {
    GameBoard gameBoard = new GameBoard();
    for (int j = 0; j < height; j++ ) {
      for (int i = 0; i < width; i++ ) {
        gameBoard.set(i, j, Square.makeSquare(board.next()));
        if (gameBoard.get(i, j).hasTile()) {
          System.out.println("Square " + i + ":" + j + " has tile " + gameBoard.getTile(i, j));
        }
        
      }
    }
    gameBoard.setLexicon(GADDAGBuilder.buildGADDAG(lexicon));
    return gameBoard;
  }
  
  public GADDAG getLexicon() {
    return lexicon;
  }
  
  public void setLexicon(GADDAG lexicon) {
    this.lexicon = lexicon;
  }
  
  public void set(int i, int j, Square s) {
    if (flip) {
      board[i][j] = s;
    } else {
      board[j][i] = s;
    }
  }
  
  public Square get(int i, int j) {
    if (flip) {
      return board[i][j];
    } else {
      return board[j][i];
    }
  }
  
  public boolean hasTile(int i, int j) {
    try {
      if (flip) {
        return board[i][j].hasTile();
      } else {
        return board[j][i].hasTile();
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }
  
  public Character getTile(int i, int j) {
    if (flip) {
      return board[i][j].getTile();
    } else {
      return board[j][i].getTile();
    }
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
  
  public List<Move> computeMoves(List<Character> rack) {
    ArrayList<Move> output = new ArrayList<Move>();
    for (int j = 0; j < height; j++ ) {
      for (int i = 0; i < width; i++ ) {
        if (get(i, j).isAnchor()) {
          GADDAG current = lexicon;
          genMoves(0, i, j, output, new Move(), new ArrayList<Character>(rack), current, false);
        }
        
      }
    }
    return output;
  }
  
  private void genMoves(
      int offset,
      int anchori,
      int anchorj,
      ArrayList<Move> output,
      Move inMove,
      ArrayList<Character> rack,
      GADDAG base,
      boolean offOfReverse) {
    
    if (anchorj + offset >= height || anchorj + offset < 0) {
      return;
    }
    
    if (hasTile(anchori, anchorj + offset)) {
      Character l = getTile(anchori, anchorj + offset);
      log.fine("Tile " + l + " present at " + anchori + ":" + (anchorj + offset));
      Move move = new Move(inMove);
      GADDAG current;
      if (offOfReverse) {
        current = base;
      } else {
        current = base.get(l);
      }
      goOn(offset, anchori, anchorj, l, output, move, rack, current, offOfReverse);
    } else if (!rack.isEmpty()) {
      for (Character c : rack) {
        if (c != Square.BLANK && get(anchori, anchorj + offset).legal(c)) {
          log.fine("Trying out " + c + " at " + anchori + ":" + (anchorj + offset));
          ArrayList<Character> newRack = new ArrayList<Character>(rack);
          newRack.remove(c);
          Move move = new Move(inMove);
          GADDAG current;
          if (offOfReverse) {
            current = base;
          } else {
            current = base.get(c);
          }
          goOn(offset, anchori, anchorj, c, output, move, newRack, current, offOfReverse);
        } else {
          // implement blank tiles
        }
      }
    } else {
      log.fine("no more possibilities");
    }
    
  }
  
  private void goOn(
      int offset,
      int anchori,
      int anchorj,
      Character tile,
      ArrayList<Move> output,
      Move move,
      ArrayList<Character> rack,
      GADDAG base,
      boolean offOfReverse) {
    
    if (base != null) {
      log.fine("" + offset);
      if (offset <= 0) {
        addPlay(move, tile, anchori, anchorj + offset);
        if (base.hasAsEnd(tile) && !hasTile(anchori, anchorj + offset - 1) && !hasTile(anchori, anchorj + 1)) {
          recordMove(move, output);
        }
        GADDAG current = base;
        log.fine("" + current);
        log.fine("genning 1 up with move:\n " + move);
        genMoves(offset - 1, anchori, anchorj, output, move, rack, current, false);
        current = base.get('@');
        if (current != null && !hasTile(anchori, anchorj + offset - 1)) {
          log.fine("" + current);
          log.fine("genning reverse with move:\n " + move);
          genMoves(1, anchori, anchorj, output, move, rack, current, true);
        }
        
      } else if (offset > 0) {
        addPlay(move, tile, anchori, anchorj + offset);
        
        if (base.hasAsEnd(tile) && !hasTile(anchori, anchorj + offset + 1)) {
          recordMove(move, output);
        }
        if (offOfReverse) {
          base = base.get(tile);
          if (base == null) {
            return;
          }
        }
        log.fine("genning 1 down with move:\n " + move);
        GADDAG current = base;
        log.fine("" + current);
        genMoves(offset + 1, anchori, anchorj, output, move, rack, current, false);
      }
    } else {
      log.fine("Invalid trans");
    }
    
  }
  
  private void addPlay(Move move, Character tile, int i, int j) {
    if (flip) {
      move.addPlay(tile, j, i);
    } else {
      move.addPlay(tile, i, j);
    }
    
  }
  
  private void recordMove(Move move, ArrayList<Move> output) {
    Move record = new Move(move);
    output.add(record);
    log.fine("Recorded:\n " + record + "\n");
    
  }
  
  public void computeCrossSets() {
    GADDAG current = lexicon;
    for (int j = 0; j < height; j++ ) {
      for (int i = width - 1; i >= 0; i-- ) {
        if (!hasTile(i, j)) {
          computeCrossSet(i, j);
          current = lexicon;
        }
      }
    }
  }
  
  private void computeCrossSet(int i, int j) {
    GADDAG current = lexicon;
    if (hasTile(i - 1, j) && hasTile(i + 1, j)) {
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
        GADDAG base = current;
        for (char c : CharIterator.iter()) {
          current = base;
          current = current.get(c);
          x = i + 1;
          while (current != null && hasTile(x + 1, j)) {
            current = current.get(getTile(x, j));
            x++ ;
          }
          if (current != null) {
            if (current.hasAsEnd(getTile(x, j))) {
              get(i, j).addLegalSet(c);
              System.out.println("Adding " + c);
            }
          }
        }
      }
      
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
  
  public boolean isFliped() {
    return flip;
  }
  
  public void setFliped(boolean flip) {
    this.flip = flip;
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
    File lexicon = new File(args[1]);
    GameBoard board = GameBoard.buildGameBoard(new Scanner(input), new Scanner(lexicon));
    Logger logger = Logger.getLogger("");
    logger.addHandler(new SimpleHandler());
    logger.setLevel(Level.INFO);
    System.out.println(board);
    board.setFliped(true);
    board.computeAnchors();
    board.computeCrossSets();
    Scanner in = new Scanner(System.in);
    ArrayList<Character> rack = new ArrayList<Character>();
    while (in.hasNext("[a-zA-Z]")) {
      rack.add(in.next("[a-zA-Z]").charAt(0));
    }
    List<Move> moves = board.computeMoves(rack);
    for (Move m : moves) {
      System.out.println("Move:");
      System.out.println(m);
      System.out.println("----------");
    }
  }
}

class Square {
  
  private final int letterMultiplier;
  private final int wordMultiplier;
  private Set<Character> legalSet;
  private Character tile;
  private boolean isAnchor;
  
  public static final Character BLANK = '#';
  
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
  
  public void addLegalSet(char c) {
    this.getLegalSet().add(c);
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
