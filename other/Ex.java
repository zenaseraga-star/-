package other;

public class Ex extends Exception{
public Ex(String massage){
super(massage);
}
@Override
public String getMessage(){
    return "Ошибка при выполнении команд: " + super.getMessage();
}
}