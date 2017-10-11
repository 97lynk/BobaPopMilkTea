package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import BoBaPop.Util.Animation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoginFXMLController implements Initializable {

    @FXML
    ImageView viewLoginImage;
    @FXML
    JFXTextField txtUser;
    @FXML
    JFXPasswordField txtPassword;
    @FXML
    JFXButton btnSignIn;
    @FXML
    private Label lblUser, lblPassword, lblStatus;
    @FXML
    private ImageView viewLogo;
    @FXML
    private AnchorPane paneLogin;
    @FXML
    private JFXButton btnClose;

    @FXML
    private void onClickButtonSignIn(ActionEvent event) {
        try (Connection conn = ConnectToMySql.createConnection(txtUser.getText(), txtPassword.getText())) {
            if (conn == null) {
                return;
            }
//            //set hình theo tên đăng nhập
//            viewLoginImage.setImage(
//                    new Image(getClass().getResource("/BoBaPop/images/" + txtUser.getText() + ".png").toString()));
            Parent parent = FXMLLoader.load(getClass().getResource("/BoBaPop/View/FXMLDocument.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(parent, -1, -1);
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);//xóa title bar
            stage.setTitle("BoBaPop Management");
            stage.setResizable(false);
            stage.show();

            //đóng Stage login
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.close();
        } catch (SQLException e) {
            String message = "The user or password is incorrect";
            if (txtUser.getText().trim().length() <= 0
                    && txtPassword.getText().trim().length() <= 0) {
                message = "You must type user and password.";
            } else {
                if (txtUser.getText().trim().length() <= 0) {
                    message = "You must type user";
                }
                if (txtPassword.getText().trim().length() <= 0) {
                    message = "You must type password";
                }
            }

            lblStatus.setText(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void onClickButtonClose(ActionEvent event) {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String image = getClass().getResource("/BoBaPop/images/bg.jpg").toExternalForm();
        paneLogin.setStyle("-fx-background-image: url('" + image + "');"
                + "-fx-background-size:cover;");
        //mờ đục logo
        Animation.playFadeTransition(viewLogo, 
                Duration.millis(3000.0), Duration.ZERO, 
                1.0, 0.0, 30, true);

        //xoay hình
        Animation.playRotateTransition(viewLoginImage, 
                Duration.millis(3000.0), Duration.ZERO, 
                360.0, 30, true);

        //chuyển động  2 textfield
        Animation.playTranslateTransition(txtUser, 
                Duration.millis(1500.0), Duration.ZERO, 
                300.0, 0.0, 0.0, 0.0);
        Animation.playTranslateTransition(txtPassword, 
                Duration.millis(1500.0), Duration.ZERO, 
                -300.0, 0.0, 0.0, 0.0);

        //event cho 2 textfield
        txtUser.focusedProperty().addListener((
                ObservableValue<? extends Boolean> arg0,
                Boolean oldPropertyValue,
                Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                lblUser.setOpacity(1.0);
                txtUser.setPromptText("");
            }
        });
        txtPassword.focusedProperty().addListener((
                ObservableValue<? extends Boolean> arg0,
                Boolean oldPropertyValue,
                Boolean newPropertyValue) -> {
            if (newPropertyValue) {
                lblPassword.setOpacity(1.0);
                txtPassword.setPromptText("");
            }
        });
    }

}
