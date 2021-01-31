package automata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * LL(1) Parser for the following grammar. Implements the {@code RegexParser}
 * interface
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
 * 
 * @author Josh Mukherjee
 */
public class LL1_Parser implements RegexParser {

    private static final int T_CHAR = 0; // A-Za-z
    private static final int T_R_PAR = 1; // )
    private static final int T_L_PAR = 2; // (
    private static final int T_BAR = 3; // |
    private static final int T_STAR = 4; // *
    private static final int T_END = 5; // $s
    private static final int T_EPSILON = 6; // '' - empty string

    private static final int No_Terminals = 7; // number of termonals

    private static final int NT_E = -1; // E
    private static final int NT_Eprime = -2; // E'
    private static final int NT_T = -3; // T
    private static final int NT_Tprime = -4; // T'
    private static final int NT_F = -5; // F
    private static final int NT_P = -6; // P
    private static final int NT_Pprime = -7; // P'

    /**
     * List of all the rules. Each rule is a list of non-terminals ({@code Integer})
     */
    private static List<List<Integer>> rules;
    /**
     * Map of non-terminals ({@code Integer}) to the rules they can be expanded by
     */
    private static Map<Integer, List<Integer>> ruleIndex;
    /**
     * Map of non-terminals ({@code Integer}) to their first sets
     */
    private static Map<Integer, Set<Integer>> firstSets;
    /**
     * Map of non-terminals ({@code Integer}) to their follow sets
     */
    private static Map<Integer, Set<Integer>> followSets;

    /**
     * Parse table used in parsing First index of Non-terminal ({@code Integer})
     * Second index Character ({@code Integer})
     */
    private static Map<Integer, Map<Integer, Integer>> table;

