package people;


public class Sailor2 extends Person {
    boolean sleep;
    public Sailor2 ( ) {
        this.name = "Матрос 2";
        this.har = "сонный";
        this.side = true;
        this.hidden = false;
        this.sleep = true;

    }
    public boolean chSleep(){
        if(this.sleep == true){
        this.sleep = false;
        System.out.println( this.name + " проснулся");

    }
    else{
        this.sleep= true;
         System.out.println( this.name + " уснул");

    }
   
    return(sleep); 

    }
 @Override
     public  void walk(){
        System.out.println( name + " идет");


     }
     @Override
     public void seat(){
        System.out.println( name + " сидит");
    
     }


@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    
    Sailor2 other = (Sailor2) obj;
    return name != null && name.equals(other.name);
}

@Override
public int hashCode() {
    
    return name != null ? name.hashCode() : 0;
}

@Override
public String toString() {
    return name + " (" + 
           (sleep ? "спит" : "бодрствует") + 
           ", " + har + 
           ", " + (hidden ? "скрыт" : "виден") + 
           ")";
}
}