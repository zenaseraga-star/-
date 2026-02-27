package people;

public class Friday extends Person{
    public Friday (){
        this.name = "Пятница";
        this.hidden = false;
        this.har = "сильный";
        this.side = false;

    }
    public void doing(){
        System.out.println(this.name + " занимается своими делами");
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
    
    Friday other = (Friday) obj;
    return name != null && name.equals(other.name);
}

@Override
public int hashCode() {
    
    return name != null ? name.hashCode() : 0;
}

@Override
public String toString() {
    return name + " (" +  har + ", " + (hidden ? "скрыт" : "виден") + ", " + (side ? "мятежник" : " против мятежа"+ ")");
}


}
