package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import static BoBaPop.Model.tables.Users.*;
import BoBaPop.Model.tables.records.UsersRecord;
import BoBaPop.Util.Animation;
import BoBaPop.Util.MessageBox;
import BoBaPop.Util.MyFunction;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import java.io.IOException;
import java.net.URL;
import javafx.scene.control.Label;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jooq.DSLContext;
import tray.notification.NotificationType;

public class Login implements Initializable {

    @FXML
    ImageView viewLoginImage;
    @FXML
    JFXTextField txtUser;
    @FXML
    JFXPasswordField txtPassword;
    @FXML
    JFXButton btnSignIn;
    @FXML
    private Label lblStatus;
    @FXML
    private ImageView viewLogo;
    @FXML
    private AnchorPane paneLogin;

    @FXML
    private void onClickButtonSignIn(ActionEvent event) {
        System.out.println("You are click login...");
        txtUser.validate();
        txtPassword.validate();

        DSLContext context = ConnectToMySql.context;
        String userName = txtUser.getText();
        String passWord = txtPassword.getText();
        UsersRecord record = context.fetchAny(USERS, USERS.USERNAME.eq(userName)
                .and(USERS.PASSWORD.eq(passWord)));
        Parent parent = null;
        Stage stage = new Stage();
        if (record == null) {
            lblStatus.setText("Tài khoản hoặc mật khẩu không đúng");
            if (!txtUser.validate() && !txtPassword.validate()) {
                lblStatus.setText("");
            }
        } else {
            try {
                switch (record.getPermisson()) {
                    case MyFunction.ADMIN:
                        parent = FXMLLoader.load(new URL(MyFunction.MANAGER_URI));
                        stage.setMaximized(false);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setTitle(MyFunction.MANAGER_TITLE);
                        break;
                    case MyFunction.CASHIER:
                        parent = FXMLLoader.load(new URL(MyFunction.CASHIER_URI));
                        stage.setTitle(MyFunction.CASHIER_TITLE);
                        stage.setMaximized(true);
                        break;
                }
            } catch (IOException iOException) {
                MessageBox.showAndWait(iOException.getClass().getSimpleName(),
                        iOException.getMessage(),
                        NotificationType.ERROR);
            }

            Scene scene = new Scene(parent);
            stage.getIcons().add(MyFunction.iconApp);
            stage.setScene(scene);
            stage.show();

            //đóng Stage login
            onClickButtonClose(event);
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

        RequiredFieldValidator validator1 = new RequiredFieldValidator();
        validator1.setMessage("Nhập tài khoản");
        txtUser.getValidators().add(validator1);

        RequiredFieldValidator validator2 = new RequiredFieldValidator();
        validator2.setMessage("Nhập mật khẩu");
        txtPassword.getValidators().add(validator2);

        txtUser.textProperty().addListener(e -> {
            txtUser.validate();
        });
        txtPassword.textProperty().addListener(e -> {
            txtPassword.validate();
        });

    }

}
