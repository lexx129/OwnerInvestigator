package classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;

/**
 * Created by Lexx on 11.12.2016.
 */
public class myFile extends File {

    private Long size;
    private FileTime changeDate;
    private String type;

    public Long getSize() {
        return size;
    }

    public FileTime getChangeDate() {
        return changeDate;
    }

    public String getType() {
        String ext = "";
        String path = this.getAbsolutePath();
        int i = path.lastIndexOf('.');
        if (i > 0)
            return path.substring(i);
        return "";
    }

    public myFile(String pathname) {
        super(pathname);
        this.size = this.length();
        try {
            this.changeDate = Files.getLastModifiedTime(this.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
