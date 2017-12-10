package BoBaPop.Controller;

// import model 
import BoBaPop.DA.ConnectToMySql;
import static BoBaPop.Model.Tables.*;

import BoBaPop.Model.tables.records.TablesRecord;
import BoBaPop.Util.MessageBox;
import BoBaPop.Util.ConfigureMyGrid;
import BoBaPop.Util.MyFunction;

// import javafx node, layout và control
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tray.notification.NotificationType;

public class TableManagement implements Initializable {

    @FXML
    private GridPane paneMap;
    @FXML
    private HBox hPane;

    private List<TablesRecord> listTables = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // binding kích thước của grid pane bàn với form gốc
        paneMap.prefHeightProperty().bind(hPane.heightProperty().subtract(10));
        paneMap.prefWidthProperty().bind(hPane.widthProperty().subtract(10));

        //thêm cột cho tableview bills
        clickReloadData(null);
    }

    //load data  gridpane
    private void loadDataDridMap() {
        listTables = ConnectToMySql.context.selectFrom(TABLES).fetchInto(TablesRecord.class);
    }

    //cấu trúc bàn lên gridpane
    private void loadStructureGridMap() {
        // xóa cũ hết trong pane
        paneMap.getChildren().clear();
        try {
            // dùng stream duyệt danh sách các bàn
            addCells(listTables);
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

    // customize và thêm các button vào gridpane
    private void addCells(List<TablesRecord> listTable) {
        // cấu hình, tính kích thước từ paneMap
        ConfigureMyGrid.initialize(paneMap, listTable.size());
        // duyệt và thêm cell
        listTable.stream().forEach(record -> {
            try {
                AnchorPane anchorPane = (AnchorPane) FXMLLoader.load(new URL(MyFunction.TABLE_CELL_URI));
                // tìm txtTableName để set TableID, TableName
                JFXTextField txtTableName = (JFXTextField) anchorPane.lookup("#txtTableName");
                txtTableName.setText(record.getTablename());
                txtTableName.setAccessibleText(record.getTableid().toString());

                // thêm vào gridPane
                int index = listTable.indexOf(record);
                if (record.getTableid() != 0) {
                    paneMap.add(anchorPane,
                            index % ConfigureMyGrid.grid.getCol(),
                            index / ConfigureMyGrid.grid.getCol());
                }
            } catch (IOException ex) {
                Logger.getLogger(TableManagement.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        // button + thêm bàn cuối cùng
        try {
            // load view
            AnchorPane addTablePane = FXMLLoader.load(getClass().getResource("/BoBaPop/View/Manager/AddNewTable.fxml"));

            JFXTextField txtTableName = (JFXTextField) addTablePane.lookup("#txtTableName");
            JFXButton btnClose = (JFXButton) addTablePane.lookup("#btnClose");
            txtTableName.setOnAction(event -> {
                try {
                    TablesRecord newTable = ConnectToMySql.context.newRecord(TABLES);
                    newTable.setTablename(txtTableName.getText());
                    newTable.store();
                    clickReloadData(null);
                    btnClose.fire();

                    MessageBox.showAndDismiss(
                            "THÊM BÀN",
                            "Bàn đã được thêm",
                            NotificationType.SUCCESS);
                } catch (NumberFormatException | DataAccessException daException) {
                    MessageBox.showAndWait(
                            "Lỗi xảy ra " + daException.getClass().getSimpleName(),
                            daException.getMessage(),
                            NotificationType.ERROR);
                }
            });

            // thêm vào cell cuối
            paneMap.add(addTablePane,
                    listTable.size() % ConfigureMyGrid.grid.getCol(),
                    listTable.size() / ConfigureMyGrid.grid.getCol());
        } catch (IOException iOException) {
            MessageBox.show(
                    "Lỗi xảy ra",
                    iOException.getClass().getSimpleName(),
                    iOException.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    public void clickReloadData(ActionEvent event) {
        loadDataDridMap();
        loadStructureGridMap();
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
