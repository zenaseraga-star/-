package lab2.mypokemon.mymove.qwish;

import ru.ifmo.se.pokemon.*;

public class Harden extends StatusMove {
    public Harden(double pow, double acc) {
        super(Type.NORMAL, pow, acc);
    }


    @Override
    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.DEFENSE, 1);
    }

    @Override
    protected String describe() {
         return "использует" + this.getClass().getSimpleName();
    }
}

