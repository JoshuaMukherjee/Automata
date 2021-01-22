package automata;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GlushkovGen implements Generator {

    private Set<String> lambda;
    private Set<String> P;
    private Set<String> D;
    private Set<String> F;
    private String regex;

    public GlushkovGen(String regex){
        this.regex = regex;
        lambda = CreateLambda();
    }

    @Override
    public Automota create() {
        return new NFA();
    }

    public Set<String> CreateLambda(){
        Set<String> lambda = new HashSet<>();
        if(regex.isEmpty()){
            lambda = Collections.emptySet();
            lambda.add("");
        }
    

        return lambda;
    }


    
}
