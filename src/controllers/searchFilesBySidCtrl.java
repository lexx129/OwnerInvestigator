package controllers;

import methods.FileFinder;
import classes.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        if (pathToSearch != null) {

        }
    }

    public void findFiles() throws IOException {

        String targetSid = Main.chosenUser.getSid();
//                System.out.println("Reference target is: " + owner.toString());

//        System.out.println("Владелец целевого файла:  " + owner.toString());
        //   ArrayList<File> found_files;
        File dir = new File(pathToSearch);
        Path startingDir = dir.toPath();
        FileFinder finder = new FileFinder();
        finder.sidPattern = targetSid;
        Files.walkFileTree(startingDir, finder);
        //   found_files = finder.found_files;
        finder.done();
        writer.close();
        open(savepath + "\\" + ("result.txt"));
    }
}
