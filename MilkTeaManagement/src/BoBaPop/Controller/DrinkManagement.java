/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import static BoBaPop.Model.Tables.*;
import BoBaPop.Model.tables.Drinks;
import BoBaPop.Model.tables.records.DrinksRecord;
import BoBaPop.Model.tables.records.DrinktypesRecord;
import BoBaPop.Util.MessageBox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.apache.commons.io.IOUtils;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import tray.notification.NotificationType;

public class DrinkManagement implements Initializable {

    @FXML
    private TableView<DrinksRecord> tableDrink;

    @FXML
    private JFXTextField txtDrinkName;
    @FXML
    private JFXTextField txtPrice;
    @FXML
    private JFXTextField txtUri;
    @FXML
    private JFXTextField txtDrinkTypeName;
    @FXML
    private ImageView imgDrink;
    @FXML
    private JFXComboBox<DrinktypesRecord> cbxDrinkType;
    @FXML
    private ListView<String> lvDrinkType;

    //local variable
    private boolean add = false; //false: sửa : >>
    @FXML
    private Button btnDir;

    private File file = null;
    @FXML
    private Label txtHint;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DoubleValidator doubleValidator = new DoubleValidator();
        doubleValidator.setMessage("Nhập số thập phân");
        txtPrice.setValidators(doubleValidator);
        txtPrice.textProperty().addListener((observable) -> {
            txtPrice.validate();
        });

