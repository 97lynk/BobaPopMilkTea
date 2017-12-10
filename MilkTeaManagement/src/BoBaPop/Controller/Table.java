package BoBaPop.Controller;

import static BoBaPop.Model.Tables.*;
import BoBaPop.DA.ConnectToMySql;
import static BoBaPop.Model.Routines.ftIsStaying;
import BoBaPop.Model.routines.SpDeleteTable;
import BoBaPop.Model.tables.records.TablesRecord;
import BoBaPop.Model.tables.records.VwBillDetailsRecord;
import BoBaPop.Util.MessageBox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import tray.notification.NotificationType;

import javafx.scene.layout.AnchorPane;

public class Table implements Initializable {

    @FXML
    private JFXTextField txtTableName;
    @FXML
    private AnchorPane pane;

    private void mouseClickTable(MouseEvent event) {
        Label txtSummary = (Label) txtTableName.getScene().getRoot().lookup("#txtSummary");

        if (txtSummary == null) {
            return;
        }
        int tableID = Integer.parseInt(txtTableName.getAccessibleText());
        TablesRecord tablesRecord = ConnectToMySql.context.fetchAny(TABLES, TABLES.TABLEID.eq(tableID));
        VwBillDetailsRecord record = null;

        // kiểm tra bàn có đang trống
        Byte isStaying = ConnectToMySql.context
                .select(ftIsStaying(tablesRecord.getTableid()))
                .fetchAnyInto(Byte.class);
        if (isStaying == null) {
            txtSummary.setText("");
            return;
        } // bàn trống thì lấy hóa đơn cuối cùng
        StringBuilder builder = new StringBuilder();
        builder.append(tablesRecord.getTablename());
        builder.append("(").append("ID: ").append(tablesRecord.getTableid()).append(")\n");
        if (isStaying == 0) {

            builder.append("|_Không có khách\n");
            record = ConnectToMySql.context
                    .selectFrom(VW_BILL_DETAILS)
                    .where(VW_BILL_DETAILS.TABLEID.eq(tablesRecord.getTableid())
                            .and(VW_BILL_DETAILS.ISSTAYING.eq(Byte.valueOf("0"))))
                    .orderBy(VW_BILL_DETAILS.ORDERTIME.asc())
                    .fetchAny();
            if (record != null) {
                builder.append("|___Hóa đơn cuối cùng:\n");
            } else {
                builder.append("|_Chưa có hóa đơn nào\n");
            }

        } // bàn có khách thì lấy hóa đơn hiện tại
        else {
            builder.append("|_Đang có khách\n");
            builder.append("|___Hóa đơn hiện tại:\n");
            record = ConnectToMySql.context
                    .selectFrom(VW_BILL_DETAILS)
                    .where(VW_BILL_DETAILS.TABLEID.eq(tablesRecord.getTableid())
                            .and(VW_BILL_DETAILS.ISSTAYING.eq(Byte.valueOf("1"))))
                    .orderBy(VW_BILL_DETAILS.ORDERTIME.asc())
                    .fetchAny();
        }

        if (record != null) {
            builder.append("|_____Mã HĐ: ").append(record.getBillid()).append("\n");
            builder.append("|_____Tổng cộng: ").append(record.getGrandtotal()).append("\n");
        }

        txtSummary.setText(builder.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtTableName.setOnMouseClicked(this::mouseClickTable);
        pane.setOnMouseClicked(this::mouseClickTable);

    }

    @FXML
    private void clickEditTable(ActionEvent event) {
        try {
            int tableID = Integer.parseInt(txtTableName.getAccessibleText());
            TablesRecord tablesRecord = ConnectToMySql.context.fetchAny(TABLES, TABLES.TABLEID.eq(tableID));
            if (tablesRecord == null) {
                return;
            }
            tablesRecord.setTablename(txtTableName.getText());
            tablesRecord.update();

            MessageBox.showAndDismiss(
                    "SỬA BÀN", "Tên bàn đã được đổi",
                    NotificationType.SUCCESS);

            JFXButton btnReload = (JFXButton) txtTableName.getScene().getRoot().lookup("#btnReload");
            if (btnReload != null) {
                btnReload.fire();
            }

        } catch (NumberFormatException | DataAccessException daException) {
            MessageBox.showAndWait(
                    "Lỗi xảy ra " + daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);

        }
    }

    @FXML
    private void clickDeleteTable(ActionEvent event) {
        // hỏi xem có xóa ko?
        Optional<ButtonType> result = MessageBox.show(
                "Xác nhận thao tác", "Xóa " + txtTableName.getText(),
                "Bạn có muốn xóa bàn này?",
                Alert.AlertType.NONE, ButtonType.YES, ButtonType.NO);

        // chọn khác yes thì out
        if (result.get() != ButtonType.YES) {
            return;
        }

        try {
            int tableID = Integer.parseInt(txtTableName.getAccessibleText());
            SpDeleteTable deleteTable = new SpDeleteTable();
            deleteTable.setTableid(tableID);
            deleteTable.execute(ConnectToMySql.context.configuration());
            
            MessageBox.showAndDismiss(
                    "XÓA BÀN",
                    "Bàn đã bị xóa",
                    NotificationType.SUCCESS);

            JFXButton btnReload = (JFXButton) txtTableName.getScene().getRoot().lookup("#btnReload");
            if (btnReload != null) {
                btnReload.fire();
            }

        } catch (NumberFormatException | DataAccessException daException) {
            MessageBox.showAndWait(
                    "Lỗi xảy ra " + daException.getClass().getSimpleName(),
                    daException.getMessage(),
                    NotificationType.ERROR);

        }

    }
}
