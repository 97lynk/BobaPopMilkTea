package BoBaPop.Main;

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
        Parent root = FXMLLoader.load(getClass().getResource("/BoBaPop/View/FXMLDocument.fxml"));
        Scene scene = new Scene(root, root.prefWidth(-1), root.prefHeight(-1));
//        String image = getClass().getResource("/BoBaPop/images/bg.jpg").toExternalForm();
//        root.setStyle("-fx-background-image: url('" + image + "');"
//                + "-fx-background-size:cover;");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
