package people;



public class Rob extends Person implements Command {
    public Rob(){
       
       this.name = "Робинзон Крузо";
        int a = (int) (10*Math.random());
        this.aut = a;
        this.har = "опытный";
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
    
    Rob other = (Rob) obj;
    return name != null && name.equals(other.name);
}

@Override
public int hashCode() {
    
    return name != null ? name.hashCode() : 0;
}

@Override
public String toString() {
    return name + " (" +  "авторитет: " + ", " + har + ", " + (hidden ? "скрыт" : "виден") + ", " + (side ? "мятежник" : " против мятежа"+ ")");
}
}
