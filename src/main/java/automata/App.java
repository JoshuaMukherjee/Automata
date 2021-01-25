package automata;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args )
    {
        RegexParser p = new LL1_Parser();
        p.getType("hello");
        System.out.println( new GlushkovGen("hi").create() );
    }
}
