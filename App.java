
import java.util.ArrayList;
import other.Ex;
import other.Location;
import people.*;
import things.*;

public class App {
    public static void main(String[] args) throws Exception {
         Location loc = Location.FOREST;
           record Rope( String name, boolean tied){
    public String isTied(){
        System.out.print(this.name);
        return (tied? "привязана к лодке" : " не привязана к лодке");
 }}
       Rope rope = new Rope("веревка на берегу", true);
        ArrayList<Person> team = new  ArrayList<>();
        Captain cap = new Captain();
        Rob rob = new Rob();
        Friday fri = new Friday();
        Sailor1 s1 = new Sailor1();
     Sailor2 s2 = new Sailor2();
     Helper help = new Helper();
     Boat boat = new Boat();
     Weapon weap = new Weapon();
    team.add(rob);
    team.add(help);
    team.add(cap);
try{ rob.command(rob, fri, "останьтесь здесь");
     rob.command1(rob, team, "идите за мной");
}
catch(Ex e){
    System.out.println(  e.getMessage());
     
        cap.command(cap, fri, "останьтесь здесь");
     
     cap.command1(cap, team, "идите за мной");
     }
  finally{
    loc =Location.chLoc("бухта");
    cap.command1(cap, team, "спрячьтесь");
    cap.chHide();
    cap.showHide();
    loc = Location.chLoc("берег");
    if(
    rob.loud() == false || help.loud() == false){
        
        s1.speak( " ТРЕВОГА, ВРАГИ ");
        cap.chHide();
        rob.chHide();
        help.chHide();
         s2.chSleep();
         boat.chFool();

         fri.crossover();
         
         boolean c = s2.att(s2, cap);
        if( c == true){
            s2.speak("Капитан повержен, сдавайтесь!");
        fri.speak("Капитан!");
        fri.att(fri, s2);
        weap.wh();
        fri.att(fri, s1);
        System.out.println("Пятница спас команду, но не без потерь...");
         rope = new Rope("веревка", false);
         System.out.println( rope.isTied());
        boat.chFool();

        }
        else{ 
           
            weap.wh();
            cap.convince(s1);
            s1.showSide();
            System.out.println("Команда капитана одержалла трудную победу, все прошло хорошо. Счастливый Конец :)");
        }

         

    }
    else{
        cap.attac(s2);
        s2.chSleep();
        cap.chHide();
        rob.chHide();
        help.chHide();
        s2.l();
        weap.wh();
        s1.chHavew();
        cap.convince(s1);
        s1.showSide();
        System.out.println("Капитан одержал победу и матрос беспрекословно перешел на его сторону. Конец!");
        


    }


record Satur(String m, int number){
    

}
     Satur s = new Satur("май", 4);
System.out.println(s.m());

    }
  

}}