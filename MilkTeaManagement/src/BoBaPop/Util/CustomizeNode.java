package BoBaPop.Util;

import BoBaPop.Model.tables.records.TablesRecord;
import BoBaPop.Model.tables.records.VwBillDetailsRecord;
import com.jfoenix.controls.JFXButton;
import java.util.Map;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.jooq.Record;

public class CustomizeNode {

    public static final String GRAY_COLOR_50 = "rgba(153, 153, 153, 0.5)";
    public static final String TEAL_COLOR_50 = "rgba(0, 150, 136, 0.5)";
    public static final String WHITE_COLOR_20 = "rgba(0, 0, 0, 0.2)";

    public static JFXButton createMyButton(Map.Entry<Integer, Record> entry) {
        JFXButton button = new JFXButton(entry.getKey().toString());

        // styling cho button
        button.setStyle("-fx-background-radius: 0;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 18px;"
        );

        // set size
        //button.setPrefSize(widthCell, heightCell);
//        animation
//        Animation.playTranslateTransition(
//                button, Duration.millis(1000), Duration.ZERO,
//                -150 + random.nextInt(300), 0,
//                -150 + random.nextInt(300), 0);
        // set alignment
        button.setAlignment(Pos.TOP_LEFT); //nội dung trong button
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(button, VPos.BOTTOM);

        Record record = entry.getValue();
        //text và color 
        if (record instanceof VwBillDetailsRecord) { // là một VwBillDetailsRecord
            // show thông tin tóm tắt bill
            VwBillDetailsRecord billdetailsRecord = (VwBillDetailsRecord) record;

            StringBuilder summaryBill = new StringBuilder(billdetailsRecord.getTablename());
            summaryBill.append("\n").append(billdetailsRecord.getGrandtotal()).append("đ");
            summaryBill.append("\n").append((billdetailsRecord.getIspaid() == 1) ? "Đã trả" : "");
            button.setText(summaryBill.toString());

            // set màu cho button dựa vào trạng thái hiện tại
            StringBuilder backGroundColor = new StringBuilder(button.getStyle());
            backGroundColor.append("-fx-background-color: ");
            if (billdetailsRecord.getIsstaying() != 0) {
                backGroundColor.append(TEAL_COLOR_50);
            } else {
                backGroundColor.append(GRAY_COLOR_50);
            }
            backGroundColor.append(";");
            button.setStyle(backGroundColor.toString());

            // set AccessibleText =  BillID
            button.setAccessibleText(billdetailsRecord.getBillid().toString());
            // set AccessibleHelp =  TableID
            button.setAccessibleHelp(billdetailsRecord.getTableid().toString());

        } else if (record instanceof TablesRecord) { // là một TablesRecord
            TablesRecord tablesRecord = (TablesRecord) record;
            // show mỗi tên bàn lên
            button.setText(tablesRecord.getTablename());

            // set màu
            button.setStyle(button.getStyle() + "-fx-background-color: " + GRAY_COLOR_50 + ";");
            // set AccessibleHelp =  TableID
            button.setAccessibleHelp(tablesRecord.getTableid().toString());
        }
        return button;
    }

    public static Label createMyLabel(Map.Entry<Integer, Record> entry) {
        Label meta = new Label();

        // thêm thông tin nếu là VwBillDetailsRecord
        if (entry.getValue() instanceof VwBillDetailsRecord) {
          //  meta.setText(((VwBillDetailsRecord) entry.getValue()).getFullname());
        } else if (entry.getValue() instanceof TablesRecord) {
            meta.setText("Chưa có hóa đơn nào!");
        }
        // customize
        // meta.setPrefSize(widthCell, 0.0);
        meta.setWrapText(true);
        meta.setOpacity(0.0);
        StringBuilder styleLabel = new StringBuilder("-fx-background-radius: 0;");
        styleLabel.append("-fx-background-color:").append(WHITE_COLOR_20).append(";");
        styleLabel.append("-fx-text-fill: white").append(";");
        styleLabel.append("-fx-font-size: 18px").append(";");
        styleLabel.append("-fx-padding: 5px").append(";");
        meta.setStyle(styleLabel.toString());

        GridPane.setHalignment(meta, HPos.CENTER);
        GridPane.setValignment(meta, VPos.TOP);
        return meta;
    }
}
