package people;

public class Helper extends Person {
    public Helper(){
        this.name = "Помощник капитана";
        this.hidden = false;
        this.side = false;
        this.har ="отважный";
    }
    public void doing(){
        System.out.println(this.name + " занимается своими деами");
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
    
    Helper other = (Helper) obj;
    return name != null && name.equals(other.name);
}

@Override
public int hashCode() {
    
    return name != null ? name.hashCode() : 0;
}

@Override
public String toString() {
    return name + " ("  + har + ", " + (hidden ? "скрыт" : "виден") + ", " + (side ? "мятежник" : " против мятежа"+ ")");
}

}
