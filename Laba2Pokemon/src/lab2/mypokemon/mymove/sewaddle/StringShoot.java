package lab2.mypokemon.mymove.sewaddle;

import ru.ifmo.se.pokemon.*;

public class StringShoot extends StatusMove {
    public StringShoot(double pow,double acc){
        super(Type.BUG, pow, acc);
    }


    @Override
    protected void applyOppEffects(Pokemon p){
        Effect speedLow = new Effect().stat(Stat.SPEED, -2);
        p.addEffect(speedLow);
    }


    @Override
    protected String describe () {
        return "использует " + this.getClass().getSimpleName();

    }

}
