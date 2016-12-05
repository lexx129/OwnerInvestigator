package methods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;

public class FileOwner {
    public static ArrayList<UserPrincipal> owners = new ArrayList<>();

    public static UserPrincipal scanFile(File file) throws IOException {

        return Files.getOwner(file.toPath());
    }

    public static ArrayList<UserPrincipal> scanDirectory(File rootDir) {
        System.out.println(rootDir);
        UserPrincipal owner;

        File[] filesInDir = rootDir.listFiles();

        try {
            for (File file : filesInDir) {
                if (file.isDirectory())
                    scanDirectory(file);
                else {
                    owner = Files.getOwner(file.toPath());
                    if (!owners.contains(owner))
                        owners.add(owner);
                    //  System.out.println(owner);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return owners;
    }
}

