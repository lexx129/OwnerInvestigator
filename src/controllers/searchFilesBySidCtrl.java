package controllers;

import classes.myFile;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
import java.nio.file.SimpleFileVisitor;

// Контроллер формы, отвечающей за поиск всех файлов
// владельца с выбранным SID из списка

public class searchFilesBySidCtrl {

    // переменные верхней части окна (выбор файлов)
    @FXML
    TextField pathToSearchField;
    @FXML
    Button continueButton;
    @FXML
    CheckBox bypassAccess;
    // элементы нижней части формы с результатами поиска
    @FXML
    TableView<myFile> foundFilesList;
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
    @FXML
    Label indicatorLabel;
    public ObservableList<myFile> foundFiles;


    private Stage dialogStage;
    private String pathToSearch;
    public Task<ObservableList<myFile>> task;
    public ObservableValue<String> value1;
    Thread t;
    Main main;

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
            Main.filesOfUser = FXCollections.observableArrayList();
            // Main.searchService = new searchBySidService(pathToSearch, bypassAccess.isSelected());
//            t.start();
            initSearch();
            //    this.continueButton.getScene().getWindow().hide();
        }
    }

    //инициализация таблицы вывода найденных файлов и фонового сервиса, выполняющего поиск
    @FXML
    void initialize() {
        this.main = new Main();

        //        searchTarget.setText(Main.chosenUser.getSid());
        foundFilesList.setPlaceholder(new Label("Нет элементов для отображения"));
        // Описываем столбцы таблицы, имена должны совпадать с именами полей
        fileName.setCellValueFactory(new PropertyValueFactory<File, String>("name"));
        fileChangeDate.setCellValueFactory(new PropertyValueFactory<File, String>("changeDate"));
        fileType.setCellValueFactory(new PropertyValueFactory<File, String>("type"));
        fileSize.setCellValueFactory(new PropertyValueFactory<File, Long>("size"));



        foundFilesList.setRowFactory(new Callback<TableView<myFile>, TableRow<myFile>>() {
            @Override
            public TableRow<myFile> call(TableView<myFile> param) {
                final TableRow<myFile> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem showInExplorerItem = new MenuItem("Показать в проводнике");
                showInExplorerItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            // Показ файла в проводнике
                            File chosen = foundFilesList.getItems().get(row.getIndex());
                            Runtime.getRuntime().exec("explorer.exe /select," + chosen.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                contextMenu.getItems().add(showInExplorerItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );
                return row;
            }
        });
    }

    private void initSearch() {
        foundFiles = FXCollections.observableArrayList();

        ObservableValue<String> value = new ReadOnlyObjectWrapper<>(String.valueOf(Main.filesOfUser.size()));
        ObservableValue<String> value1 = new SimpleStringProperty(String.valueOf(Main.filesOfUser.size()));
        //связываем таблицу с найденными файлами с переменной, куда они сохраняются

        foundFilesList.setItems(Main.filesOfUser);
        foundFilesAmount.setText("0");
        task = new Task<ObservableList<myFile>>() {
            @Override
            protected ObservableList<myFile> call() throws Exception {
                String targetSid = Main.chosenUser.getSid();
//                System.out.println("Reference target is: " + owner.toString());
              //  ObservableList<myFile> foundFiles = FXCollections.observableArrayList();
//        System.out.println("Владелец целевого файла:  " + owner.toString());
                //   ArrayList<File> found_files;
                File dir = new File(pathToSearch);
                Path startingDir = dir.toPath();
                FileFinder finder = new FileFinder(bypassAccess.isSelected());
                finder.sidPattern = targetSid;
//                main.showFileSearcherLayout("foundFiles");

                    Files.walkFileTree(startingDir, finder);
                    updateMessage(String.valueOf(Main.filesOfUser.size()));

                //   found_files = finder.found_files;
                finder.done();
                return foundFiles;
            }
        };

        Platform.setImplicitExit(false);
        // настраиваем видимость анимации загрузки и подписи только во время поиска
        progressIndicator.visibleProperty().bind(task.runningProperty());
        indicatorLabel.visibleProperty().bind(task.runningProperty());

        Main.filesOfUser.addListener(new ListChangeListener<myFile>() {
            @Override
            public void onChanged(Change<? extends myFile> c) {
//                while (c.next()){
//                    if (c.wasAdded())
//                        setCounter(c.getAddedSize());
////
//                }
               // if (Main.filesOfUser.size() % delay == 0)
                    setCounter(Main.filesOfUser.size());
            }
        });

        progressIndicator.setProgress(-1d);
//        Main.searchService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                progressIndicator.setProgress(0d);
//                //      Main.filesOfUser = Main.searchService.getValue();
//            }
//        });
//        Main.searchService.setOnFailed(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                System.out.println("Something gone wrong while searching for files by SID");
//                System.out.println(event.getEventType().getName());
//                System.out.println(event.getSource().getMessage());
//            }
//        });
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
              //  foundFilesList.setItems(foundFiles);
                progressIndicator.setProgress(0d);
            }
        });
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Something went wrong while searching files..");
                Throwable exc = event.getSource().getException();
                exc.printStackTrace();
                System.out.println(event.getEventType().getName());

            }
        });


        Thread t = new Thread(task);
      //  foundFilesAmount.textProperty().bind(value1);
        t.setDaemon(true);
        t.start();
    }

    public void setCounter(int number) {
//      Long currValue = Long.valueOf(foundFilesAmount.getText());
//        foundFilesAmount.setText(String.valueOf(currValue + number));
//        foundFiles = number;
        foundFilesAmount.setText(String.valueOf(number));
    }

}
