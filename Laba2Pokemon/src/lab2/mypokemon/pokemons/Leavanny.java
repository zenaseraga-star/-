package lab2.mypokemon.pokemons;


import lab2.mypokemon.mymove.leavanny.EnergyBall;

public final class Leavanny extends Swadloon {
    public Leavanny(String name, int level){
        super(name, level);
        super.setStats(75, 103, 80, 70, 80, 92);

        addMove(new EnergyBall(90, 100));
    }
}