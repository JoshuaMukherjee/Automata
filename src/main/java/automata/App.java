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
            p.getType("(he\'\'llo)");
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        System.out.println( new GlushkovGen("hi").create() );
    }
}
