package hri.speech.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EntityTrie {
  int numNodes = 1;
  Map<String, Integer> edgeMap = new HashMap<String, Integer>();
  Map<Integer, String> entityMap = new HashMap<Integer, String>();

  String edgeKey(Integer node, String word) {
    return node + "_" + word;
  }

  Integer crossEdge(Integer node, String word) {
    return edgeMap.get(edgeKey(node, word));
  }

  Integer crossEdgeOrAdd(Integer node, String word) {
    Integer newNode = crossEdge(node, word);
    if (newNode == null) {
      newNode = numNodes;
      numNodes = numNodes + 1;
      edgeMap.put(edgeKey(node, word), newNode);
    }
    return newNode;
  }

  public void addPattern(String sPattern, String entity) {
    sPattern = sPattern.trim().replaceAll(" +", " ");
    sPattern = sPattern.replaceAll("\\[ ", "\\[").replaceAll(" \\]", "]");
    List<String> matchWords = Arrays.asList(sPattern.split(" "));
    // System.out.println("pattern " + sPattern);

    List<Integer> nodeList = new LinkedList<Integer>();
    nodeList.add(0);

    for (String strWord : matchWords) {
      boolean bOptional = strWord.startsWith("[");
      if (bOptional) {
        strWord = strWord.substring(strWord.indexOf('[') + 1,
            strWord.indexOf(']'));
      }

      List<Integer> nodeListNew = new LinkedList<Integer>();
      if (bOptional) {
        nodeListNew.addAll(nodeList);
      }

      for (Integer node : nodeList) {
        nodeListNew.add(crossEdgeOrAdd(node, strWord));
      }
      nodeList = nodeListNew;
    }

    for (Integer node : nodeList) {
      entityMap.put(node, entity);
    }
  }

  public String toString() {
    return "Edges " + edgeMap.toString() + "\nEntities " + entityMap.toString();
  }

  public MatchingEntities getPrefixMatches(List<String> words) {
    MatchingEntities matchingEntities = new MatchingEntities();
    Integer currentNode = 0;
    String currentMatchedWords = "";

    for (String strWord : words) {
      Integer node = crossEdge(currentNode, strWord);
      if (node == null)
        break;
      String entity = entityMap.get(node);
      currentMatchedWords = currentMatchedWords + " " + strWord;
      currentMatchedWords = currentMatchedWords.trim();
      if (entity != null) {
        matchingEntities.add(entity, currentMatchedWords);
      }
      currentNode = node;
    }

    return matchingEntities;
  }

  public MatchingEntities getAllMatches(List<String> words) {
    MatchingEntities matchingEntities = getPrefixMatches(words);
    for (int i = 1; i < words.size(); i++) {
      matchingEntities.add(getPrefixMatches(words.subList(i, words.size())));
    }
    return matchingEntities;
  }

  public MatchingEntities getEntities(String sentence) {
    return getAllMatches(Arrays.asList(sentence.split(" ")));
  }

  /**
   * <pre>
   * ASSUMPTION about pattern line form:
   *   basePattern = word 
   *       OR [optWord]
   *       OR basePattern basePattern
   *   pattern = basePattern
   *       OR (basePattern | pattern)
   *       OR <empty>
   * </pre>
   */
  public void loadEntitiesFromFile(String entitiesFile)
      throws FileNotFoundException {

    long start = System.currentTimeMillis();
    Scanner in = new Scanner(new FileInputStream(entitiesFile));
    String entityType = "";
    while (in.hasNextLine()) {
      String l = in.nextLine().trim();
      if (l.isEmpty())
        continue;
      if (l.startsWith("<")) {
        entityType = l.substring(l.indexOf('<') + 1, l.indexOf('>'));
        continue;
      }
      if (l.startsWith("(") || l.startsWith("|")) {
        if (!l.contains(")"))
          l = l + in.nextLine().trim();
        // System.out.println(l + l.indexOf('('));
        String entityString = entityType + ":"
            + l.substring(l.indexOf('"') + 1, l.lastIndexOf('"'));
        String fullPatternString = l.substring(l.indexOf('(') + 1,
            l.indexOf(')'));
        for (String patternString : fullPatternString.split("\\|")) {
          patternString = patternString.trim().replace(" +", " ");
          patternString = patternString.replace("\\[ +", "\\[").replace(
              " +\\]", "]");
          // System.out.println(patternString);
          addPattern(patternString, entityString);
        }
      }
    }

    long end = System.currentTimeMillis();
    System.out.println("Time taken in loading: " + (end - start));

  }

  public static void testTrie() {
    EntityTrie trie = new EntityTrie();
    trie.addPattern("[A] B [C] D", "1");
    trie.addPattern("B D E", "2");
    System.out.println(trie.toString());
    System.out.println(trie.getEntities("A B D E"));
    System.out.println(trie.getEntities("B D E"));
  }

}
