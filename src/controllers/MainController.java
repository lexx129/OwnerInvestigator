package controllers;

import classes.*;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import methods.FileOwner;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class mainController {

    private Main main;

    @FXML
    private TableView<User> ownerTable;
    @FXML
    private TableColumn<User, Integer> id;
    @FXML
    private TableColumn<User, String> sid;
    @FXML
    private TableColumn<User, String> name;


    @FXML
    SplitPane rootLayout;
    @FXML
    FileChooser fileChooser;

    @FXML
    DirectoryChooser dirChooser;
    private Button openButton;
    @FXML
    public Path pathToFile;
    @FXML
    private Path pathToDir;
    @FXML
    private Button okButton; // "Продолжить" из выбора файла
    @FXML
    private Button okButton2; // "Продолжить" из выбора папки
    @FXML
    private Button cancelButton;
    @FXML
    private TextField fileInput;
    @FXML
    private TextField dirInput;
    @FXML
    private AnchorPane resultsLayout;
    @FXML
    private CheckBox dirBypassAccess;
    private Stage dialogStage;
    public static Properties props;


    private void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void initialize() {
        setMain(new Main());
        ownerTable.setPlaceholder(new Label("Нет элементов для отображения"));
        id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        sid.setCellValueFactory(new PropertyValueFactory<User, String>("sid"));
        name.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        ownerTable.setItems(Main.userList);

        ownerTable.setRowFactory(new Callback<TableView<User>, TableRow<User>>() {
            @Override
            public TableRow<User> call(TableView<User> param) {
                final TableRow<User> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem showInfoItem = new MenuItem("Показать расширенную информацию");
                final MenuItem searchFilesItem = new MenuItem("Искать все файлы этого пользователя");
                showInfoItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
//                        FXMLLoader domainSearchLoader = new FXMLLoader(getClass().getResource("/views/domainLoginLayout.fxml"));
//                        try {
//                            Parent domainSearch = domainSearchLoader.load();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        domainSearchCtrl domainSearchCtrl = domainSearchLoader.getController();
//                        try {

                        Main.chosenUser = ownerTable.getItems().get(row.getItem().getId() - 1);
                        main.showDomainSearchLayout();
//                            domainSearchCtrl.findOwnerInfo(chosenUser.getSid(), chosenUser.getName());
//                        } catch (NamingException | UnknownHostException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
                searchFilesItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Main.chosenUser = ownerTable.getItems().get(row.getItem().getId() - 1);
                        main.showFileSearcherLayout();
                    }
                });
                contextMenu.getItems().add(showInfoItem);
                contextMenu.getItems().addAll(searchFilesItem);
                // настраиваем выпадение меню только для непустой строки
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );
                return row;
            }
        });
        // пытаемся найти последний выбранный путь из файла с настройками
        props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream is;
        try {
            is = new FileInputStream(".\\ownerInvestigator.properties");
            props.load(is);
        } catch (FileNotFoundException e1) {
            System.out.println("Can't find file with props!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFile(ActionEvent e) throws IOException {
//        fileInput = new TextField();
//        stage = new Stage();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Выбрать файл..");

//        File file1 = new File(".\\ownerInvestigator.properties");
//        System.out.println(file1.getAbsolutePath());
        OutputStream os;
        File initial;
        if (props.getProperty("root_lastSelectedFile") != null) {
            initial = new File(props.getProperty("root_lastSelectedFile"));
            fileChooser.setInitialDirectory(initial);
        }
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            os = new FileOutputStream(".\\ownerInvestigator.properties");
            props.setProperty("root_lastSelectedFile", file.getParent());
            props.store(os, null);
            os.close();
            pathToFile = file.toPath();
            this.okButton.setDisable(false);
            fileInput.setText(file.toString());
        }
    }

    @FXML
    private void handleDir(ActionEvent actionEvent) throws IOException {
        dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Выбрать папку...");
        OutputStream os;
        File initial;
        if (props.getProperty("root_lastSelectedDir") != null) {
            initial = new File(props.getProperty("root_lastSelectedDir"));
            dirChooser.setInitialDirectory(initial);
        }
        File file = dirChooser.showDialog(dialogStage);
        if (file != null) {
            os = new FileOutputStream(".\\ownerInvestigator.properties");
            props.setProperty("root_lastSelectedDir", file.getParent());
            props.store(os, null);
            os.close();
            pathToDir = file.toPath();
            this.okButton2.setDisable(false);
            dirInput.setText(file.toString());
        }
    }

    @FXML
    // обработчик для вкладки с выбором файла
    private void fileHandleOk() throws IOException {
        if (pathToFile != null) {
            Main.singleFilePath = pathToFile.toString();
            Main.singleFileOwner = FileOwner.scanFile(new File(String.valueOf(pathToFile)));
            Main.resultSwitch = 1;
            main.showResultsLayout();
        }
    }

    @FXML
    // обработчик для вкладки с выбором папки
    private void dirHandleOk() throws IOException {
        if (pathToDir != null) {
            Main.dirPath = pathToDir.toString();
            Main.dirFileOwners = FileOwner.scanDirectory(new File(String.valueOf(pathToDir)), dirBypassAccess.isSelected());
            Main.resultSwitch = 2;
            main.showResultsLayout();
            for (int i = 0; i < Main.dirFileOwners.size(); i++) {
                System.out.println(Main.dirFileOwners.get(i).getName());
            }
        }
    }

    @FXML
    private void fileHandleCancel() {
        Main.singleFilePath = "";
        fileInput.setText("");
        okButton.setDisable(true);
    }

    @FXML
    private void dirHandleCancel() {
        Main.dirPath = "";
        dirInput.setText("");
        okButton2.setDisable(true);
    }

}


