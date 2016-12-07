package controllers;

import classes.*;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.naming.NamingException;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.Properties;

public class MainController implements newSidListener {

    private Main main;

    @FXML
    private TableView<User> ownerTable;
    @FXML
    private TableColumn<User, Integer> id;
    @FXML
    private TableColumn<User, String> sid;


    @FXML
    SplitPane rootLayout;
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
        setMain(new Main());
        id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        sid.setCellValueFactory(new PropertyValueFactory<User, String>("sid"));
        ownerTable.setItems(Main.userList);

        ownerTable.setRowFactory(new Callback<TableView<User>, TableRow<User>>() {
            @Override
            public TableRow<User> call(TableView<User> param) {
                final TableRow<User> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem showInfoItem = new MenuItem("Показать расширенную информацию");
                showInfoItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FXMLLoader domainSearchLoader = new FXMLLoader(getClass().getResource("/views/domainLoginLayout.fxml"));
                        try {
                            Parent domainSearch = domainSearchLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        DomainSearchCtrl domainSearchCtrl = domainSearchLoader.getController();
                        try {
                            domainSearchCtrl.findOwnerInfo(ownerTable.getItems().get(row.getItem().getId()).getSid());
                        } catch (NamingException | UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                });
                contextMenu.getItems().add(showInfoItem);
                // настраиваем выпадение меню только для непустой строки
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu)null)
                        .otherwise(contextMenu)
                );
                return row;
            }
        });

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
        input.setText("");
//        this.okButton.getScene().getWindow().hide();
    }


    @Override
    public void newSidFound(SidFoundEvent event) {
        System.out.println("Wow, we received something!");
    }
}


