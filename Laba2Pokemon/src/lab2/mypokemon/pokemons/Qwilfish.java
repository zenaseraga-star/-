package lab2.mypokemon.pokemons;

import lab2.mypokemon.mymove.qwish.AquaTail;
import lab2.mypokemon.mymove.qwish.Bubble;
import lab2.mypokemon.mymove.qwish.Facade;
import lab2.mypokemon.mymove.qwish.Harden;
import ru.ifmo.se.pokemon.*;

public final class Qwilfish extends Pokemon{
    public Qwilfish(String name, int level){
        super(name, level);

        super.setType(Type.WATER, Type.POISON);
        super.setStats(65, 95, 85, 55, 55, 85);

        Facade facede = new Facade(70, 100);
        Harden harden = new Harden(0, 0);
        Bubble bubble = new Bubble(65, 100);
        AquaTail aquaTail = new AquaTail(90, 90);

        super.setMove(facede, harden, bubble, aquaTail);
    }
}
