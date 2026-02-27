package things;
public class Weapon {
    boolean inHand;
 public Weapon(){
    this.inHand = true;

 }
 public boolean wh(){
     if(this.inHand== true){
        this.inHand = false;
        System.out.println( " оружее матроса теперь на земле " );

    }
    else{
        this.inHand= true; 
        System.out.println( "оружее теперь в руках");
    }
   
    return(inHand);
 }
}
