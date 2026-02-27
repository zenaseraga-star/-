package lab2.mypokemon.pokemons;

import lab2.mypokemon.mymove.cacnea.CottonSpore;
import lab2.mypokemon.mymove.cacnea.EnergyBall;
import lab2.mypokemon.mymove.cacnea.PinMissile;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Cacnea extends Pokemon {
    public Cacnea(String name, int level) {
        super(name, level);
        super.setType(Type.GRASS);
        super.setStats(50, 85, 40, 85, 40, 35);

        PinMissile pinMissile = new PinMissile(25, 95);
        EnergyBall energyBall = new EnergyBall(90, 100);
        CottonSpore cottonSpore = new CottonSpore(0, 100);

        setMove(pinMissile, energyBall, cottonSpore);
    }
}
