package methods;

import classes.Main;
import controllers.domainSearchCtrl;
import controllers.getSidCtrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;

public class FileFinder extends SimpleFileVisitor<Path> {

    private static ArrayList<File> found_files = new ArrayList<>();
    private static ArrayList<File> check_failed = new ArrayList<>();
    //    private final Matcher matcher;
    private int numMatches = 0;
    private boolean bypassAccess;
    public String sidPattern;

    public FileFinder(boolean toBypass) {
        this.bypassAccess = toBypass;
    }

    private void compare(Path path) throws IOException {
        getSidCtrl sidCtrl = new getSidCtrl();
        String currOwnerSid = sidCtrl.getSid(Files.getOwner(path.toAbsolutePath()));

        if (currOwnerSid.equals(sidPattern)) {
            numMatches++;
            //  File file = new File(path));
            found_files.add(path.toFile());
        }
    }

    private void done() throws IOException {
        Main.writer.write("Совпадений : " + numMatches + "\r\n");
//        System.out.println("Matched: " + numMatches);
        if (numMatches == 0)
            Main.writer.write("В указанной директории совпадений не обнаружено \r\n");
//            System.out.println("No files found in given directory");
        else {
            Main.writer.write("Найденные файлы: \r\n");
//            System.out.println("Files found: ");
            for (File found_file : found_files) {
                Main.writer.write(String.valueOf(found_file) + "\r\n");
//                System.out.println(found_file);
            }
            if (!check_failed.isEmpty()) {
                Main.writer.write("\r\n Не удалось проверить (нет доступа): \r\n");
                for (File failed : check_failed) {
                    Main.writer.write(String.valueOf((failed)) + "\r\n");
                }
            }
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        compare(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//        System.out.println("Now going to: " + dir.toAbsolutePath());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.out.println("Access to file '" + file.toString() + "' is denied");
        if (bypassAccess) {
            UserPrincipal user = null;
            try {
                user = FileSystems.getDefault().getUserPrincipalLookupService().lookupPrincipalByName(System.getProperty("user.name"));
//            System.out.println(user.getName());
                if (AclManager.getAccess(user.getName()) == 0) { // добавление доступа прошло успешно
                    visitFile(file, )
                }
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | IOException e) {
                e.printStackTrace();
            }
        }
        if (!check_failed.contains(file.toFile()))
            check_failed.add(file.toFile());
        return FileVisitResult.CONTINUE;
    }

}
