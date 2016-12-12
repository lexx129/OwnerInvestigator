package methods;

import classes.Main;
import classes.myFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class searchBySidService extends javafx.concurrent.Service<ObservableList> {
    private String pathToSearch;
    private boolean bypassAccess;

    public searchBySidService(String pathToSearch, boolean bypassAccess) {
        this.pathToSearch = pathToSearch;
        this.bypassAccess = bypassAccess;
    }

    @Override
    protected Task<ObservableList> createTask() {
        return new Task<ObservableList>() {
            @Override
            protected ObservableList<myFile> call() throws Exception {
                ObservableList<myFile> foundFiles = FXCollections.observableArrayList();
                Main main = new Main();
                String targetSid = Main.chosenUser.getSid();
//                System.out.println("Reference target is: " + owner.toString());

//        System.out.println("Владелец целевого файла:  " + owner.toString());
                //   ArrayList<File> found_files;
                File dir = new File(pathToSearch);
                Path startingDir = dir.toPath();
                FileFinder finder = new FileFinder(bypassAccess);
                finder.sidPattern = targetSid;
//                main.showFileSearcherLayout("foundFiles");
                Files.walkFileTree(startingDir, finder);
                //   found_files = finder.found_files;
                finder.done();
                return foundFiles;
            }
        };

    }
}