        loadStructureTableViewDrink();
        loadDataTableViewDrink();
        loadDrinkType();
    }

    private void loadStructureTableViewDrink() {
        Drinks.DRINKS.fieldStream()
                .forEach(col -> {
                    // tạo tablecolumn với header là tên trường trong vw_billdetails
                    TableColumn column = new TableColumn(col.getName());
                    column.setCellValueFactory(new PropertyValueFactory(col.getName().toLowerCase()));
                    if (col.getName().contains("ID")) {
                        column.setVisible(false); // ẩn các cột ID
                    }
                    // thêm vào tableview
                    tableDrink.getColumns().add(column);
                });

    }

    private void loadDataTableViewDrink() {

        // load data cho bảng
        try {
            ObservableList<DrinksRecord> data
                    = FXCollections.observableArrayList(ConnectToMySql.context
                            .selectFrom(Drinks.DRINKS)
                            .fetchInto(DrinksRecord.class));
            tableDrink.setItems(data);
        } catch (MappingException mappingException) {
            MessageBox.show(
                    mappingException.getClass().getSimpleName(),
                    mappingException.getLocalizedMessage(),
                    mappingException.getMessage(),
                    Alert.AlertType.ERROR);
        } catch (DataAccessException dataAccessException) {
            MessageBox.show(
                    dataAccessException.getClass().getSimpleName(),
                    dataAccessException.getLocalizedMessage(),
                    dataAccessException.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void loadDrinkType() {
        try {
            // khởi tạo dữ liệu cho combobox
            ObservableList<DrinktypesRecord> drinktypesRecords
                    = FXCollections.observableArrayList(
                            ConnectToMySql.context.selectFrom(DRINKTYPES).fetch());
            cbxDrinkType.setConverter(new StringConverter<DrinktypesRecord>() {
                @Override
                public String toString(DrinktypesRecord object) {
                    return object.getDrinktypename();
                }

                @Override
                public DrinktypesRecord fromString(String string) {
                    return new DrinktypesRecord();
                }
            });
            cbxDrinkType.setItems(drinktypesRecords);
            lvDrinkType.setItems(FXCollections.observableArrayList(ConnectToMySql.context.selectFrom(DRINKTYPES).fetch(DRINKTYPES.DRINKTYPENAME)));
            lvDrinkType.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        } catch (DataAccessException daException) {
            MessageBox.showAndWait(
                    daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);
        }
    }

    @FXML
    private void clickTableDrink(MouseEvent event) {
        DrinksRecord drinksRecord = tableDrink.getSelectionModel().getSelectedItem();
        System.out.println(drinksRecord);
        if (drinksRecord.getImage() != null) {
            imgDrink.setImage(new Image(new ByteArrayInputStream(drinksRecord.getImage())));
        }

        txtDrinkName.setText(drinksRecord.getDrinkname());
        txtPrice.setText(drinksRecord.getUnitprice().toString());

        DrinktypesRecord drinktypesRecord = ConnectToMySql.context.fetchAny(DRINKTYPES, DRINKTYPES.DRINKTYPEID.eq(drinksRecord.getDrinktypeid()));
        cbxDrinkType.getSelectionModel().select(drinktypesRecord);
    }

    @FXML
    private void clickadd(ActionEvent event) {
        try {
            DrinksRecord drinksRecord = ConnectToMySql.context.newRecord(DRINKS);
            drinksRecord.setDrinkname(txtDrinkName.getText());
            drinksRecord.setDrinktypeid(cbxDrinkType.getValue().getDrinktypeid());
            drinksRecord.setUnitprice(Double.parseDouble(txtPrice.getText()));
            if (file != null) {

                drinksRecord.setImage(IOUtils.toByteArray(new FileInputStream(file)));

            }

            drinksRecord.store();
            MessageBox.showAndDismiss(
                    "THÊM THỨC UỐNG", "Thức uống " + drinksRecord.getDrinkname() + " đã được thêm",
                    NotificationType.SUCCESS);
            tableDrink.getItems().clear();
            loadDataTableViewDrink();

        } catch (Exception daException) {
            MessageBox.showAndWait(
                    daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);
        }
    }

    @FXML
    private void clickedit(ActionEvent event) {
        try {
            DrinksRecord selectedRecord = tableDrink.getSelectionModel().getSelectedItem();
            if (selectedRecord == null) {
                return;
            }
            DrinksRecord drinkRecord = ConnectToMySql.context.fetchAny(
                    DRINKS,
                    DRINKS.DRINKID.eq(selectedRecord.getDrinkid()));
            if (drinkRecord == null) {
                throw new DataAccessException("Can't find that drinktype");
            }
            if (file != null) {
                drinkRecord.setImage(IOUtils.toByteArray(new FileInputStream(file)));
            }

            drinkRecord.setDrinkname(txtDrinkTypeName.getText());
            drinkRecord.setDrinkname(txtDrinkName.getText());
            drinkRecord.setDrinktypeid(cbxDrinkType.getValue().getDrinktypeid());
            drinkRecord.setUnitprice(Double.parseDouble(txtPrice.getText()));
            drinkRecord.update();

            MessageBox.showAndDismiss(
                    "SỬA THỨC UỐNG", "Thông tin về thức uống đã được cập nhật",
                    NotificationType.SUCCESS);
            tableDrink.getItems().clear();
            loadDataTableViewDrink();
        } catch (Exception daException) {
            MessageBox.showAndWait(
                    daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);
        }
    }

    @FXML
    private void clickdel(ActionEvent event) {
        try {
            DrinksRecord selectedRecord = tableDrink.getSelectionModel().getSelectedItem();
            if (selectedRecord == null) {
                return;
            }
            DrinksRecord drinkRecord = ConnectToMySql.context.fetchAny(
                    DRINKS,
                    DRINKS.DRINKID.eq(selectedRecord.getDrinkid()));
            if (drinkRecord == null) {
                throw new DataAccessException("Can't find that drinktype");
            }
            drinkRecord.delete();
            MessageBox.showAndDismiss(
                    "XÓA THỨC UỐNG",
                    drinkRecord.getDrinkname() + " đã bị xóa",
                    NotificationType.SUCCESS);
            tableDrink.getItems().clear();
            loadDataTableViewDrink();
        } catch (DataAccessException daException) {
            MessageBox.showAndWait(
                    daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);
        }
    }

    @FXML
    private void clickItemListDrinkType(MouseEvent event) {
        String item = lvDrinkType.getSelectionModel().getSelectedItem();
        if (item != null) {
            txtDrinkTypeName.setText(item);
            txtDrinkTypeName.setAccessibleText(item);
        }
    }

    @FXML
    private void clickDir(ActionEvent event) {
        add = !add;
        if (add) {
            btnDir.setText("<<");
            txtHint.setText("Bạn đang thêm nhóm thức uống");
        } else {
            btnDir.setText(">>");
            txtHint.setText("Bạn đang sửa tên nhóm thức uống");
        }

    }

    @FXML
    private void clickSaveDrinkType(ActionEvent event) {
        try {
            if (add) {
                DrinktypesRecord drinktypesRecord = ConnectToMySql.context.newRecord(DRINKTYPES);
                drinktypesRecord.setDrinktypename(txtDrinkTypeName.getText());
                drinktypesRecord.store();
                MessageBox.showAndDismiss(
                        "THÊM NHÓM THỨC UỐNG",
                        "Nhóm" + drinktypesRecord.getDrinktypename() + " đã được thêm",
                        NotificationType.SUCCESS);

            } else {
                DrinktypesRecord drinktypesRecord = ConnectToMySql.context.fetchAny(
                        DRINKTYPES,
                        DRINKTYPES.DRINKTYPENAME.eq(txtDrinkTypeName.getAccessibleText()));
                if (drinktypesRecord == null) {
                    throw new DataAccessException("Không tìm thấy loại này");
                }
                drinktypesRecord.setDrinktypename(txtDrinkTypeName.getText());
                drinktypesRecord.update();
                MessageBox.showAndDismiss(
                        "SỬA NHÓM THỨC UỐNG",
                        "Nhóm thức uống đã được đổi tên",
                        NotificationType.SUCCESS);

            }
            txtDrinkTypeName.setAccessibleText(null);
            loadDrinkType();
        } catch (DataAccessException daException) {
            MessageBox.showAndWait(
                    daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);
        }
    }

    @FXML
    private void clickDeleteDrinkType(ActionEvent event) {
        try {

            DrinktypesRecord drinktypesRecord = ConnectToMySql.context.fetchAny(
                    DRINKTYPES,
                    DRINKTYPES.DRINKTYPENAME.eq(txtDrinkTypeName.getAccessibleText()));

            if (drinktypesRecord == null) {
                throw new DataAccessException("Không tìm thấy nhóm này");
            }
            drinktypesRecord.delete();
            MessageBox.showAndDismiss(
                    "XÓA NHÓM THỨC UỐNG",
                    "Nhóm thức uống đã bị xóa",
                    NotificationType.SUCCESS);
            txtDrinkTypeName.setAccessibleText(null);
            loadDrinkType();
        } catch (DataAccessException daException) {
            MessageBox.showAndWait(
                    daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);
        }
    }

    @FXML
    private void clickOpenImage(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Immage Files(*.png)", "*.png");
        chooser.getExtensionFilters().add(filter);
        // Show open dialog
        file = chooser.showOpenDialog(((JFXButton) event.getSource()).getScene().getWindow());
        if (file != null) {
            txtUri.setText(file.getAbsolutePath());
            try {
                imgDrink.setImage(new Image(new FileInputStream(file)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DrinkManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
