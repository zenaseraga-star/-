package people;
public class Sailor1 extends Person {
   boolean haveW;
   public Sailor1(){
      this.name = "Матрос 1";
      this.har = "беспрекословен";
      this.hidden =  false;
      this.side = true;
      this.haveW = true;

   }
   public boolean chHavew(){
       if(this.haveW == true){
        this.haveW = false;
    }
    else{
        this.haveW= true;
    }
   
    return(haveW);
        
    }
   

   
   public void putW(){
      System.out.println( this.name + " кладет оружее на землю");
      chHavew();
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
    
    Sailor1 other = (Sailor1) obj;
    return name != null && name.equals(other.name);
}

@Override
public int hashCode() {
    
    return name != null ? name.hashCode() : 0;
}

@Override
public String toString() {
    return name + " (" +  har +  ", " + (hidden ? "скрыт" : "виден") +  ")";
}

}
