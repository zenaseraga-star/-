package people;
import java.util.ArrayList;
import other.*;

public interface Command { 
     default Integer  command  ( Person c, Person v, String frase ) throws Ex{
        
    
        System.out.println(c.toString() + " командует "+ v.name +": " + frase );
        if(c.aut >= 5){
        
           
            switch (frase) {
                case "спрячься " :
                case "выйди из укрытия" :
                    
                v.chHide() ;
                case "иди за мной": v.walk();
                case "останьтесь здесь" : v.doing();
                    
                    break;
                default:  
                
                v.speak("я такую команду не знаю");
        
             } return 1;
            
        }
        else {
           
            throw new Ex( c.name + " Не имеет достаточного приоритета "+ "нужна 5, иммет: "+ c.aut);
            
        }
    }
    
     default Integer  command1( Person c, ArrayList<Person> team, String frase )throws Ex{
        
    
        System.out.println(c.toString() + " командует всему отряду: " + frase );
          if(c.aut >= 5){
        for(Person v: team){
           
       if(v == c){
                continue;
            }
         
        
           
            switch (frase) {
                case "спрячьтесь" :
                case "выйдите из укрытия" :
                    
                v.chHide() ;
                break;
                case "идите за мной": v.walk();
                break;
                case "останьтесь здесь" : v.doing();
                    
                    break;
                default:  
                
                v.speak("я такую команду не знаю");
                break;
        
             } 

            }
         
             return 1;
        }
        
         else {
             throw new Ex( c.name + " Не имеет достаточного приоритета "+ "нужна 5, имеет: "+ c.aut);
           
          
           
        }
    }
      
    }



