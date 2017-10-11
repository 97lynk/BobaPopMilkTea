package BoBaPop.Util;

import BoBaPop.Model.tables.records.BillsRecord;
import BoBaPop.Model.tables.records.TablesRecord;
import BoBaPop.Model.tables.records.VwBilldetailsRecord;
import com.jfoenix.controls.JFXButton;
import java.security.SecureRandom;
import java.util.Map;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.jooq.Record;

public class CustomizeNode {

    public static JFXButton createMyButton(Map.Entry<Integer, Record> entry,
            double widthCell, double heightCell) {
        JFXButton button = new JFXButton(entry.getKey().toString());
        SecureRandom random = new SecureRandom();
        //styling cho button
        button.setStyle("-fx-background-radius: 0;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 18px;"
        );
        //size
        button.setPrefSize(widthCell, heightCell);
        //animation
        Animation.playTranslateTransition(
                button, Duration.millis(3000), Duration.ZERO,
                -150 + random.nextInt(300), 0,
                -150 + random.nextInt(300), 0);
        //alignment
        button.setAlignment(Pos.TOP_LEFT); //nội dung trong button
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(button, VPos.BOTTOM);

        Record record = entry.getValue();
        //text và color 
        if (record instanceof VwBilldetailsRecord) {
            VwBilldetailsRecord billdetailsRecord = (VwBilldetailsRecord) record;
            StringBuilder builder = new StringBuilder(billdetailsRecord.getTablename());
            builder.append("\n").append(billdetailsRecord.getGrandtotal() + "đ");
            builder.append("\n").append((billdetailsRecord.getIspaid() == 1) ? "Đã trả" : "");
            button.setText(builder.toString());
            if (billdetailsRecord.getIsstaying() != 0) {
                button.setStyle(button.getStyle() + "-fx-background-color: rgba(0, 150, 136, 0.5);");
            } else {
                button.setStyle(button.getStyle() + "-fx-background-color: rgba(153, 153, 153, 0.5);");
            }
            //System.out.println(billdetailsRecord.getTableid());
            button.setAccessibleText(billdetailsRecord.getBillid().toString());
            button.setAccessibleHelp(billdetailsRecord.getTableid().toString());
        } else if (record instanceof TablesRecord) {
            TablesRecord tablesRecord = (TablesRecord) record;
            button.setText(tablesRecord.getTablename());
            button.setStyle(button.getStyle() + "-fx-background-color: rgba(153, 153, 153, 0.5);");
           
            button.setAccessibleHelp(tablesRecord.getTableid().toString());
           /// System.out.println(tablesRecord.getTableid());
        }
        return button;
    }

    public static Label createMyLabel(Map.Entry<Integer, Record> entry,
            double widthCell, double heightCell) {
        Label meta = new Label("hello");
        meta.setPrefSize(widthCell, 0.0);
        meta.setWrapText(true);
        meta.setOpacity(0.0);
        meta.setStyle(
                "-fx-background-radius: 0;"
                + "-fx-background-color: rgba(0, 0, 0, 0.2);"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 18px;"
                + "-fx-padding: 5px;"
        );

        GridPane.setHalignment(meta, HPos.CENTER);
        GridPane.setValignment(meta, VPos.TOP);
        if (entry.getValue() instanceof VwBilldetailsRecord) {
            VwBilldetailsRecord record = (VwBilldetailsRecord) entry.getValue();
            meta.setText(record.getFullname());
        } else if (entry.getValue() instanceof TablesRecord) {
            meta.setText("Chưa có hóa đơn nào!");
        }
        return meta;
    }
}
