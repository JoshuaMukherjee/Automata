package automata;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args )
    {
        RegexParser p = new LL1_Parser();
        try{
            // p.getType("(a)");
            System.out.println(p.parse("a|b*"));
        } catch (RuntimeException e){
            System.err.println("error: " + e.getClass());
        }
    }
}
