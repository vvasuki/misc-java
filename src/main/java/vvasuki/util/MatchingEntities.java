package hri.speech.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

// Assumtion: Every entity occurs at most once in an utterance, and it is
// taken to match the longest string which fits the corresponding pattern.
public class MatchingEntities {
  // Eg: Name:honda_acura_service=acura
  Map<String, String> entity2words = new HashMap<String, String>();

  // Eg: acura=Name:honda_acura_service
  SetMultimap<String, String> words2entities = HashMultimap.create();

  // Eg: Name:honda_acura_service=Name_1
  BiMap<String, String> entity2Tag = HashBiMap.create();

  public void setWords2entities() {
    for (String entity : entity2words.keySet()) {
      words2entities.put(entity2words.get(entity), entity);
    }
  }

  public String toString() {
    return entity2words.toString();
  }

  public void add(MatchingEntities e) {
    for (String entity : e.entity2words.keySet()) {
      add(entity, e.entity2words.get(entity));
    }
  }

  public class Match {
    public String entity;
    public String matchedString;
    public String toString() {
      return entity + ":" + matchedString;
    }
  }

  public Match getMatch(String taggedString) {
    Match match = new Match();
    // System.out.println(taggedString);
    String entityId = entity2Tag.inverse().get(taggedString);
    match.entity = entityId.split(":")[1];
    match.matchedString = entity2words.get(entityId);
    // TODO Maybe fix this to do some cleverer matching.
    return match;
  }

  public void add(String entity, String words) {
    String currentWords = entity2words.get(entity);
    if (currentWords == null || currentWords.length() < words.length())
      entity2words.put(entity, words);
  }

  public void setEntityTags() {
    Set<String> entities = entity2words.keySet();
    Map<String, Integer> entityTypeCount = new HashMap<String, Integer>();
    for (String entity : entities) {
      String entityType = entity.substring(0, entity.indexOf(':'));
      int entityNum = 0;
      if (!entityTypeCount.containsKey(entityType)) {
        entityTypeCount.put(entityType, 1);
      } else {
        entityNum = entityTypeCount.get(entityType);
        entityTypeCount.put(entityType, entityNum + 1);
      }
      // System.out.println(entityTypeCount);
      entity2Tag.put(entity, entityType + "__" + entityNum);
    }
  }

  // TODO: Known error. Given bank, california, california bank trust:
  // produces [[california], [bank, california bank trust], [bank, california
  // bank trust]]
  public List<List<String>> groupMatchedTexts() {
    Set<String> matchedStrings = words2entities.keySet();

    ArrayList<List<String>> overlapGroups = new ArrayList<List<String>>();
    for (String match : matchedStrings) {
      List<String> overlapGroup = null;

      for (List<String> group : overlapGroups) {
        for (String gMatch : group) {
          // TODO: Ideally the following check should have been an
          // intersection.
          if (gMatch.contains(match) || match.contains(gMatch)) {
            overlapGroup = group;
            break;
          }
        }
      }
      if (overlapGroup == null)
        overlapGroup = new ArrayList<String>();
      overlapGroup.add(match);
      overlapGroups.add(overlapGroup);
    }
    return overlapGroups;
  }

  public Set<String> getEntityTaggings(String sentence) {
    setWords2entities();
    setEntityTags();
    // System.out.println(entity2Tag);
    Set<String> matchedStrings = words2entities.keySet();

    Set<String> taggings = new HashSet<String>();
    taggings.add(" " + sentence + " ");
    for (String matchedString : matchedStrings) {
      List<String> newTaggings = new ArrayList<String>();
      // System.out.println(matchedString + " : Entities matching : " + words2entities.get(matchedString));
      for (String entity : words2entities.get(matchedString)) {
        String entityTag = entity2Tag.get(entity);
        for (String tagging : taggings) {
          tagging = tagging.replace(" " + matchedString + " ", " " + entityTag
              + " ");
          newTaggings.add(tagging);
        }
      }
      taggings.addAll(newTaggings);
    }

    return taggings;
  }

}
