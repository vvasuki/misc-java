package hri.speech.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Transition {
  String output;
  State nextState;
  Transition(String outputIn, State nextStateIn){output = outputIn; nextState = nextStateIn;}
}

class State {
  Integer id;
  boolean isFinal = false;
  State(int i, boolean b){id = i; isFinal = b;}
}

public class DeterministicFST {
  int numStates = 1;
  
  public State rootState = new State(0, false);
  
  static enum TransitType {DEFAULT, NEW, SELF}
  
  Map<String, Transition> transitionMap = new HashMap<String, Transition>();
  
  String transitKey(State state, String word) {
    return state.id + "_" + word;
  }
 
  Transition defaultTransit(State state) {
    String key = transitKey(state, "DEFAULT");
    return transitionMap.get(key);
  }
  
  Transition transit(State state, String word, TransitType onFailure) {
    String key = transitKey(state, word);
    if(transitionMap.containsKey(key))
      return transitionMap.get(key);
    else switch (onFailure){
      case DEFAULT: return defaultTransit(state);
      case NEW: {
        State nextState = getNewState(false);
        Transition t = new Transition(word, nextState);
        transitionMap.put(key, t);
        return t;
      }
      default:{
        Transition t = new Transition(word, state);
        transitionMap.put(key, t);
        return t;
      }
    }
  }
  
  public List<String> transduce(List<String> lstInput) {
    State currentState = rootState;
    Iterator<String> inputIter = lstInput.iterator();
    List<String> lstOutput = new ArrayList<String>();
    while(inputIter.hasNext()) {
      String strIn = inputIter.next();
      Transition trans = transit(currentState, strIn, TransitType.DEFAULT);
      currentState = trans.nextState;
      lstOutput.add(trans.output);
    }
    return lstOutput;
  }
  
  State getNewState(boolean isFinal) {
    return new State(numStates ++, isFinal);
  }
  
  
}

