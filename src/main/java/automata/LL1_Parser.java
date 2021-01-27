package automata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * LL(1) Parser for the following grammar
 * <p>
 * 1. E -> T E'
 * </p>
 * <p>
 * 2. E' -> | T E'
 * </p>
 * <p>
 * 3. E' -> ''
 * </p>
 * <p>
 * 4. T -> F T'
 * </p>
 * <p>
 * 5. T' -> F T'
 * </p>
 * <p>
 * 6. T' -> ''
 * </p>
 * 
 * <p>
 * 7. F -> P P'
 * </p>
 * 
 * <p>
 * 8. P -> ( E )
 * </p>
 * <p>
 * 9. P -> CHAR
 * </p>
 * <p>
 * 10. P' -> *
 * </p>
 * <p>
 * 11. P' -> ''
 * </p>
 */
public class LL1_Parser implements RegexParser {

    private static final int T_CHAR = 0; // A-Za-z
    private static final int T_R_PAR = 1; // )
    private static final int T_L_PAR = 2; // (
    private static final int T_BAR = 3; // |
    private static final int T_STAR = 4; // *
    private static final int T_END = 5; // $
    private static final int T_EPSILON = 6; // ''

    private static final int NT_E = -1; // E
    private static final int NT_Eprime = -2; // E'
    private static final int NT_T = -3; // T
    private static final int NT_Tprime = -4; // T'
    private static final int NT_F = -5; // F
    private static final int NT_P = -6; // P
    private static final int NT_Pprime = -7; // P'

    private static List<List<Integer>> rules;
    private static Map<Integer,List<Integer>> ruleIndex;
    private static Map<Integer, Set<Integer>> firsts;

    static {
        rules = new ArrayList<>();
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_T, NT_Eprime))); // Rule 1 E -> T E'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_BAR, NT_T, NT_Eprime))); // Rule 2 E' -> | T E'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_EPSILON))); // Rule 3 E' -> ''
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_F, NT_Tprime))); // Rule 4 T -> F T'
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_F, NT_Tprime))); // Rule 5 T' -> F T'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_EPSILON))); // Rule 6  T' -> ''
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_P,NT_Pprime))); // Rule 7 F -> P P'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_L_PAR, NT_E, T_R_PAR))); // Rule 8 P -> ( E )
        rules.add(new ArrayList<Integer>(Arrays.asList(T_CHAR))); // Rule 9 P -> CHAR
        rules.add(new ArrayList<Integer>(Arrays.asList(T_STAR))); // Rule 10 P' -> *
        rules.add(new ArrayList<Integer>(Arrays.asList(T_EPSILON))); // Rule 11 P' -> ''

        ruleIndex = new HashMap<>();
        ruleIndex.put(NT_E, Arrays.asList(0));
        ruleIndex.put(NT_Eprime, Arrays.asList(1,2));
        ruleIndex.put(NT_T, Arrays.asList(3));
        ruleIndex.put(NT_Tprime, Arrays.asList(4,5));
        ruleIndex.put(NT_F, Arrays.asList(6));
        ruleIndex.put(NT_P, Arrays.asList(7,8));
        ruleIndex.put(NT_Pprime, Arrays.asList(9,10));

        firsts = GetFirstSets();
    }

    /**
     * Returns {@code int} based on highest level type
     * 
     * 0 if not valid syntax
     * 
     * @param regex
     * @return {@int} type of regex
     */
    public int getType(String regex) throws RuntimeException {
        Lexer(regex);
        System.out.println(firsts);
        return 0;

    }

    /**
     * Lexer for LL(1) Parser. Takes input string and returns tokens
     * 
     * @param input string
     * @return {@code List} of Tokens (of type {@code int})
     */
    public List<Integer> Lexer(String input) throws RuntimeException {
        System.out.println("Lexer...");
        List<Integer> tokens = new ArrayList<>();
        boolean inEpsilon = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122)) {
                tokens.add(T_CHAR);
                inEpsilon = false;
            } else if (c == '(') {
                tokens.add(T_L_PAR);
                inEpsilon = false;
            } else if (c == ')') {
                tokens.add(T_R_PAR);
                inEpsilon = false;
            } else if (c == '|') {
                tokens.add(T_BAR);
                inEpsilon = false;
            } else if (c == '*') {
                tokens.add(T_STAR);
                inEpsilon = false;
            } else if (c == '\'') {
                if (!inEpsilon && i + 1 < input.length() && input.charAt(i + 1) == '\'') {
                    tokens.add(T_EPSILON);
                    inEpsilon = true;
                } else if (!(inEpsilon)) {
                    throw new RuntimeException("Unexpected token " + c);
                } else {
                    inEpsilon = false;
                }
            } else {
                throw new RuntimeException("Unexpected token " + c);
            }
        }
        tokens.add(T_END);
        System.out.print("tokens: ");
        System.out.println(tokens);
        return tokens;
    }

    /**
     * Gets all firsts sets, uses {@code getFirstSet} method for each Non-Terminal
     * 
     * @return {@code Map} of {@code Set} objects for each NT
     */
    public static Map<Integer, Set<Integer>> GetFirstSets() {

        Map<Integer,Set<Integer>> firsts = new HashMap<>();
        int NT;
        for(int i=1;i<=ruleIndex.size();i++){
            NT = -1*i;
            firsts.put(NT, getFirstSet(NT));
        }


        return firsts;

    }

    /**
     * Compute the first set of a given non terminal
     * @param NT the non terminal to compute the first set for
     * @return {@code Set} of terminals 
     */
    public static Set<Integer> getFirstSet(int NT){
        Set<Integer> firstSet = new HashSet<>();
        List<Integer> ruleList = ruleIndex.get(NT);
        List<Integer> currentRule;
        Integer t;
        for (Integer r : ruleList) {
            currentRule = rules.get(r);
            for (int i = 0; i < currentRule.size(); i++) {
                t = currentRule.get(i);
                if(t >= 0){
                    firstSet.add(t);
                    break;
                }else{
                    if(t != NT){
                        firstSet.addAll(getFirstSet(t));
                        break;
                    }
                }
            }
            
        }
        return firstSet;
    }
}
