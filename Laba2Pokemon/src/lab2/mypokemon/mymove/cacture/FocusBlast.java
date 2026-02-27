package lab2.mypokemon.mymove.cacture;

import ru.ifmo.se.pokemon.*;

public class FocusBlast extends SpecialMove {
    public FocusBlast(double pow, double acc) {
        super(Type.FIGHTING, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect specialDefLowerEff = new Effect().chance(0.1).stat(Stat.SPECIAL_DEFENSE, -1);
        p.addEffect(specialDefLowerEff);
    }


    @Override
    protected String describe() {
        return "использует " + this.getClass().getSimpleName();
    }
}
