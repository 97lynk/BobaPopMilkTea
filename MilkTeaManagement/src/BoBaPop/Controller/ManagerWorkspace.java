package BoBaPop.Controller;

import BoBaPop.Util.MessageBox;
import BoBaPop.Util.MyFunction;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class ManagerWorkspace implements Initializable {

    @FXML
    private VBox root;

    private Stage billStage = null;
    private Stage tableStage = null;
    private Stage drinkStage = null;
    private Stage userStage = null;
    private Stage statisticStage = null;
    private Stage aboutStage = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private Stage load(String url) {
        try {
            Parent parent = FXMLLoader.load(new URL(url));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setOnHidden(windowevent -> {
                show();
            });

            return stage;
        } catch (IOException e) {
            MessageBox.show("NumberFormatException",
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    Alert.AlertType.WARNING);
        }
        return null;
    }

    @FXML
    private void openTableManagement(ActionEvent event) {
        if (tableStage == null) {
            System.out.println("Fist load : BillManagement");
            tableStage = load(MyFunction.TABLE_URI);
            tableStage.setTitle(MyFunction.TABLE_TITLE);
            tableStage.setMaximized(true);
            tableStage.initStyle(StageStyle.UNDECORATED);
        }
        tableStage.getIcons().add(MyFunction.iconApp);
        tableStage.show();
        this.hide();
    }

    @FXML
    private void openBillsManagement(ActionEvent event) {
        if (billStage == null) {
            System.out.println("Fist load : BillManagement");
            billStage = load(MyFunction.BILL_URI);
            billStage.setTitle(MyFunction.BILL_TITLE);
            billStage.initStyle(StageStyle.UNDECORATED);
            billStage.setMaximized(true);
        }
        billStage.getIcons().add(MyFunction.iconApp);
        billStage.show();
        this.hide();

    }

    @FXML
    private void openDrinksManagement(ActionEvent event) {
        if (drinkStage == null) {
            System.out.println("Fist load : DrinkManagement");
            drinkStage = load(MyFunction.DRINK_URI);
            drinkStage.setTitle(MyFunction.DRINK_TITLE);
            drinkStage.setMaximized(true);
        }
        drinkStage.getIcons().add(MyFunction.iconApp);
        drinkStage.show();
        this.hide();
    }

    @FXML
    private void openUsersManagement(ActionEvent event) {
        if (userStage == null) {
            System.out.println("Fist load : UserManagement");
            userStage = load(MyFunction.USER_URI);
            userStage.setTitle(MyFunction.USER_TITLE);
            userStage.setResizable(false);
            userStage.initStyle(StageStyle.UNDECORATED);
            userStage.setMaximized(false);
        }
        userStage.getIcons().add(MyFunction.iconApp);
        userStage.show();
        this.hide();
    }

    @FXML
    private void openStatisticStage(ActionEvent event) {
        if (statisticStage == null) {
            System.out.println("Fist load : Statistic");
            statisticStage = load(MyFunction.STATISTIC_URI);
            statisticStage.setTitle(MyFunction.STATISTIC_TITLE);
            statisticStage.initStyle(StageStyle.UNDECORATED);
        }
        statisticStage.getIcons().add(MyFunction.iconApp);
        statisticStage.show();
        this.hide();
    }

    private void hide() {
        ((Stage) root.getScene().getWindow()).hide();
    }

    private void show() {
        try {
            Stage stage = (Stage) root.getScene().getWindow();
            if (stage != null) {
                stage.show();
            }
        } catch (Exception e) {
            System.out.println("dd");
        }

    }

    @FXML
    private void clickMinimize(ActionEvent event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.setIconified(true);
    }

    @FXML
    private void clickLogout(ActionEvent event) {
        try {
            Parent login = FXMLLoader.load(new URL(MyFunction.LOGIN_URI));
            Scene scene = new Scene(login);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.show();
            Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            thisStage.setOnHidden(null);
            thisStage.close();
        } catch (IOException e) {

        }
    }

    @FXML
    private void clickClose(ActionEvent event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.hide();
    }

    @FXML
    private void openAbout(ActionEvent event) {
        if (aboutStage == null) {
            System.out.println("Fist load : abour");
            aboutStage = load(MyFunction.ABOUT_URI);
            aboutStage.setTitle(MyFunction.ABOUT_TITLE);
            aboutStage.setResizable(false);
            aboutStage.setMaximized(false);

        }
        aboutStage.show();
        this.hide();
    }
}
