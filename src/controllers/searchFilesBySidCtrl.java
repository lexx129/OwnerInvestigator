package controllers;

import javafx.scene.control.*;
import methods.FileFinder;
import classes.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

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

    // объекты формы с результатом поиска
    @FXML
    TableView<File> foundFilesList;
    @FXML
    TableColumn<File, String> fileName;
    @FXML
    TableColumn<File, String> fileChangeDate;
    @FXML
    TableColumn<File, String> fileType;
    @FXML
    TableColumn<File, Long> fileSize;
    @FXML
    TextField searchTarget;
    @FXML
    TextField foundFilesAmount;
    @FXML
    ProgressIndicator progressIndicator;

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
        if (pathToSearch != null) {
            try {
                findFiles();

                this.continueButton.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void findFiles() throws IOException {

        String targetSid = Main.chosenUser.getSid();
//                System.out.println("Reference target is: " + owner.toString());

//        System.out.println("Владелец целевого файла:  " + owner.toString());
        //   ArrayList<File> found_files;
        File dir = new File(pathToSearch);
        Path startingDir = dir.toPath();
        FileFinder finder = new FileFinder(bypassAccess.isSelected());
        finder.sidPattern = targetSid;
        Files.walkFileTree(startingDir, finder);
        //   found_files = finder.found_files;
        finder.done();
    }
}
