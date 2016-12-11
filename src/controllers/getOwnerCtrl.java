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

import javax.jws.soap.SOAPBinding;
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

// Контроллер формы с выбором файла и извлечения его SID
// Также отвечает за добавление его в список сохраненных

public class getOwnerCtrl implements Initializable {

    @FXML
    private TextArea results;
    @FXML
    private Button saveButton;
    @FXML
    DirectoryChooser dirChooser;
    private Stage dialogStage;
    private Main main;

    boolean isAcknowledged = false;
    // для единственного пользователя

    HashMap<String, Boolean> userIsAcknowledged;
    // для хранения нескольких пользователей

    private void setMain(Main main) {
        this.main = main;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMain(new Main());
        userIsAcknowledged = new HashMap<>();
        switch (Main.resultSwitch) {
            case 1:
                Main.ownerAttributes = new ArrayList<>();
                if (!Main.singleFileOwner.getName().matches("S-.*")) {
//                    System.out.println("This user is registered");
                    isAcknowledged = true;
                    Main.ownerAttributes.add("Имя владельца: " + Main.singleFileOwner.getName());
                }
                Main.ownerAttributes.add("SID: " + getSid(Main.singleFileOwner));
                for (int i = 0; i < Main.ownerAttributes.size(); i++) {
                    results.appendText(Main.ownerAttributes.get(i) + ";\n");
                }
                break;
            case 2:
                Iterator<UserPrincipal> iter = Main.dirFileOwners.iterator();
                UserPrincipal current;

                while (iter.hasNext()) {
                    current = iter.next();
                    if (!current.getName().matches("S-.*")) {
//                    System.out.println("This user is registered");
                        userIsAcknowledged.put(getSid(current), true);
//                        isAcknowledged = true;
                        //Main.ownerAttributes.add("Имя владельца: " + Main.singleFileOwner.getName());
                        results.appendText("Имя пользователя: " + current.getName() + "\n");
                    }
                    results.appendText("SID: " + getSid(current) + "\n-------------------\n");
                }
                break;
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
        int size;
        HashMap<String, String> tempFiles = new HashMap<>();
        User temp = null;
        Alert alert;
        switch (Main.resultSwitch) {
            case 1:
                size = Main.userList.size();
                File tempFile = new File(Main.singleFilePath);
                tempFiles.put(tempFile.getAbsolutePath(), tempFile.getName());
                String currSid = getSid(Main.singleFileOwner);
                if (isAcknowledged)
                    temp = new User(size + 1, Main.singleFileOwner.getName(), currSid, tempFiles);
                else
                    temp = new User(size + 1, "NONAME", currSid, tempFiles);
                if (!isInUserList(temp)) {
                    Main.userList.add(size, temp);
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Пользователь с SID '" + currSid + "' уже есть в списке.");
                    alert.setTitle("Оповещение");
                    alert.setHeaderText("Внимание!");
                    alert.showAndWait();
                }
                break;
            case 2:
                for (UserPrincipal currUser : Main.dirFileOwners) {
                    size = Main.userList.size();
                    currSid = getSid(currUser);
                    if (userIsAcknowledged.containsKey(getSid(currUser)))
                        temp = new User(size + 1, currUser.getName(), currSid, tempFiles);
                    else
                        temp = new User(size + 1, "NONAME", currSid, tempFiles);
                    if (!isInUserList(temp)) {
                        Main.userList.add(size, temp);
                    } else {
                        alert = new Alert(Alert.AlertType.INFORMATION, "Пользователь с SID '" + currSid + "' уже есть в списке.");
                        alert.setTitle("Оповещение");
                        alert.setHeaderText("Внимание!");
                        alert.showAndWait();
                    }
                }
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
        } catch (IllegalAccessException e) {
            System.out.println();
            e.printStackTrace();
        } catch (NoSuchFieldException exc) {
// SID встроенного администратора не определяется автоматически
            return "S-1-5-32-544";
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
