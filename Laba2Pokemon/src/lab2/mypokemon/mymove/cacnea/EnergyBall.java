package lab2.mypokemon.mymove.cacnea;

import ru.ifmo.se.pokemon.*;

public class EnergyBall extends SpecialMove {
    public EnergyBall(double pow, double acc) {
        super(Type.GRASS, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect defLowerEffect = new Effect().chance(0.1).stat(Stat.DEFENSE, -1);
        p.addEffect(defLowerEffect);
    }

    @Override
    protected String describe() {
        return "использует " + this.getClass().getSimpleName();
    }
}
