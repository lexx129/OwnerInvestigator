package controllers;


import classes.SidFoundEvent;
import classes.newSidListener;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import classes.Main;
import classes.User;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class getSidCtrl implements Initializable {

    @FXML
    private TextArea results;
    @FXML
    private Button saveButton;
    @FXML
    DirectoryChooser dirChooser;
    private Stage dialogStage;
    private Main main;

    boolean isAcknowledged = false;

    private void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//
        Main.ownerAttributes = new ArrayList<>();
        setMain(new Main());
        if (!Main.singleFileOwner.getName().matches("S-.*")) {
            System.out.println("This user is registered");
            isAcknowledged = true;
            Main.ownerAttributes.add("Имя владельца: " + Main.singleFileOwner.getName());

        }
        Main.ownerAttributes.add("SID: " + getSid(Main.singleFileOwner));
        for (int i = 0; i < Main.ownerAttributes.size(); i++) {
            results.appendText(Main.ownerAttributes.get(i) + ";\n");
        }
//        results.appendText(String.valueOf(Main.singleFileOwner));
    }

    @FXML
    private void handleSave() throws IOException {
        String savepath;
        dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Сохранить результат");
        File file = dirChooser.showDialog(dialogStage);
        if (file != null) {
            savepath = file.toString() + "\\result.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(savepath));
            for (int i = 0; i < Main.ownerAttributes.size(); i++) {
                writer.write(Main.ownerAttributes.get(i) + "; \r\n");
            }
            writer.close();
            open(savepath);
        }
    }

    private void open(String filename) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
        }
        try {
            if (desktop != null) {
                desktop.open(new File(filename));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @FXML
    private void handleListAdd() {
        int size = Main.userList.size();
        HashMap<String, String> tempFiles = new HashMap<>();
        File tempFile = new File(Main.singleFilePath);
        tempFiles.put(tempFile.getAbsolutePath(), tempFile.getName());
        String name;
        String currSid = getSid(Main.singleFileOwner);
        User temp = null;
        Alert alert;
        if (isAcknowledged)
            temp = new User(size + 1, Main.singleFileOwner.getName(), currSid, tempFiles);
        else
            temp = new User(size + 1, "NONAME", currSid, tempFiles);
        if (!isInUserList(temp)) {
            Main.userList.add(size, temp);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION, "SID " + currSid + " уже есть в списке.");
            alert.setTitle("Оповещение");
            alert.setHeaderText("Внимание!");
            alert.showAndWait();
        }
        this.saveButton.getScene().getWindow().hide();
        //_fireSidFoundEvent();
    }

    private void _fireSidFoundEvent() {
        SidFoundEvent event = new SidFoundEvent(this, Main.singleFileOwner.toString());
        for (Object _listener : main._listeners) {
            ((newSidListener) _listener).newSidFound(event);
        }
    }

    public synchronized void addMoodListener(newSidListener l) {
        main._listeners.add(l);
    }

    public String getSid(UserPrincipal user) {
        Field f;
        String result = "";
        try {
//      достаем SID пользователя из private-поля экземпляра
            f = user.getClass().getDeclaredField("sidString");
            f.setAccessible(true);
            result = (String) f.get(user);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isInUserList(User candidate) {
        for (User anUserList : Main.userList) {
            if (anUserList.equals(candidate))
                return true;
        }
        return false;
    }
}
