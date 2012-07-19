import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GADDAGBuilder {
  
  public static void main(String[] args) throws FileNotFoundException {
    GADDAG gaddag = null;
    if (args.length == 0) {
      gaddag = buildGADDAG(new Scanner(System.in));
    } else {
      File file = new File(args[0]);
      gaddag = buildGADDAG(new Scanner(file));
    }
    System.out.println(gaddag);
  }
  
  public static GADDAG buildGADDAG(Scanner words) {
    GADDAG root = new GADDAG("root");
    while (words.hasNext()) {
      String word = words.next();
      for (int i = 0; i < word.length(); i++ ) {
        String splitword = reverse(word.substring(0, i)) + "@" + word.substring(i, word.length());
        buildGADDAGbranch(root, splitword);
      }
      
    }
    System.out.println();
    return root;
  }
  
  private static void buildGADDAGbranch(GADDAG root, String word) {
    for (int i = 0; i < word.length(); i++ ) {
      root = root.putIfNull(String.valueOf(word.charAt(i)));
    }
  }
  
  private static String reverse(String substring) {
    return (new StringBuffer(substring)).reverse().toString();
  }
}
