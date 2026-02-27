package lab2.mypokemon.mymove.qwish;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class AquaTail extends SpecialMove {
    public AquaTail(double pow, double acc){
        super(Type.WATER, pow, acc);
    }

    @Override
    protected String describe() {
        return "использует " + this.getClass().getSimpleName();
    }
}
