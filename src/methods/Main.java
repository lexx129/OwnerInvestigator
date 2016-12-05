package methods;

import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

public class Main extends Application {
    private Stage primaryStage;
    private SplitPane rootLayout;

    public static String singleFilePath = "";
    public static UserPrincipal singleFileOwner;



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

    public static void main(String[] args) {
        launch(args);
    }
}
