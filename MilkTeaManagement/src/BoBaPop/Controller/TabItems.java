/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import static BoBaPop.Model.Tables.DRINKTYPES;
import static BoBaPop.Model.Tables.*;
import static BoBaPop.Model.Tables.VW_DRINKS;
import BoBaPop.Model.routines.SpChargedGrandTotal;
import BoBaPop.Model.tables.records.DrinksRecord;
import BoBaPop.Model.tables.records.DrinktypesRecord;
import BoBaPop.Model.tables.records.ItemsRecord;
import BoBaPop.Model.tables.records.VwDrinksRecord;
import BoBaPop.Model.tables.records.VwItemsRecord;
import BoBaPop.Util.MessageBox;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Alert;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;

public class TabItems implements Initializable {

    private boolean addItem = false;
    /////////
    @FXML
    private ImageView imgDrink;
    @FXML
    private JFXComboBox<VwDrinksRecord> cbxDrink;
    @FXML
    private JFXComboBox<DrinktypesRecord> cbxDrinkType;
    @FXML
    private JFXTextField txtPrice;
    @FXML
    private JFXTextField txtAmount;
    @FXML
    private JFXTextField txtTotal;
    @FXML
    private TableView<VwItemsRecord> tableItem;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //imgDrink.fitHeightProperty().bind(((AnchorPane)imgDrink.getParent()).heightProperty().multiply(0.5));
        //imgDrink.fitWidthProperty().bind(((AnchorPane)imgDrink.getParent()).widthProperty());

        try {

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

            cbxDrinkType.getItems().addAll(drinktypesRecords);
        } catch (Exception ex) {
        }

        cbxDrinkType.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ObservableList<VwDrinksRecord> vwDrinksRecords
                        = FXCollections.observableArrayList(
                                ConnectToMySql.context.fetch(VW_DRINKS, VW_DRINKS.DRINKTYPEID.eq(newValue.getDrinktypeid())));
                cbxDrink.getItems().clear();
                cbxDrink.setConverter(new StringConverter<VwDrinksRecord>() {
                    @Override
                    public String toString(VwDrinksRecord object) {
                        return object.getDrinkname();
                    }

                    @Override
                    public VwDrinksRecord fromString(String string) {
                        return new VwDrinksRecord();
                    }
                });
                cbxDrink.getItems().addAll(vwDrinksRecords);

