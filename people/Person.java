package people;

import other.Location;

public abstract class Person {
   protected  String name;
    String har;
    boolean side;
    boolean hidden;
    int aut;
    protected Person() {
    
    }

    public void speak(String frase){
        System.out.println( name + " говорит: " + frase);
    
    }
    public  void l(){
        System.out.println( this.name + " лежит без сознания");
    };
 public abstract void walk();
 public abstract void seat();

    public String getHar() {
        return har;
    }
public boolean getSide() {
    return side;
}
public void showSide(){
    if(side == true ){
        System.out.println( name + " - мятежник");
    }
    else{
    System.out.println( name + " теперь  за капитана");

    }
}
    public boolean chSide(Person v){
        if(v.side == true){
        v.side = false;
    }
    else{
        v.side = true;
    }
   
    return(side);
        
    }
      


   public void showHide(){
    if(hidden == true){
        System.out.println( name + " скрыт");
    }
    else{
    System.out.println( name + " раскрыт");
   }
   }
   public boolean chHide(){
    if(hidden == true){
        hidden = false;
    }
    else{
        hidden = true;
    }
   
    return(hidden);
        
    }
    public void showAut(){
        System.out.println( " авторитет: " + aut);
    }

    public Location crossover(){
        Location loc = Location.COAST;
         System.out.println(this.name + " перебрался через бухточку и бежит на помощь");
        return loc;
       
        
    }
     public void doing(){
        System.out.println(this.name + " занимается своими делами");
    }
    public boolean loud(){
        if(Math.random() < 0.1){
this.hidden = false;
System.out.println(this.name + " пошумел ");
return hidden;
        } 
    else return true;  }
   
    public boolean att(Person c, Person v){
      if( c.har == "сильный"){
         System.out.println( c.name + "Вырубает врага с одного удара");
         return true;
      }
      else if(v.har == "сильный"){
          System.out.println( v.name + "Вырубает врага с одного удара");
          return true;
      }
      else  {
         System.out.println( c.name + " атакует " + v.name);
         if(Math.random() > 0.5 ){
            System.out.println(c.name + " побеждает в бою "+ v.name);
            v.l();
            return true;

         }
         else{  System.out.println(v.name + " побеждает в бою "+ c.name);
            c.l();
            return false;
         }
      }
     
   }
   
   }


