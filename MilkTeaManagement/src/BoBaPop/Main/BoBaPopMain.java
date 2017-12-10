package BoBaPop.Main;

import BoBaPop.DA.ConnectToMySql;
import BoBaPop.Util.MyFunction;
import java.net.URL;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BoBaPopMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(new URL(MyFunction.LOGIN_URI));
        Scene scene = new Scene(root);
        stage.setTitle(MyFunction.LOGIN_TITLE);
        stage.getIcons().add(MyFunction.iconApp);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        ConnectToMySql.initialize();
        launch(args);
    }
}
