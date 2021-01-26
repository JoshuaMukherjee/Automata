package automata;

import java.util.Stack;

/**
 * LL(1) Parser for the following grammar
 * <p> E -> T E' </p>
 * <p> E' -> | T E' </p>
 * <p> E' -> '' </p>
 * <p> T -> F T' </p>
 * <p> T' -> F T' </p>
 * <p> T' -> '' </p>
 * 
 * <p> F -> P P' </p>
 * 
 * <p> P -> ( E ) </p> 
 * <p> P -> <CHAR> </p>
 * <p> P' -> * </p>
 * <p> P' -> '' </p>
 */
public class LL1_Parser implements RegexParser {

    private static final int T_CHAR = 0; //A-Za-z
    private static final int T_R_PAR = 1; // )
    private static final int T_L_PAR = 2; // (
    private static final int T_BAR = 3; // |
    private static final int T_STAR = 4; // *
    private static final int T_END = 5; // $ 


    /**
     * Returns {@code int} based on highest level type
     * 
     * 0 if not valid syntax
     * 
     * @param regex
     * @return type of regex
     */
    public int getType(String regex) {
        Lexer(regex);
        return 0;

    }

    /**
     * Lexer for LL(1) Parser. Takes input string and returns tokens
     * @param input string
     * @return Tokens
     */
    public Stack<Integer> Lexer(String input) {
        System.out.println("Lexer");
        Stack<Integer> tokens = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if(c >= 64 && c <= 122){
                tokens.push(T_CHAR);
            }else if (c == '('){
                tokens.push(T_L_PAR);
            }else if (c == ')'){
                tokens.push(T_R_PAR);
            }else if (c == '|'){
                tokens.push(T_BAR);
            }else if (c == '*'){
                tokens.push(T_STAR);
            }
        }
        tokens.push(T_END);
        System.out.println(tokens);
        return tokens;
    }
}
 /*
E -> T E'
E' -> | T E' 
E' -> ''

T -> F T'
T' -> F T' 
T' -> ''

F -> P P'

P -> ( E ) 
P -> <CHAR>
P' -> * 
P' -> ''

*/