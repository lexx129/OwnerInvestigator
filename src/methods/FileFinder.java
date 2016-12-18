package methods;

import classes.Main;
import classes.myFile;
import controllers.getOwnerCtrl;
import controllers.searchFilesBySidCtrl;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
    private String currDeniedPath;
    private int accessDeniedCaught;

    public FileFinder(boolean toBypass) {
        searcherCtrl = new searchFilesBySidCtrl();
        ownerCtrl = new getOwnerCtrl();
        this.bypassAccess = toBypass;
        accessDeniedCaught = 0;
    }

    private void compare(Path file) throws IOException {
        String currOwnerSid;
        //       accessDeniedCaught = 0;
        try {
            currOwnerSid = ownerCtrl.getSid(Files.getOwner(file.toAbsolutePath()));
            accessDeniedCaught = 0;
            if (currOwnerSid.equals(sidPattern)) {
                numMatches++;
                myFile found = new myFile(file.toString());
                //   Main.filesOfUser.add(file);

                Main.filesOfUser.add(found);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (AccessDeniedException exc) {
            System.out.println("No access to file " + file.toString());
            if (bypassAccess)
                try {
                    processDenied(file);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
//            if (!check_failed.contains(file.toFile()))
//                check_failed.add(file.toFile());
            //exc.printStackTrace();
        }
    }

    private void processDenied(Path deniedPath) throws ExecutionException, InterruptedException {
        AclManager manager = new AclManager();
        accessDeniedCaught++;   // если доступ запрещен, то запрет нужно удалить, прежде добавлять права
        FutureTask<String> futureTask = new FutureTask(
                new AcceptanceDialog()
        );
        currDeniedPath = String.valueOf(deniedPath);
        Platform.runLater(futureTask);
        String res = futureTask.get();
        if (res.equals("Y")) {
            try {
                if (accessDeniedCaught > 1) { // если попытка выдать доступ не удалась, нужно удалить запрет
                    manager.removeForbiddance(System.getProperty("user.name"), deniedPath.toFile());
                    accessDeniedCaught = 0;
                }
                manager.getFileAccess(System.getProperty("user.name"), deniedPath.toFile());
                if (deniedPath.toFile().isFile())
                    compare(deniedPath);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void done() throws IOException {
        System.out.println("Найдено файлов : " + numMatches);
//        System.out.println("Matched: " + numMatches);
        if (numMatches == 0)
            System.out.println("В указанной директории совпадений не обнаружено");
//            System.out.println("No files found in given directory");
        else {
            //System.out.println("Найденные файлы:");
//            System.out.println("Files found: ");
            for (File found_file : Main.filesOfUser) {
//                System.out.println(String.valueOf(found_file));
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
        System.out.println("Now going to: " + dir.toAbsolutePath());
        if (Files.isWritable(dir)) // проверяем наличие доступа к директории
            return FileVisitResult.CONTINUE;
        else {
            System.out.println("Don't have access to " + dir.toString());
            if (!bypassAccess) { // если не нужно обходить доступ, пропускаем эту папку
                Main.failedFoldersAmount++;
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
        try {
            processDenied(dir); // иначе пытаемся получить права и обработать файлы внутри
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.out.println("Access to file '" + file.toString() + "' is denied");
        if (exc.toString().contains("java.nio.file.AccessDeniedException"))
            try {
                processDenied(file);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        return FileVisitResult.CONTINUE;
    }

    private class AcceptanceDialog implements Callable<String> {
        @Override
        public String call() throws Exception {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Подтвердите действие");
            if (accessDeniedCaught <= 1) {
                alert.setContentText("У вас нет доступа к '" + currDeniedPath +
                        "'\nВы действительно хотите его получить?");
            } else
                alert.setContentText("Доступ к '" + currDeniedPath + "' запрещен. \nСбросить?");
            ButtonType yes = new ButtonType("Да");
            ButtonType no = new ButtonType("Нет");
            alert.getButtonTypes().setAll(yes, no);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == yes) {
                System.out.println("Надо брать");
                return ("Y");
            }
            if (result.get() == no) {
                System.out.println("Не будем брать");
                Main.failedFilesAmount++;
                return ("NO");
            }
            return null;
        }
    }
}
