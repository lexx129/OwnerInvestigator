package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import methods.FileOwner;
import methods.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ChooseFileCtrl {
    @FXML
    FileChooser fileChooser;
    @FXML
    private Button openButton;
    @FXML
    public Path path;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField input;

    @FXML
    private void handleOk() throws IOException {
        if (path != null) {
            Main.singleFilePath = path.toString();
            Main.singleFileOwner = FileOwner.scanFile(new File(String.valueOf(path)));
        }

    }

    @FXML
    private void handleCancel() {
        Main.singleFilePath = "";
        this.okButton.getScene().getWindow().hide();
    }

}
