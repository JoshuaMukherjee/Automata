package automata;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args )
    {
        RegexParser p = new LL1_Parser();
        p.getType("(he\'\'llo)");
        System.out.println( new GlushkovGen("hi").create() );
    }
}
