package lab2.mypokemon.mymove.qwish;
import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade(double pow, double acc) {
        super(Type.NORMAL, pow, acc);
    }

    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def) {
        double baseDamage = super.calcBaseDamage(att, def);
        Status p = att.getCondition();
        if (p == Status.POISON || p == Status.PARALYZE || p == Status.BURN) {
            return baseDamage * 2;
        }

        return baseDamage;
    }

    @Override
    protected String describe() {
        return "использует " + this.getClass().getSimpleName();
    }
}
