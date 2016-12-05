package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import methods.Main;
import methods.User;

public class MainController {

    private Main main;

    @FXML
    private TableView<User> ownerTable;
    @FXML
    private TableColumn<User, Integer> columnNum;
    @FXML
    private TableColumn<User, String> userSid;



    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void initialize(){
        columnNum.setCellValueFactory(new PropertyValueFactory<User, Integer>("№"));
        userSid.setCellValueFactory(new PropertyValueFactory<User, String>("SID"));
    }



}
