package automata;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Test
    public void LexerLongInput(){
        LL1_Parser parser = new LL1_Parser();
        List<Integer> tokens = parser.Lexer("(a|b)*|''");
        List<Integer> correct = new ArrayList<>();
        correct.addAll(Arrays.asList(2, 0, 3, 0, 1, 4, 3, 6, 5));
        assertEquals(correct, tokens);
    }

    @Test
    public void LexerQuotes(){
        LL1_Parser parser = new LL1_Parser();
        List<Integer> tokens = parser.Lexer("''");
        List<Integer> correct = new ArrayList<>();
        correct.addAll(Arrays.asList(6, 5));
        assertEquals(correct, tokens);
    }

    @Test(expected = RuntimeException.class)
    public void Lexer3Quotes(){
        LL1_Parser parser = new LL1_Parser();
        parser.Lexer("'''");
    }

    @Test(expected = RuntimeException.class)
    public void LexerUnknownSymbol(){
        LL1_Parser parser = new LL1_Parser();
        parser.Lexer("§");
    }

    @Test
    public void ParserSingle(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(true,parser.parse("a"));
    }

    @Test
    public void ParserCat(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(true,parser.parse("ab"));
    }

    @Test
    public void ParserAlt(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(true,parser.parse("a|b"));
    }

    @Test
    public void ParserStar(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(true,parser.parse("a*"));
    }

    @Test
    public void ParserPar(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(true,parser.parse("(a|b)"));
    }

    @Test
    public void ParserLong(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(true,parser.parse("(a|b*)|ab"));
    }

    @Test
    public void ParserWrongPar(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(false,parser.parse("(ab"));
    }

    @Test
    public void ParserWrongAlt(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(false,parser.parse("(ab|"));
    }

    @Test
    public void ParserWrongStar(){
        LL1_Parser parser = new LL1_Parser();
        assertEquals(false,parser.parse("*"));
    }

}
