package lab2.mypokemon.mymove.sewaddle;


import ru.ifmo.se.pokemon.*;

public class Rest extends StatusMove {
    public Rest(){
        super(Type.PSYCHIC, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p){
        double fullHP = p.getStat(Stat.HP);
        p.setMod(Stat.HP, (int)(fullHP - p.getHP()));

        Effect sleepEff = new Effect().turns(2).condition(Status.SLEEP);
        p.addEffect(sleepEff);
    }


    @Override
    protected String describe () {
        return "использует " + this.getClass().getSimpleName() +  " ложиться на боковую на 2 хода и полностю восстанавливает HP";
    }
}
