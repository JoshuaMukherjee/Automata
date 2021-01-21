package automata;

import java.util.HashSet;
import java.util.Set;


public class NFA implements Automota {

    private State start = new State("1",false);
    private Set<State> currentStates = new HashSet<>();

    public void create(String regex){

    }
    

    public boolean accept(String input){return true;}
}
