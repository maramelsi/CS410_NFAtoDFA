import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class State {
    private String stateID;
    private Map<String, HashSet<State>> transitions;
    private Boolean startState;
    private Boolean finalState;

    public State(String stateID) {
        this.stateID = stateID;
        transitions = new HashMap<>();
        startState = false;
        finalState = false;
    }

    public void put(String symbol, State end){
        HashSet<State> tempSet;
        if (transitions.containsKey(symbol)){
            tempSet = transitions.get(symbol);
        }
        else{
            tempSet = new HashSet<>();
        }
        tempSet.add(end);
        transitions.put(symbol, tempSet);
    }

    public void replace(String symbol, State end){
        HashSet<State> tempSet = new HashSet<>();
        tempSet.add(end);
        transitions.put(symbol, tempSet);
    }
    public void printTransitions(){
        for (Map.Entry<String, HashSet<State>> set: transitions.entrySet()){
            HashSet<State> hs = set.getValue();
            Iterator itr = hs.iterator();
            while(itr.hasNext()){
                System.out.println(this.stateID + " " + set.getKey() + " " + itr.next().toString());
            }

        }
    }
    public void writeTransitions(FileWriter writer) throws IOException {
        for (Map.Entry<String, HashSet<State>> set: transitions.entrySet()){
            HashSet<State> hs = set.getValue();
            Iterator itr = hs.iterator();
            while(itr.hasNext()){
                writer.write(this.stateID + " " + set.getKey() + " " + itr.next().toString()+"\n");
            }

        }
    }

    @Override
    public String toString() {
        return this.stateID;
    }

    public char[] toCharArray() {
        char[] ch = new char[this.stateID.length()];
        for (int i = 0; i < this.stateID.length(); i++) {
            ch[i] = this.stateID.charAt(i);
        }
        return ch;
    }

    public String getStateID() {
        return stateID;
    }

    public Map<String, HashSet<State>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<String, HashSet<State>> transitions) {
        this.transitions = transitions;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public void setStartState() {
        this.startState = true;
    }

    public void setFinalState() {
        this.finalState = true;
    }

    public Boolean isStartState() {
        return startState;
    }

    public Boolean isFinalState() {
        return finalState;
    }
}
