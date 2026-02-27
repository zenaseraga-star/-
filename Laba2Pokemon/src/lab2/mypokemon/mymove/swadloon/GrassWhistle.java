package lab2.mypokemon.mymove.swadloon;

import ru.ifmo.se.pokemon.*;

public class GrassWhistle extends StatusMove {
    public GrassWhistle(double pow, double acc){
        super(Type.GRASS, pow, acc);
    }


    @Override
    protected void applyOppEffects(Pokemon p){
        if (p.getCondition() != Status.SLEEP) {
            int i = (int)(Math.random() * 3 + 1);
            Effect sleepEff = new Effect().turns(i).condition(Status.SLEEP);
            p.addEffect(sleepEff);
        }
    }

    @Override
    protected String describe() {
        return "использует " +  this.getClass().getSimpleName();
    }

}
