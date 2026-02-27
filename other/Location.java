package other;



public enum Location {
    FOREST("лес"),
    BAY("бухта"),
    COAST("берег"),
    SEA("море");
    final String name;
     Location(String name){
        this.name = name;
     }

     public static Location chLoc(String loc){
        System.out.println("Локация: " + loc );

     switch (loc) {
            case "лес" : return FOREST;
            case "бухта" : return BAY;
            case "берег" : return COAST;
            case "море" : return SEA;
            default : return FOREST;
        }
    


     

    }
}
