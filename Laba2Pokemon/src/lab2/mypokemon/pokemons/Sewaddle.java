package lab2.mypokemon.pokemons;

import lab2.mypokemon.mymove.sewaddle.Rest;
import lab2.mypokemon.mymove.sewaddle.StringShoot;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Sewaddle extends Pokemon {
    public Sewaddle(String name, int level){
        super(name, level);
        super.setType(Type.BUG, Type.GRASS);
        super.setStats(45, 53, 70, 40, 60, 42);

        Rest rest = new Rest();
        StringShoot stringShoot = new StringShoot(0, 40);

        super.setMove(rest, stringShoot);
    }
}