                cbxDrink.getSelectionModel().selectFirst();
            } catch (Exception e) {
            }
        });

        cbxDrink.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            DrinksRecord drinksRecord = ConnectToMySql.context.fetchAny(DRINKS, DRINKS.DRINKID.eq(newValue.getDrinkid()));
            if (drinksRecord == null) {
                return;
            }
            imgDrink.setImage(new Image(new ByteArrayInputStream(drinksRecord.getImage())));
            txtPrice.setText(drinksRecord.getUnitprice().toString());
        });

        addEventTextFieldAmount();
    }

    @FXML
    private void clickTableItems(MouseEvent event) {
        try {
            VwItemsRecord record = (VwItemsRecord) tableItem.getSelectionModel().getSelectedItem();
            System.out.println(record);
            cbxDrinkType.getSelectionModel().select(new DrinktypesRecord(record.getDrinktypeid(), record.getDrinktypename()));
            ObservableList<VwDrinksRecord> vwDrinksRecords
                    = FXCollections.observableArrayList(
                            ConnectToMySql.context.fetch(VW_DRINKS, VW_DRINKS.DRINKTYPEID.eq(record.getDrinktypeid())));

            cbxDrink.getItems().clear();
            cbxDrink.setConverter(new StringConverter<VwDrinksRecord>() {
                @Override
                public String toString(VwDrinksRecord object) {
                    return object.getDrinkname();
                }

                @Override
                public VwDrinksRecord fromString(String string) {
                    return new VwDrinksRecord();
                }
            });
            cbxDrink.getItems().addAll(vwDrinksRecords);
            cbxDrink.getSelectionModel().select(new VwDrinksRecord(
                    record.getDrinkid(),
                    record.getDrinkname(),
                    record.getDrinktypeid()));

            txtAmount.setText(record.getQuantity().toString());
            txtPrice.setText(record.getUnitprice().toString());
            txtTotal.setText(record.getTotal().toString());

            cbxDrink.setAccessibleText(record.getDrinkid().toString());
        } catch (Exception ex) {
            MessageBox.showException(
                    "Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

    private void addEventTextFieldAmount() {
        txtAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if (newValue.trim().length() > 2) {
                txtAmount.setText(newValue.substring(0, 2));
            }
            if (!newValue.matches("\\d*")) {
                txtAmount.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (txtAmount.getText().trim().length() > 0
                    && txtPrice.getText().trim().length() > 0) {
                double price = Double.parseDouble(txtPrice.getText());
                int amount = Integer.parseInt(txtAmount.getText());
                txtTotal.setText(price * amount + "");
            }
        });
    }

    @FXML
    private void clickAddItem(ActionEvent event) {
        addItem = true;
        btnSave.setDisable(false);
        btnCancel.setDisable(false);

    }

    @FXML
    private void clickEditItem(ActionEvent event) {
        addItem = false;
        btnSave.setDisable(false);
        btnCancel.setDisable(false);

    }

    @FXML
    private void clickRemoveItem(ActionEvent event) {
        try {
            int billID = Integer.parseInt(tableItem.getAccessibleText());
            int drinkID = Integer.parseInt(cbxDrink.getAccessibleText());
            ItemsRecord itemsRecord = ConnectToMySql.context.fetchAny(ITEMS, ITEMS.BILLID.eq(billID)
                    .and(ITEMS.DRINKID.eq(drinkID)));
            itemsRecord.delete();
            clickRefresh(event);
        } catch (NumberFormatException ex) {
            MessageBox.showException(
                    "Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clickSave(ActionEvent event) {
        try {
            int billID = Integer.parseInt(tableItem.getAccessibleText());
            if (addItem) {
                ItemsRecord itemsRecord = ConnectToMySql.context.newRecord(ITEMS);
                itemsRecord.setBillid(billID);
                if (cbxDrink.getValue().getDrinkid() == null) {
                    return;
                }
                itemsRecord.setDrinkid(cbxDrink.getValue().getDrinkid());
                itemsRecord.setQuantity(Integer.parseInt(txtAmount.getText()));
                itemsRecord.setTotal(Double.parseDouble(txtPrice.getText()) * Integer.parseInt(txtAmount.getText()));
                itemsRecord.store();
            } else {
                if (cbxDrink.getAccessibleText() == null) {
                    return;
                }
                int drinkID = Integer.parseInt(cbxDrink.getAccessibleText());
                ItemsRecord itemsRecord = ConnectToMySql.context.fetchAny(ITEMS, ITEMS.BILLID.eq(billID)
                        .and(ITEMS.DRINKID.eq(drinkID)));
                itemsRecord.setDrinkid(cbxDrink.getValue().getDrinkid());
                itemsRecord.setQuantity(Integer.parseInt(txtAmount.getText()));
                itemsRecord.setTotal(Double.parseDouble(txtPrice.getText()) * Integer.parseInt(txtAmount.getText()));
                itemsRecord.update();
            }
            clickRefresh(event);
        } catch (NumberFormatException ex) {
            MessageBox.showException(
                    "Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        } catch (DataAccessException ex) {
            MessageBox.show(
                    "Lỗi xảy ra", "Thức uống",
                    "Thức uống này đã có", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clickCancel(ActionEvent event) {
        btnSave.setDisable(true);
        btnCancel.setDisable(true);
    }

    @FXML
    private void clickRefresh(ActionEvent event) {
        try {

            int billID = Integer.parseInt(tableItem.getAccessibleText());
            SpChargedGrandTotal chargedGrandTotal = new SpChargedGrandTotal();
            chargedGrandTotal.setBillId(billID);
            chargedGrandTotal.execute(ConnectToMySql.context.configuration());

            Result<VwItemsRecord> result = ConnectToMySql.context
                    .selectFrom(VW_ITEMS)
                    .where(VW_ITEMS.BILLID.eq(billID))
                    .fetch();

            ObservableList table = FXCollections.observableArrayList(result);
            tableItem.setItems(table);

        } catch (Exception ex) {
            MessageBox.showException(
                    "Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

}
