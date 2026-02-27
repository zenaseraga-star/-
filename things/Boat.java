package things;

import people.Person;

public class Boat {
    boolean fool;

      
public Boat(){
    this.fool = true;
}
public boolean chFool(){
     if(this.fool == true){
        this.fool = false;
        System.out.println( "Матрос вылез из шлюпки" );

    }
    else{
        this.fool= true; 
        System.out.println("Герои сели в шлюпку и поплыли дальше");
    }
   
    return(fool);
        

}
}
    


