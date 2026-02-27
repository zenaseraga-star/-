package lab2.mypokemon.mymove.cacnea;

import ru.ifmo.se.pokemon.*;

public class CottonSpore extends StatusMove {
    public CottonSpore(double pow, double acc) {
        super(Type.GRASS, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect speedLowerEff = new Effect().stat(Stat.SPEED, -2);
        p.addEffect(speedLowerEff);
    }


    @Override
    protected String describe () {
        return "использует " + this.getClass().getSimpleName();

    }

}
