package classes;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Lexx on 05.12.2016.
 */
public class User {
    private int id;
    private String sid;
    private HashMap<String, String> files; // ключ - абсолютный путь файла, значения - имя файла

    public User(int id, String sid, HashMap<String, String> files) {
        this.id = id;
        this.sid = sid;
        this.files = files;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getSid().equals(user.getSid());

    }

    @Override
    public int hashCode() {
        return getSid().hashCode();
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public HashMap<String, String> getFiles() {
        return files;
    }

    public void addFile(String newFile) {
        File file = new File(newFile);
        if (!this.files.containsKey(newFile)){
            this.files.put(file.getAbsolutePath(), file.getName());
        }
    }
}
