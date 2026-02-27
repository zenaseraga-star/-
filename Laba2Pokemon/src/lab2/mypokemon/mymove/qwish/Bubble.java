package lab2.mypokemon.mymove.qwish;

import ru.ifmo.se.pokemon.*;

public class Bubble extends SpecialMove {
    

    public Bubble(double pow, double acc) {
        super(Type.WATER, pow, acc);
    }
    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect speedLowerEffect = new Effect().chance(0.1).stat(Stat.SPEED, -1);
        p.addEffect(speedLowerEffect);
    }



    @Override
    protected String describe() {
        return "использует " + this.getClass().getSimpleName();
    }
}

