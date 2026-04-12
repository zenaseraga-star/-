package ru.itmo.ArsikAndEva.cli;
public class ExitCommand implements Command{
    @Override
    public void execute(String[] args){
        System.out.println("Пока!");
        System.exit(0);
    }
}