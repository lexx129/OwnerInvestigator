package methods;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;

public class FileOwner {
    public static ArrayList<UserPrincipal> owners = new ArrayList<>();

    public static UserPrincipal scanFile(File file) throws IOException {

        return Files.getOwner(file.toPath());
    }

    public static ArrayList<UserPrincipal> scanDirectory(File rootDir, boolean letBypass) {
//        System.out.println(rootDir);
        UserPrincipal owner;
        AclManager manager = new AclManager();
        File[] filesInDir = rootDir.listFiles();
        if (filesInDir != null){
            try {
                if (!rootDir.createNewFile()){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (filesInDir != null) {
            for (int i = 0; i < filesInDir.length; i++) {
                if (filesInDir[i].isDirectory())
                    scanDirectory(filesInDir[i], letBypass);
                else {
                    try {
                        owner = Files.getOwner(filesInDir[i].toPath());
                        if (!owners.contains(owner))
                            owners.add(owner);
                        //  System.out.println(owner);
                    } catch (AccessDeniedException exc) {
                        if (letBypass) {


                            try {
                                manager.getFileAccess(System.getProperty("user.name"), filesInDir[i]);
                                i--;
                            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return owners;
    }
}

