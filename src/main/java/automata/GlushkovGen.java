package automata;

public class GlushkovGen implements Generator{

    @Override
    public Automota create() {
        return new NFA();
    }

    
    
}
