package automata;

public interface RegexParser {
    public int getType(String regex);

    public boolean parse(String regex);
}
