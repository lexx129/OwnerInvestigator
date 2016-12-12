package controllers;

import javafx.scene.control.*;
import methods.FileFinder;
import classes.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import methods.searchBySidService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Контроллер формы, отвечающей за поиск всех файлов
// владельца с выбранным SID из списка

public class searchFilesBySidCtrl {

    @FXML
    TextField pathToSearchField;
    @FXML
    Button continueButton;
    @FXML
    CheckBox bypassAccess;

    private Stage dialogStage;
    private String pathToSearch;


    @FXML
    private void handleDir(ActionEvent e) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File file = dirChooser.showDialog(dialogStage);
        if (file != null) {
            pathToSearch = file.getAbsolutePath();
            continueButton.setDisable(false);
            pathToSearchField.setText(file.toString());
        }
    }

    @FXML
    private void handleCancel() {
        pathToSearch = "";
        this.continueButton.getScene().getWindow().hide();
    }

    @FXML
    private void handleNext() {
        Main main = new Main();
        if (pathToSearch != null) {
            Main.searchService = new searchBySidService(pathToSearch, bypassAccess.isSelected());
            main.showFileSearcherLayout("foundFiles");

            this.continueButton.getScene().getWindow().hide();
        }
    }

    private void findFiles() throws IOException {

//        Main main = new Main();
//        String targetSid = Main.chosenUser.getSid();
////                System.out.println("Reference target is: " + owner.toString());
//
////        System.out.println("Владелец целевого файла:  " + owner.toString());
//        //   ArrayList<File> found_files;
//        File dir = new File(pathToSearch);
//        Path startingDir = dir.toPath();
//        FileFinder finder = new FileFinder(bypassAccess.isSelected());
//        finder.sidPattern = targetSid;
//        main.showFileSearcherLayout("foundFiles");
//        Files.walkFileTree(startingDir, finder);
//        //   found_files = finder.found_files;
//        finder.done();
    }



}
