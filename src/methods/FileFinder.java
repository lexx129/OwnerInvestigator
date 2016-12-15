package methods;

import classes.Main;
import classes.myFile;
import controllers.getOwnerCtrl;
import controllers.searchBySidResultCtrl;
import controllers.searchFilesBySidCtrl;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

// Класс, реализующий функционал прохода по папкам

public class FileFinder extends SimpleFileVisitor<Path> {

    private static ArrayList<File> found_files = new ArrayList<>();
    private static ArrayList<File> check_failed = new ArrayList<>();
    //    private final Matcher matcher;
    private int numMatches = 0;
    private boolean bypassAccess;
    public String sidPattern;
    private searchFilesBySidCtrl searcherCtrl;
    private getOwnerCtrl ownerCtrl;

    public FileFinder(boolean toBypass) {
        searcherCtrl = new searchFilesBySidCtrl();
        ownerCtrl = new getOwnerCtrl();
        this.bypassAccess = toBypass;
    }

    private void compare(Path path) throws IOException {
        String currOwnerSid;
        currOwnerSid = ownerCtrl.getSid(Files.getOwner(path.toAbsolutePath()));

        if (currOwnerSid.equals(sidPattern)) {
            numMatches++;
            myFile file = new myFile(path.toString());
            //   Main.filesOfUser.add(file);

            Main.filesOfUser.add(file);
        }
    }

    private void processDenied(Path deniedPath) {
        AclManager manager = new AclManager();

        try {
            manager.getFileAccess(System.getProperty("user.name"), deniedPath.toFile());
            compare(deniedPath);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | IOException e) {
            e.printStackTrace();
        }
    }

    public void done() throws IOException {
        System.out.println("Найдено файлов : " + numMatches);
//        System.out.println("Matched: " + numMatches);
        if (numMatches == 0)
            System.out.println("В указанной директории совпадений не обнаружено");
//            System.out.println("No files found in given directory");
        else {
            System.out.println("Найденные файлы:");
//            System.out.println("Files found: ");
            for (File found_file : Main.filesOfUser) {
                System.out.println(String.valueOf(found_file));
//                System.out.println(found_file);
            }
            if (!check_failed.isEmpty()) {
                System.out.println("Не удалось проверить (нет доступа):");
                for (File failed : check_failed) {
                    System.out.println(String.valueOf((failed)));
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
        if (bypassAccess)
            processDenied(file);
        if (!check_failed.contains(file.toFile()))
            check_failed.add(file.toFile());
        return FileVisitResult.CONTINUE;
    }

}
