package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import BoBaPop.Model.Tables;
import BoBaPop.Model.tables.records.UsersRecord;
import BoBaPop.Util.MessageBox;
import BoBaPop.Util.MyFunction;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import com.jfoenix.validation.RequiredFieldValidator;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.commons.io.IOUtils;
import org.jooq.exception.DataAccessException;
import tray.notification.NotificationType;

public class UserManagementController implements Initializable {

    @FXML
    private Label txtUserID;
    @FXML
    private ImageView imgAvatar;
    @FXML
    private JFXTextField txtUser;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXTextField txtFullName;
    @FXML
    private JFXTextField txtPhone;
    @FXML
    private JFXTextField txtPermisson;
    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXTextArea txtAddress;

    @FXML
    private CheckBox chkGender;

    @FXML
    private JFXPasswordField txtConfirm;
    @FXML
    private AnchorPane paneAccount;
    @FXML
    private AnchorPane paneCV;
    @FXML
    private AnchorPane paneButton;
    @FXML
    private AnchorPane paneButtonAccount;
    @FXML
    private Pane paneTool;

    //local variable
    private File file = null;
    private List<UsersRecord> users;
    private ListIterator<UsersRecord> it;

    // local variable
    private boolean addUser = false;
    private boolean validateAccount = false;
    private boolean changePass = false;

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        ValidationSupport validationSupport = new ValidationSupport();

        validationSupport.registerValidator(txtConfirm, false, this::checkMatch);

        RequiredFieldValidator validator1 = new RequiredFieldValidator();
        validator1.setMessage("User name is required");

        RequiredFieldValidator validator2 = new RequiredFieldValidator();
        validator2.setMessage("Password is required");

        RequiredFieldValidator validator3 = new RequiredFieldValidator();
        validator3.setMessage("Comfirm password is required");

        RequiredFieldValidator validator4 = new RequiredFieldValidator();
        validator3.setMessage("Permission is required");

        txtUser.getValidators().add(validator1);
        txtPassword.getValidators().add(validator2);
        txtConfirm.getValidators().add(validator3);
        txtPermisson.getValidators().add(validator4);

