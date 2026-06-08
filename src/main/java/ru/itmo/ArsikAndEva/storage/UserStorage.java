//package ru.itmo.ArsikAndEva.storage;
//
//import ru.itmo.ArsikAndEva.users.User;
//import ru.itmo.ArsikAndEva.users.UserManager;
//
//import java.io.*;
//import java.util.HashMap;
//
//public class UserStorage {
//    private String filepath;
//    private UserManager userManager;
//    public UserStorage(String filePath){
//        this.filepath = filePath;
//    }
//    public  void save (HashMap<Long, User> users) throws IOException {
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))){
//            oos.writeObject(users);
//        }
//
//
//    }
//    public HashMap<Long, User> load() throws IOException, ClassNotFoundException {
//        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))){
//            return (HashMap<Long, User>) ois.readObject();
//        }
//    }
//}
