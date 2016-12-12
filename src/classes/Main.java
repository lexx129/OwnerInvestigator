package classes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import methods.searchBySidService;

import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private SplitPane rootLayout;
    private AnchorPane resultsLayout;
    private AnchorPane domainSearchLayout;
    private AnchorPane selectDirLayout;
    private AnchorPane searchFilesBySidResultLayout;

    // переменные для информации о владельце
    public static String singleFilePath = "";
    public static UserPrincipal singleFileOwner;
    public static String dirPath = "";

    // список найденных в папке владельцев
    public static ArrayList<UserPrincipal> dirFileOwners = new ArrayList<>();
    // список атрибутов конкретного пользователя
    public static ArrayList<String> ownerAttributes = new ArrayList<>();

    // '1' - вывод результата иссл. 1 файла;
    // '2' - вывод результата иссл. папки
    public static int resultSwitch;

    // список добавленных SID
    public static ObservableList<User> userList = FXCollections.observableArrayList();
    // данные для поиска по домену
    public static String username;
    public static String password;
    // выбранный SID из списка сохраненных
    public static User chosenUser;

    // найденные файлы выбранного из списка владельца
    public static ObservableList<myFile> filesOfUser = FXCollections.observableArrayList();
    public static searchBySidService searchService;

    public List _listeners = new ArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        initRootLayout();
    }

    private void initRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/views/RootLayout.fxml"));
        rootLayout = loader.load();
//        mainController mainController = loader.getController();

//        mainController.setMain(this);
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("File owner investigator");
        primaryStage.show();
    }

    public void showResultsLayout(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/getOwnerLayout.fxml"));
            resultsLayout = loader.load();
            Stage resultStage = new Stage();
            resultStage.setTitle("Результат");
            resultStage.initModality(Modality.WINDOW_MODAL);
            resultStage.initOwner(primaryStage);
            // Show the scene containing the root layout.
            Scene scene = new Scene(resultsLayout);
            resultStage.setScene(scene);
//            resultStage.setX(rootLayout.getLayoutX() + 300);
//            resultStage.setY(rootLayout.getLayoutY() + 300);
            resultStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDomainSearchLayout(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/domainLoginLayout.fxml"));
        try {
            domainSearchLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage dsStage = new Stage();
        dsStage.setTitle("Поиск информации в контроллере домена");
        Scene scene = new Scene(domainSearchLayout);
        dsStage.setScene(scene);
        dsStage.show();
    }

    public void showFileSearcherLayout(String what) {
        FXMLLoader loader = new FXMLLoader();
        if (what.equals("selectDir")) {
            loader.setLocation(getClass().getResource("/views/selectDirLayout.fxml"));
            try {
                selectDirLayout = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage dirStage = new Stage();
            dirStage.setTitle("Поиск файлов указанного владельца");
            Scene dirScene = new Scene(selectDirLayout);
            dirStage.setScene(dirScene);
            dirStage.show();
        }
        else if (what.equals("foundFiles")){
            loader.setLocation(getClass().getResource("/views/foundFilesLayout.fxml"));
            try {
                searchFilesBySidResultLayout = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage resStage = new Stage();
            resStage.setTitle("Результаты поиска по SID");
            Scene resScene = new Scene(searchFilesBySidResultLayout);
            resStage.setScene(resScene);
            resStage.show();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}
