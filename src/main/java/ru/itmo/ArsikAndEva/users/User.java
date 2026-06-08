package ru.itmo.ArsikAndEva.users;

import ru.itmo.ArsikAndEva.exception.ValidationException;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private final String login;
   private String password;
   private Long userId;
    public User(String login, String password, Long id){
        this.login = login;
        this.password= password;
        this.userId = id;
    }

    public String getLogin() {
        return login;
    }

    public Long getUsId() {
        return userId;
    }
    public boolean checkPassword(String password){
        if(this.password.equals(Hash.HashPass(password))){
            return true;
        }
       return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
