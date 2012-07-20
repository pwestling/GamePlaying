import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GADDAGBuilder {
  
  public static void main(String[] args) throws FileNotFoundException {
    GADDAG rootMin = null;
    if (args.length == 0) {
      rootMin = buildGADDAG(new Scanner(System.in));
    } else {
      File file = new File(args[0]);
      rootMin = buildGADDAG(new Scanner(file));
    }
    // System.out.println("Prepping String...");
    // System.out.println(GADDAG);
    System.out.println("Done!");
    System.out.println("Nodes: " + GADDAG.idCounter);
    Scanner in = new Scanner(System.in);
    while (in.hasNext()) {
      System.out.println(rootMin.contains(in.next()));
    }
    
  }
  
  public static List<String> traverse(GADDAG root) {
    List<String> words = new ArrayList<String>();
    for (char c : root.getTransitions()) {
      GADDAG child = root.get(c);
      if (child == null)
        continue;
      for (String s : traverse(child)) {
        words.add("" + c + s);
      }
    }
    for (char end : root.getEndSet()) {
      words.add("" + end);
    }
    return words;
  }
  
  public static GADDAG buildGADDAGmin(Scanner words) {
    GADDAG root = new GADDAG();
    while (words.hasNext()) {
      GADDAG node = root;
      String word = words.next();
      String splitword1 = reverse(word);
      node = buildGADDAGbranch(node, splitword1);
      node = root;
      String splitword2 = reverse(word.substring(0, word.length() - 1)) + "@" + word.substring(word.length() - 1);
      node = buildGADDAGbranch(root, splitword2);
      for (int m = word.length() - 3; m >= 0; m-- ) {
        GADDAG temp = node;
        node = root;
        for (int i = m; i >= 0; i-- ) {
          node = node.put(word.charAt(i));
        }
        node = node.put('@');
        node.put(word.charAt(m + 1), temp);
      }
      
    }
    return root;
  }
  
  public static GADDAG buildGADDAG(Scanner words) {
    GADDAG root = new GADDAG();
    while (words.hasNext()) {
      String word = words.next().trim();
      for (int i = 1; i <= word.length(); i++ ) {
        String splitword = reverse(word.substring(0, i)) + "@" + word.substring(i);
        buildGADDAGbranch(root, splitword);
      }
    }
    return root;
  }
  
  private static GADDAG buildGADDAGbranch(GADDAG root, String word) {
    GADDAG current = root;
    for (int i = 0; i < word.length() - 1; i++ ) {
      current = current.put(word.charAt(i));
      if (i == word.length() - 2) {
        current.putEndSet(word.charAt(word.length() - 1));
      }
    }
    return current;
  }
  
  private static String reverse(String substring) {
    return (new StringBuffer(substring)).reverse().toString();
  }
}
