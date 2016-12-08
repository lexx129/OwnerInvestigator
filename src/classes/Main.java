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

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private Stage primaryStage;
    private SplitPane rootLayout;
    private AnchorPane resultsLayout;
    private AnchorPane domainSearchLayout;
    private Stage dialogStage;

    public static String singleFilePath = "";
    public static UserPrincipal singleFileOwner;
    public static ArrayList<String> ownerAttributes = new ArrayList<>();

    public static ObservableList<User> userList = FXCollections.observableArrayList();
    public static String username;
    public static String password;
    public static User chosenUser;

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
//        MainController mainController = loader.getController();

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
            loader.setLocation(getClass().getResource("/views/SidSearchResultLayout.fxml"));
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

    public static void main(String[] args) {
        launch(args);
    }
}
