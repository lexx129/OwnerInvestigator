package methods;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Lexx on 05.12.2016.
 */
public class User {
    private int id;
    private String sid;
    private HashMap<String, String> files;

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
