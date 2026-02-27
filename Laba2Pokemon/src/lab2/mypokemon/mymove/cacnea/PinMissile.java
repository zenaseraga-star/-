package lab2.mypokemon.mymove.cacnea;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class PinMissile extends PhysicalMove {

    public PinMissile(double pow, double acc) {
        super(Type.BUG, pow, acc, 0, 2);
        this.hits = 2;
    }

    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def) {
        int hits;
        double r = Math.random();

        if (r < 3.0 / 8) {
            hits = 2;
        } else if (r < 6.0 / 8) {
            hits = 3;
        } else if (r < 7.0 / 8) {
            hits = 4;
        } else {
            hits = 5;
        }
        this.hits = hits;
        double totalPower = this.power * hits;


        double baseDamage = super.calcBaseDamage(att, def);

        return baseDamage * (totalPower / this.power);


        }
    @Override
    protected String describe() {
        return "использует " + this.getClass().getSimpleName() + ", попадает " + this.hits + " раз";

    }
}
