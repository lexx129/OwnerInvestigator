package controllers;


import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.HashMap;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {

    @FXML
    private TextArea results;
    @FXML
    private Button saveButton;
    @FXML
    DirectoryChooser dirChooser;
    private Stage dialogStage;

    //    public void setMain(Main mainApp) {
//        this.mainApp = mainApp;
//    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < Main.ownerAttributes.size(); i++) {
            results.appendText(Main.ownerAttributes.get(i) + ";\n");
        }
        EventType eventType = new EventType("sidAdded");

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
        User temp = new User(size + 1, getSid(Main.singleFileOwner),tempFiles);
        if (!isInUserList(temp)) {
            Main.userList.add(size + 1, temp);

        }
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

    public boolean isInUserList(User candidate) {
        for (User anUserList : Main.userList) {
            if (anUserList.equals(candidate))
                return true;
        }
        return true;
    }
}
