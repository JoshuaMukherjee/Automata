package automata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State {
    private String id;
    private boolean acceptState;
    private Map<String,Set<State>> transitions = new HashMap<>();


    public State(String id, boolean accept){
        this.id = id;
        this.acceptState = accept;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAcceptState() {
        return acceptState;
    }

    public void setAcceptState(boolean acceptState) {
        this.acceptState = acceptState;
    }

    public Set<State> transition(String s){
        return transitions.get(s);
    }

    public void AddTransition(String s, Set<State> state){
        transitions.put(s, state);
    }

}
