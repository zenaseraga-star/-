package ru.itmo.ArsikAndEva.users;

public class SessionManager {
    private User currentUser;
    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }
    public User getCurrentUser(){
        return this.currentUser;
    }
    public boolean isExistUser(){
        return currentUser != null;
    }
    public void exitUser(){
        currentUser =null;
    }
}
