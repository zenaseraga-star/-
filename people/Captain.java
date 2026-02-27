package people;
  public class  Captain extends Person implements Command {

    public Captain(){
        this.name = "Капитан";
    
        this.side = false;
        this.hidden = false;
        this.aut = 10;
        this.har ="опытный";

    }
    public void attac(Person v){
        if(this.hidden = true){
            System.out.println( this.name + " бросается на " + v.name );
        this.hidden = false;}
    }
    
    public void convince(Person v){
        System.out.println( name + " Сдавайся, или умрешь!");
       if(v.har == "беспрекословен") {
        chSide(v);}
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
    
    Captain other = (Captain) obj;
    return name != null && name.equals(other.name);
}

@Override
public int hashCode() {
    
    return name != null ? name.hashCode() : 0;
}

@Override
public String toString() {
    return name + " (" +  "авторитет: " + this.aut + ", " + har + ", " + (hidden ? "скрыт" : "виден") + ", " + (side ? "мятежник" : " против мятежа"+ ")");
}
     

}