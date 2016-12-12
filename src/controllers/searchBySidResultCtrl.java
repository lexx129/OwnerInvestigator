package controllers;

import classes.Main;
import classes.myFile;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import methods.searchBySidService;

import java.io.File;
import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;

public class searchBySidResultCtrl {
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

    Main main;

    @FXML
    void initialize() {
        this.main = new Main();
        searchTarget.setText(Main.chosenUser.getSid());

        foundFilesAmount.setText("0");

        foundFilesList.setPlaceholder(new Label("Нет элементов для отображения"));
        // Описываем столбцы таблицы, имена должны совпадать с именами полей
        fileName.setCellValueFactory(new PropertyValueFactory<File, String>("name"));
        fileChangeDate.setCellValueFactory(new PropertyValueFactory<File, String>("changeDate"));
        fileType.setCellValueFactory(new PropertyValueFactory<File, String>("type"));
        fileSize.setCellValueFactory(new PropertyValueFactory<File, Long>("size"));
        foundFilesList.setItems(Main.filesOfUser);

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

        Platform.setImplicitExit(false);
        // настраиваем видимость анимации загрузки и подписи только во время поиска
        progressIndicator.visibleProperty().bind(Main.searchService.runningProperty());
        indicatorLabel.visibleProperty().bind(Main.searchService.runningProperty());

        Main.filesOfUser.addListener(new ListChangeListener<myFile>() {
            @Override
            public void onChanged(Change<? extends myFile> c) {
                while (c.next()){
                //    if (c.wasAdded())
                        setCounter(c.getAddedSize());

                }
                setCounter(Main.filesOfUser.size());
            }
        });

        progressIndicator.setProgress(-1d);
        Main.searchService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                progressIndicator.setProgress(0d);
                Main.filesOfUser = Main.searchService.getValue();
            }
        });
        Main.searchService.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Something gone wrong while searching for files by SID");
                System.out.println(event.getEventType().getName());
                System.out.println(event.getSource().getMessage());
            }
        });
        Main.searchService.start();
    }

    private void setCounter(long number) {
      Long currValue = Long.valueOf(foundFilesAmount.getText());
        foundFilesAmount.setText(String.valueOf(currValue + number));
//        foundFilesAmount.setText(String.valueOf(number));
    }
}
