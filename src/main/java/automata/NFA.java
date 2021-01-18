package automata;

import java.util.ArrayList;
import java.util.List;


public class NFA implements Automota {

    private List<State> states = new ArrayList<>();
    private State start = new State("1",false);

    public void create(String regex){

    }
    
    
    
    public boolean accept(String input){return true;}
}
