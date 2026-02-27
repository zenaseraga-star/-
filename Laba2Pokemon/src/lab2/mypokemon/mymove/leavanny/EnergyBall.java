package lab2.mypokemon.mymove.leavanny;

import ru.ifmo.se.pokemon.*;

public class EnergyBall extends SpecialMove {
    public EnergyBall(double pow, double acc){
        super(Type.GRASS, pow, acc);
    }

    @Override
    protected void applyOppEffects(Pokemon p){
        Effect defLowEff = new Effect().chance(0.1).stat(Stat.SPECIAL_DEFENSE, -1);
        p.addEffect(defLowEff);
        if (defLowEff.success()){
            System.out.println("Ура-ура выпал джекпот, поэтому специальная защита " + p + " падает на один уровень");
        }
    }


    @Override
    protected String describe() {
        return "использует " + this.getClass().getSimpleName();
    }
}
