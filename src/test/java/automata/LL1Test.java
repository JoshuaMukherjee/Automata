package automata;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LL1Test {

    @Test
    public void Lexer(){
        LL1_Parser parser = new LL1_Parser();
        List<Integer> tokens = parser.Lexer("a");
        List<Integer> correct = new ArrayList<>();
        correct.add(0);
        correct.add(5);
        assertEquals(correct, tokens);

    }
    
}
