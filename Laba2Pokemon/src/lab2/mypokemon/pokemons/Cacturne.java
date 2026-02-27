package lab2.mypokemon.pokemons;
import lab2.mypokemon.mymove.cacture.FocusBlast;
import ru.ifmo.se.pokemon.Type;

public final class Cacturne extends Cacnea {
    public Cacturne(String name, int level) {
        super(name, level);
        super.addType(Type.DARK);
        super.setStats(70, 115, 60, 115, 60, 55);

        addMove(new FocusBlast(120, 70));
    }
}
