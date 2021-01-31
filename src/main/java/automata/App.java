package automata;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args )
    {
        RegexParser p = new LL1_Parser();
        System.out.println(args[0]);
        try{
            // p.getType("(a)");
            System.out.println(p.parse(args[0]));
        } catch (RuntimeException e){
            System.err.println("error: " + e.getClass());
        }
    }
}
