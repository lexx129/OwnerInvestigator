package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import classes.FileOwner;
import classes.Main;
import classes.User;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class MainController {

    private Main main;

    @FXML
    private TableView<User> ownerTable;
    @FXML
    private TableColumn<User, Integer> columnNum;
    @FXML
    private TableColumn<User, String> userSid;

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
    private AnchorPane resultsLayout;
    private Stage dialogStage;


    private void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void initialize() {
        columnNum.setCellValueFactory(new PropertyValueFactory<User, Integer>("№"));
        userSid.setCellValueFactory(new PropertyValueFactory<User, String>("SID"));
        setMain(new Main());
    }

    @FXML
    private void handleFile(ActionEvent e) throws IOException {
//        input = new TextField();
//        stage = new Stage();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать файл..");
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is;
        try {
            is = new FileInputStream(".\\preDiploma.properties");
            props.load(is);
        } catch (FileNotFoundException e1) {
            System.out.println("***caught!***");
        }
//        File file1 = new File(".\\preDiploma.properties");
//        System.out.println(file1.getAbsolutePath());
        OutputStream os;
        File initial;
        if (props.getProperty("initial_directory") != null) {
            initial = new File(props.getProperty("initial_directory"));
            fileChooser.setInitialDirectory(initial);
        }
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            os = new FileOutputStream(".\\preDiploma.properties");
            props.setProperty("initial_directory", file.getParent());
            props.store(os, null);
            os.close();
            path = file.toPath();
            this.okButton.setDisable(false);
            input.setText(file.toString());
        }
    }

    @FXML
    private void handleOk() throws IOException {
        if (path != null) {
            Main.singleFilePath = path.toString();
            Main.singleFileOwner = FileOwner.scanFile(new File(String.valueOf(path)));
            main.showResultsLayout();
        }
    }

    @FXML
    private void handleCancel() {
        Main.singleFilePath = "";
        this.okButton.getScene().getWindow().hide();
    }




}


