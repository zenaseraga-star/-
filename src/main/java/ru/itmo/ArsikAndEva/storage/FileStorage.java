package ru.itmo.ArsikAndEva.storage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class FileStorage  {

    private String filepath;
    public FileStorage( String filepath){
        this.filepath = filepath;
    }
    public void save(AllData allData) throws IOException {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream out = new FileOutputStream(filepath);
            oos = new ObjectOutputStream(out);
            oos.writeObject(allData);
        }  finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    public AllData load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            FileInputStream input = new FileInputStream(filepath);
            ois = new ObjectInputStream(input);
            return (AllData) ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }
    }