    // Set up rules and parse table etc
    static {
        rules = new ArrayList<>();
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_T, NT_Eprime))); // Rule 1 E -> T E'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_BAR, NT_T, NT_Eprime))); // Rule 2 E' -> | T E'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_EPSILON))); // Rule 3 E' -> ''
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_F, NT_Tprime))); // Rule 4 T -> F T'
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_F, NT_Tprime))); // Rule 5 T' -> F T'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_EPSILON))); // Rule 6 T' -> ''
        rules.add(new ArrayList<Integer>(Arrays.asList(NT_P, NT_Pprime))); // Rule 7 F -> P P'
        rules.add(new ArrayList<Integer>(Arrays.asList(T_L_PAR, NT_E, T_R_PAR))); // Rule 8 P -> ( E )
        rules.add(new ArrayList<Integer>(Arrays.asList(T_CHAR))); // Rule 9 P -> CHAR
        rules.add(new ArrayList<Integer>(Arrays.asList(T_STAR))); // Rule 10 P' -> *
        rules.add(new ArrayList<Integer>(Arrays.asList(T_EPSILON))); // Rule 11 P' -> ''

        ruleIndex = new HashMap<>();
        ruleIndex.put(NT_E, Arrays.asList(0));
        ruleIndex.put(NT_Eprime, Arrays.asList(1, 2));
        ruleIndex.put(NT_T, Arrays.asList(3));
        ruleIndex.put(NT_Tprime, Arrays.asList(4, 5));
        ruleIndex.put(NT_F, Arrays.asList(6));
        ruleIndex.put(NT_P, Arrays.asList(7, 8));
        ruleIndex.put(NT_Pprime, Arrays.asList(9, 10));

        firstSets = GetFirstSets();
        followSets = GetFollowSets();
        table = getTable();
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
        // getSets();
        return 0;

    }

    /**
     * prints first sets, follow sets and parse table
     */
    public void getSets() {
        System.out.println(firstSets);
        System.out.println(followSets);
        Integer NT;
        for (int i = 1; i <= ruleIndex.size(); i++) {
            NT = -1 * i;
            System.out.print(NT);
            System.out.println(table.get(NT));

        }
    }

    /**
     * Lexer for LL(1) Parser. Takes input string and returns tokens
     * 
     * @param input string
     * @return {@code List} of Tokens (of type {@code int})
     */
    public List<Integer> Lexer(String input) throws RuntimeException {
        System.out.println("Generating tokens...");
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
        // System.out.print("tokens: ");
        // System.out.println(tokens);
        return tokens;
    }

    /**
     * Gets all firsts sets, uses {@code getFirstSet} method for each Non-Terminal
     * 
     * @return {@code Map} of {@code Set} objects for each NT
     * @see #getFirstSet
     */
    public static Map<Integer, Set<Integer>> GetFirstSets() {

        Map<Integer, Set<Integer>> firsts = new HashMap<>();
        int NT;
        for (int i = 1; i <= ruleIndex.size(); i++) {
            NT = -1 * i;
            firsts.put(NT, getFirstSet(NT));
        }
        return firsts;

    }

    /**
     * Compute the first set of a given non terminal
     * 
     * @param NT the non terminal to compute the first set for
     * @return {@code Set} of terminals
     * @see #GetFirstSets
     */
    public static Set<Integer> getFirstSet(int NT) {
        Set<Integer> firstSet = new HashSet<>();
        List<Integer> ruleList = ruleIndex.get(NT);
        List<Integer> currentRule;
        Integer t;
        for (Integer r : ruleList) {
            currentRule = rules.get(r);
            for (int i = 0; i < currentRule.size(); i++) {
                t = currentRule.get(i);
                if (t >= 0) {
                    firstSet.add(t);
                    break;
                } else {
                    if (t != NT) {
                        firstSet.addAll(getFirstSet(t));
                        break;
                    }
                }
            }

        }
        return firstSet;
    }

    /**
     * Generates all follow sets for each Non-Terminal
     * 
     * @return {@code Map} of {@code Set} objects for each NT
     */
    public static Map<Integer, Set<Integer>> GetFollowSets() {
        Map<Integer, Set<Integer>> follows = new HashMap<>();
        Integer NT;
        Set<Integer> s;
        for (int i = 1; i <= ruleIndex.size(); i++) {
            NT = -1 * i;
            s = new HashSet<>();
            follows.put(NT, s);
        }
        follows.get(NT_E).add(T_END);

        Integer token;
        Integer next;
        boolean changes = true;
        List<Integer> ruleList;
        List<Integer> currentRule;

        while (changes) {
            changes = false;
            for (int i = 1; i <= ruleIndex.size(); i++) {
                ruleList = ruleIndex.get(-1 * i);
                for (Integer r : ruleList) {
                    currentRule = rules.get(r);
                    for (int j = 0; j < currentRule.size(); j++) {
                        token = currentRule.get(j);
                        if (token < 0) {
                            if (j + 1 < currentRule.size()) {
                                next = currentRule.get(j + 1);
                                if (next >= 0) {
                                    changes = changes || follows.get(token).add(next);
                                } else {
                                    changes = changes || follows.get(token).addAll(firstSets.get(next));
                                    if (firstSets.get(next).contains(T_EPSILON)) {
                                        changes = changes || follows.get(token).addAll(follows.get(-1 * i));
                                    }
                                }
                            } else {
                                changes = changes || follows.get(token).addAll(follows.get(-1 * i));
                            }
                        }
                    }
                }
            }
        }
        for (int i = 1; i <= ruleIndex.size(); i++) {
            NT = -1 * i;
            follows.get(NT).remove(T_EPSILON);
        }
        return follows;
    }

    /**
     * Generates Parse Table
     * 
     * @return {@code Map<Integer,Map<Integer,Integer>>} Table
     */
    public static Map<Integer, Map<Integer, Integer>> getTable() {
        Map<Integer, Map<Integer, Integer>> tab = new HashMap<>();
        Integer NT;
        for (int i = 1; i <= ruleIndex.size(); i++) {
            NT = -1 * i;
            Map<Integer, Integer> m = new HashMap<>();
            for (int j = 0; j < No_Terminals; j++) {
                if (j != T_EPSILON) {
                    m.put(j, null);
                }
            }
            tab.put(NT, m);
        }

        List<Integer> ruleSet;
        for (int i = 1; i <= ruleIndex.size(); i++) {
            NT = -1 * i;
            ruleSet = ruleIndex.get(NT);
            for (Integer r : ruleSet) {
                if (rules.get(r).contains(T_EPSILON)) {
                    for (Integer token : followSets.get(NT)) {
                        if (token != T_EPSILON) {
                            tab.get(NT).put(token, r);
                        }
                    }
                } else {
                    for (Integer token : firstSets.get(NT)) {
                        if ((rules.get(r).contains(token)
                                || (rules.get(r).get(0) < 0 && getFirstSet(rules.get(r).get(0)).contains(token)))
                                && token != T_EPSILON) {
                            tab.get(NT).put(token, r);
                        } // SOMETHING WRONG -6,2 = 8 not 7?
                    }
                }
            }
        }
        return tab;
    }

    @Override
    public boolean parse(String regex) {
        Stack<Integer> stack = new Stack<>();
        List<Integer> tokens = Lexer(regex);

        System.out.println("Parsing...");
        stack.push(T_END);
        stack.push(NT_E);
        int index = 0;
        Integer token;
        Integer toCompare;
        Integer r;
        Stack<Integer> temp = new Stack<>();
        Integer a;
        while (!stack.isEmpty()) {
            // System.out.println(stack);
            token = stack.pop();
            toCompare = tokens.get(index);
            if (token >= 0) {
                if (token == toCompare) {
                    if (toCompare == T_END) {
                        return true;
                    }
                    index++;
                } else {
                    // System.out.print(toCompare);
                    // System.out.println(token);
                    return false;
                }

            } else {
                r = table.get(token).get(toCompare);
                if(r == null){
                    return false;
                }
                for (Integer rule : rules.get(r)) {
                    temp.push(rule);
                }
                while (!temp.isEmpty()) {
                    a = temp.pop();
                    if (a != T_EPSILON) {
                        stack.push(a);
                    }
                }
            }
        }

        return false;
    }
}
