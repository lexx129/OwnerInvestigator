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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import methods.FileFinder;
import classes.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import methods.searchBySidService;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

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
    Label cancelledLabel;
    @FXML
    TextField foundFilesAmount;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Label indicatorLabel;
    @FXML
    Button stopSearchBtn;
    @FXML
    Button saveResBtn;


    private Stage dialogStage;
    private String pathToSearch;
    private Task<ObservableList<myFile>> task;
    public ObservableValue<String> value1;
    private Thread t;

    @FXML
    private void handleDir(ActionEvent e) throws IOException {
        DirectoryChooser dirChooser = new DirectoryChooser();
        mainController mCtrl = new mainController();
        OutputStream os;
        File initial;
        if (mainController.props.getProperty("sidSearcher_lastSelectedDir") != null) {
            initial = new File(mainController.props.getProperty("sidSearcher_lastSelectedDir"));
            dirChooser.setInitialDirectory(initial);
        }
        File file = dirChooser.showDialog(dialogStage);
        if (file != null) {
            os = new FileOutputStream(".\\ownerInvestigator.properties");
            mainController.props.setProperty("sidSearcher_lastSelectedDir", file.getParent());
            mainController.props.store(os, null);
            os.close();

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
        Main main = new Main();

        //        searchTarget.setText(Main.chosenUser.getSid());
        foundFilesList.setPlaceholder(new Label("Нет элементов для отображения"));
        // Описываем столбцы таблицы, имена должны совпадать с именами полей
        fileName.setCellValueFactory(new PropertyValueFactory<File, String>("name"));
        fileChangeDate.setCellValueFactory(new PropertyValueFactory<File, String>("changeDate"));
        fileType.setCellValueFactory(new PropertyValueFactory<File, String>("type"));
        fileSize.setCellValueFactory(new PropertyValueFactory<File, Long>("size"));

        progressIndicator.setVisible(false);

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
        // очищаем переменные перед каждым новым поиском
        Main.filesOfUser = FXCollections.observableArrayList();
        Main.failedFilesAmount = 0;
        Main.failedFoldersAmount = 0;
        cancelledLabel.setVisible(false);
        //связываем таблицу с найденными файлами с переменной, куда они сохраняются
        foundFilesList.setItems(Main.filesOfUser);

        stopSearchBtn.setDisable(false);
        saveResBtn.setDisable(false);

        task = new Task<ObservableList<myFile>>() {
            @Override
            protected ObservableList<myFile> call() throws Exception {
                String targetSid = Main.chosenUser.getSid();
                File dir = new File(pathToSearch);
                Path startingDir = dir.toPath();
                FileFinder finder = new FileFinder(bypassAccess.isSelected());
                finder.sidPattern = targetSid;
                updateMessage("0");
                System.out.println(Arrays.toString(Files.list(startingDir).toArray()));

                Files.walkFileTree(startingDir, finder);
                updateMessage(String.valueOf(Main.filesOfUser.size()));
                finder.done();
//                return foundFiles;
                return null;
            }

            @SuppressWarnings("deprecation")
            @Override
            protected void cancelled() {
                updateMessage(String.valueOf(Main.filesOfUser.size()));
                cancelledLabel.setVisible(true);
                if (t != null)
                    t.stop();
                super.cancelled();
            }

            //            @Override
//            protected void cancelled() {
//                if
//            }
        };


        Platform.setImplicitExit(false);
        // настраиваем видимость анимации загрузки и подписи только во время поиска
        progressIndicator.visibleProperty().bind(task.runningProperty());
        indicatorLabel.visibleProperty().bind(task.runningProperty());


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

                saveResBtn.setDisable(false);
                stopSearchBtn.setDisable(true);
            }
        });
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Something went wrong while searching files..");
                Throwable exc = event.getSource().getException();
                exc.printStackTrace();
                System.out.println(event.getEventType().getName());
                saveResBtn.setDisable(true);
                stopSearchBtn.setDisable(true);
            }
        });
        task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressIndicator.setProgress(0d);

                saveResBtn.setDisable(true);
                stopSearchBtn.setDisable(true);
            }
        });

        t = new Thread(task);
        foundFilesAmount.textProperty().bind(task.messageProperty());
        t.setDaemon(true);
        t.start();
    }

    @FXML
    @SuppressWarnings("deprecation")
    private void handleStopSearch() {
        task.cancel();
//        try {
//        //    foundFilesAmount.setText(String.valueOf(Main.filesOfUser.size()));
//
//            foundFilesList.setItems(FXCollections.observableArrayList());
//            t.stop();
//
//        } catch (Error e) {
//            System.out.println("Successfully stopped");
//            e.printStackTrace();
//        }
    }

    @FXML
    private void handleSaveRes() throws IOException {
        FileChooser savePathChooser = new FileChooser();
        File savePath = savePathChooser.showSaveDialog(dialogStage);
        if (savePath != null) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(savePath.getAbsoluteFile()));
            bw.write("Поиск файлов владельца с SID ='" + Main.chosenUser.getSid() +
                    "' успешно завершен. \r\nНайдено файлов: " + Main.filesOfUser.size() +
                    "\r\nНе удалось обработать: " + Main.failedFilesAmount + "; В том числе папок: " +
                    Main.failedFoldersAmount + "\r\n\r\nСписок найденных файлов: \r\n");
            bw.flush();
            for (File foundFile : Main.filesOfUser) {
                bw.write(foundFile.getAbsolutePath() + "\r\n");
            }
            bw.flush();
            bw.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Сохранено");
            alert.setContentText("Результат сохранен в '" + savePath.getAbsolutePath() + "'");
            ButtonType openBtn = new ButtonType("Открыть сохраненное");
            ButtonType okBtn = new ButtonType("OK");
            alert.getButtonTypes().setAll(openBtn, okBtn);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == openBtn) {
                openSavedInfo(savePath);
            }
            if (result.get() == okBtn)
                alert.close();
        }
    }

    private void openSavedInfo(File savePath) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            if (desktop != null) {
                desktop.open(new File(savePath.getAbsolutePath()));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public void setCounter(int number) {
//      Long currValue = Long.valueOf(foundFilesAmount.getText());
//        foundFilesAmount.setText(String.valueOf(currValue + number));
//        foundFiles = number;
        foundFilesAmount.setText(String.valueOf(number));
    }

}
