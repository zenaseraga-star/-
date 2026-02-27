import lab2.mypokemon.pokemons.*;
import ru.ifmo.se.pokemon.*;

class Program{
    public static void main(String[] args) {
        Battle b = new Battle();
        Cacnea a1 = new Cacnea("Чужой", 1);
        Qwilfish a2 = new Qwilfish("Рыбка", 1);
        Leavanny a3 = new Leavanny("Сильнейший", 2);

        Cacturne f1 = new Cacturne("Чужой2", 1);
        Sewaddle f2 = new Sewaddle("слабейший", 1);
        Swadloon f3 = new Swadloon("средняк", 2);

        
        b.addAlly(a1);
        b.addFoe(f1);
        b.addAlly(a2);
        b.addFoe(f2);
        b.addAlly(a3);
        b.addFoe(f3);
        b.go();
    }
}
