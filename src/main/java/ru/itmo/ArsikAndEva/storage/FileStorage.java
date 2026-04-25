package ru.itmo.ArsikAndEva.storage;
import java.io.*;
public class FileStorage  {

    private String filepath;
    public FileStorage(String filepath){
        this.filepath = filepath;
    }

    public void save(AllData allData) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))){
            oos.writeObject(allData);
        }
    }

    public AllData load() throws IOException, ClassNotFoundException {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))){
                return (AllData) ois.readObject();
            }
    }
}