        txtUser.textProperty().addListener(this::validateRequired);
        txtPassword.textProperty().addListener(this::validateRequired);
        txtConfirm.textProperty().addListener(this::validateRequired);
        txtPermisson.textProperty().addListener(this::validateRequired);
        initializeScrollUser();
        loadInfomation(it.next());
    }

    private void initializeScrollUser() {
        users = ConnectToMySql.context.selectFrom(Tables.USERS)
                .fetchInto(UsersRecord.class);
        it = users.listIterator();
    }

    private void loadInfomation(UsersRecord user) {
        txtUserID.setText("ID: " + user.getId().toString());
        txtUserID.setAccessibleText(user.getId().toString());

        if (user.getAvatar() != null) {
            Image avatar = new Image(new ByteArrayInputStream(user.getAvatar()));
            imgAvatar.setImage(avatar);

        } else {
            imgAvatar.setImage(null);
        }

        txtUser.setText(user.getUsername());
        txtPassword.setText(user.getPassword());

        txtFullName.setText(user.getFullname());
        txtPermisson.setText(user.getPermisson());
        txtAge.setText(user.getAge().toString());
        txtPhone.setText(user.getPhonenumber());
        chkGender.setSelected(user.getGender() == 1);
        txtAddress.setText(user.getAddress());
    }

    @FXML
    private void clickPrevious(ActionEvent event) {
        if (it.hasPrevious()) {
            loadInfomation(it.previous());
        } else {
            it.next();
        }
    }

    @FXML
    private void clickNext(ActionEvent event) {
        if (it.hasNext()) {
            loadInfomation(it.next());
        } else {
            it.previous();

        }
    }

    @FXML
    private void openFileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Immage Files(*.png)", "*.png");
        chooser.getExtensionFilters().add(filter);
        // Show open dialog
        file = chooser.showOpenDialog(((JFXButton) event.getSource()).getScene().getWindow());
        if (file != null) {
            try {
                imgAvatar.setImage(new Image(new FileInputStream(file)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DrinkManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // thêm user mới
    @FXML
    private void clickAdd(ActionEvent event) {

        txtUserID.setText("New User");
        txtConfirm.setVisible(true);

        paneButton.setDisable(false);
        paneTool.setDisable(true);

        clear(event);
        addUser = true;
    }

    // sửa SV
    @FXML
    private void clickModify(ActionEvent event) {
        paneAccount.setDisable(true);
        paneCV.setDisable(false);
        paneButton.setDisable(false);

        paneTool.setDisable(true);
        addUser = false;
    }

    // lưu sửa CV hoặc thêm User
    @FXML
    private void clickSave(ActionEvent event) throws FileNotFoundException {
        try {
            if (addUser) {
                if (!txtUser.validate() || !txtPassword.validate()
                        || !txtConfirm.validate() || !txtPermisson.validate()) {
                    MessageBox.showAndDismiss(
                            "Account",
                            "User name, password, confirm password or permisson field is invalid",
                            NotificationType.WARNING,
                            Duration.millis(2000));
                    return;
                }
                if (!txtPassword.getText().equals(txtConfirm.getText())) {
                    MessageBox.showAndDismiss(
                            "Account",
                            "Password and confirm password aren't match",
                            NotificationType.WARNING,
                            Duration.millis(2000));
                    return;
                }

                UsersRecord usersRecord = ConnectToMySql.context.newRecord(Tables.USERS);
                if (file != null) {
                    usersRecord.setAvatar(IOUtils.toByteArray(new FileInputStream(file)));
                }
                usersRecord.setUsername(txtUser.getText());
                usersRecord.setPassword(txtUser.getText());
                usersRecord.setFullname(txtFullName.getText());
                usersRecord.setPermisson(txtPermisson.getText());
                usersRecord.setAge(Integer.parseInt(txtAge.getText()));
                usersRecord.setPhonenumber(txtPhone.getText());
                usersRecord.setGender((byte) ((chkGender.isSelected()) ? 1 : 0));
                usersRecord.setAddress(txtAddress.getText());
                usersRecord.store();

                MessageBox.showAndDismiss(
                        "Add user: " + usersRecord.getUsername(), "User is created",
                        NotificationType.SUCCESS);

            } else {
                if (!txtPermisson.validate()) {
                    MessageBox.showAndDismiss(
                            "CV",
                            "Permisson field is required",
                            NotificationType.WARNING,
                            Duration.millis(2000));
                    return;
                }
                int userId = Integer.parseInt(txtUserID.getAccessibleText());
                UsersRecord usersRecord = ConnectToMySql.context
                        .fetchAny(Tables.USERS, Tables.USERS.ID.eq(userId));
                if (usersRecord == null) {
                    MessageBox.showAndDismiss(
                            "User",
                            "Can't find this user",
                            NotificationType.WARNING,
                            Duration.millis(2000));
                    return;
                }
                if (file != null) {
                    usersRecord.setAvatar(IOUtils.toByteArray(new FileInputStream(file)));
                }
                usersRecord.setFullname(txtFullName.getText());
                usersRecord.setPermisson(txtPermisson.getText());
                usersRecord.setAge(Integer.parseInt(txtAge.getText()));
                usersRecord.setPhonenumber(txtPhone.getText());
                usersRecord.setGender((byte) ((chkGender.isSelected()) ? 1 : 0));
                usersRecord.setAddress(txtAddress.getText());
                usersRecord.update();

                MessageBox.showAndDismiss(
                        "Change CV user " + usersRecord.getUsername(), "User is updated",
                        NotificationType.SUCCESS);
            }
            initializeScrollUser();
            loadInfomation(it.next());
        } catch (DataAccessException | NumberFormatException | IOException e) {
            MessageBox.showAndWait(
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    NotificationType.ERROR);
        }
        file = null;
        clickCancel(event);

    }

    @FXML
    private void clickCancel(ActionEvent event) {
        paneButton.setDisable(true);
        paneAccount.setDisable(false);
        paneCV.setDisable(false);

        txtConfirm.setVisible(false);
        paneTool.setDisable(false);

        txtUserID.setAccessibleText(null);

    }

    // thay đổi password
    @FXML
    private void clickChangePass(ActionEvent event) {
        paneCV.setDisable(true);

        txtConfirm.setVisible(true);
        txtUser.setDisable(true);
        paneButtonAccount.setVisible(true);
        paneTool.setDisable(true);

    }

    // lưu thay đổi password
    @FXML
    private void clickSavePassword(ActionEvent event) {
        try {
            if (!txtPassword.validate()
                    || !txtConfirm.validate()) {
                MessageBox.showAndDismiss(
                        "Account",
                        "Password or confirm password field is invalid",
                        NotificationType.WARNING,
                        Duration.millis(2000));
                return;
            }
            if (!txtPassword.getText().equals(txtConfirm.getText())) {
                MessageBox.showAndDismiss(
                        "Account",
                        "Password and confirm password aren't match",
                        NotificationType.WARNING,
                        Duration.millis(2000));
                return;
            }
            int userId = Integer.parseInt(txtUserID.getAccessibleText());
            UsersRecord usersRecord = ConnectToMySql.context
                    .fetchAny(Tables.USERS, Tables.USERS.ID.eq(userId));
            if (usersRecord == null) {
                MessageBox.showAndDismiss(
                        "User",
                        "Can't find this user",
                        NotificationType.WARNING,
                        Duration.millis(2000));
                return;
            }
            if (file != null) {
                usersRecord.setAvatar(IOUtils.toByteArray(new FileInputStream(file)));
            }
            usersRecord.setPassword(txtPassword.getText());
            usersRecord.update();

            MessageBox.showAndDismiss(
                    "Account",
                    "Password is changed",
                    NotificationType.SUCCESS);

            initializeScrollUser();
            loadInfomation(it.next());
        } catch (DataAccessException | NumberFormatException | IOException e) {
            MessageBox.showAndWait(
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    NotificationType.ERROR);
        }
        clickCancelPassword(event);
    }

    @FXML
    private void clickCancelPassword(ActionEvent event) {
        paneCV.setDisable(false);
        txtConfirm.setVisible(false);
        paneButtonAccount.setVisible(false);
        paneTool.setDisable(false);
        txtUser.setDisable(false);

    }

    // delete User
    @FXML
    private void clickDelete(ActionEvent event) {
        try {
            int userId = Integer.parseInt(txtUserID.getAccessibleText());
            UsersRecord usersRecord = ConnectToMySql.context
                    .fetchAny(Tables.USERS, Tables.USERS.ID.eq(userId));
            if (usersRecord == null) {
                MessageBox.showAndDismiss(
                        "User",
                        "Can't find this user",
                        NotificationType.WARNING,
                        Duration.millis(2000));
                return;
            }
            usersRecord.delete();

            initializeScrollUser();
            loadInfomation(it.next());

            MessageBox.showAndDismiss(
                    "Account" + usersRecord.getUsername(), "User is deleted",
                    NotificationType.SUCCESS);
        } catch (DataAccessException | NumberFormatException e) {
            MessageBox.showAndWait(
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    NotificationType.ERROR);

        }
    }

    private void clear(ActionEvent event) {
        imgAvatar.setImage(null);

        txtUser.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");

        txtFullName.setText("");
        txtPermisson.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        chkGender.setSelected(false);
        txtAddress.setText("");
    }

    private ValidationResult checkMatch(Control control, String content) {
        return ValidationResult.fromMessageIf(control, "passwords don't match", Severity.ERROR, !txtPassword.getText().equals(txtConfirm.getText()));
    }

    private void validateRequired(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        txtUser.validate();
        txtPassword.validate();
        txtConfirm.validate();
        txtPermisson.validate();
    }

    @FXML
    private void clickClose(ActionEvent event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.setOnHidden(null);
        Platform.exit();
    }

    @FXML
    private void clickMinimize(ActionEvent event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.setIconified(true);
    }

    @FXML
    private void clickBack(ActionEvent event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        thisStage.hide();
    }

    @FXML
    private void clickLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(new URL(MyFunction.LOGIN_URI));
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(MyFunction.LOGIN_TITLE);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.show();
            Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            thisStage.setOnHidden(null);
            thisStage.close();
        } catch (IOException e) {

        }
    }
}
