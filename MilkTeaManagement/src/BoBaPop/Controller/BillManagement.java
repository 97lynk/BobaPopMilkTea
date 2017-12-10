package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import static BoBaPop.Model.Tables.*;
import BoBaPop.Model.tables.records.VwBillStatisticRecord;
import BoBaPop.Util.MessageBox;
import BoBaPop.Util.MyFunction;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.collectingAndThen;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.ListSelectionView;
import org.jooq.Field;
import tray.notification.NotificationType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;

public class BillManagement implements Initializable {

    @FXML
    private TableView<VwBillStatisticRecord> tableBill;

    private TableView<VwBillStatisticRecord> bindingData;
    //

    @FXML
    private JFXDatePicker fromDate;
    @FXML
    private JFXDatePicker toDate;
    @FXML
    private JFXTextField txtMaxTotal;
    @FXML
    private JFXTextField txtMinTotal;
    @FXML
    private JFXSlider sliderAge;
    @FXML
    private JFXCheckBox chkOrderDate;
    @FXML
    private JFXCheckBox chkTotal;
    @FXML
    private JFXCheckBox chkAge;
    @FXML
    private ListSelectionView<String> lsvSort;

    @FXML
    private JFXTextField txtTable;
    @FXML
    private JFXTextField txtID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Connect in " + this.getClass().getSimpleName());
        bindingData = new TableView<>();
        tableBill.itemsProperty().bind(bindingData.itemsProperty());

        // load cấu trúc cho bảng
        loadStructureTableViewBill();
        ObservableList<VwBillStatisticRecord> data = loadDataTableViewBill();
        bindingData.setItems(data);
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
        toDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBefore(fromDate.getValue())) {
                LocalDate localDate = fromDate.getValue();
                fromDate.setValue(newValue);
                toDate.setValue(localDate);
            }
        });

        txtMaxTotal.setText(data.stream()
                .mapToDouble(VwBillStatisticRecord::getGrandtotal)
                .max().getAsDouble() + "");
        txtMinTotal.setText(data.stream()
                .mapToDouble(VwBillStatisticRecord::getGrandtotal)
                .min().getAsDouble() + "");

        DoubleValidator doubleValidator1 = new DoubleValidator();
        doubleValidator1.setMessage("Number is required");
        txtMaxTotal.getValidators().add(doubleValidator1);

        DoubleValidator doubleValidator2 = new DoubleValidator();
        doubleValidator2.setMessage("Number is required");
        txtMinTotal.getValidators().add(doubleValidator2);

        txtMaxTotal.textProperty().addListener((observable, oldValue, newValue) -> {
            txtMaxTotal.validate();
        });
        txtMinTotal.textProperty().addListener((observable, oldValue, newValue) -> {
            txtMinTotal.validate();
        });

     
      
        

        lsvSort.setSourceItems(
                FXCollections.observableArrayList(VW_BILL_STATISTIC
                        .fieldStream().map(Field::getName)
                        .filter(fieldname -> !fieldname.toLowerCase().contains("id"))
                        .collect(Collectors.toList())));
        lsvSort.setSourceHeader(new Label("Các cột"));
        lsvSort.setTargetHeader(new Label("Sắp xếp theo"));

        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            search(null);
        };

        txtTable.textProperty().addListener(changeListener);
    }

    private void loadStructureTableViewBill() {
        // load structure cho bảng (các cột)
        VW_BILL_STATISTIC.fieldStream()
                .forEach(col -> {

                    // tạo tablecolumn với header là tên trường trong vw_billdetails
                    TableColumn column = new TableColumn(col.getName());
                    column.setCellValueFactory(new PropertyValueFactory(col.getName().toLowerCase()));
                    if (col.getName().contains("ID")) {
                        column.setVisible(false); // ẩn các cột ID
                    }

                    // thêm vào tableview
                    tableBill.getColumns().add(column);
                });

    }

    // load dữ liệu cho tableview Bill
    private ObservableList<VwBillStatisticRecord> loadDataTableViewBill() {

        ObservableList<VwBillStatisticRecord> data = null;
        // load data cho bảng
        try {

//            
//            data = FXCollections.observableArrayList(ConnectToMySql.context
//                    .fetch("SELECT * FROM vw_billstatistic").toArray(new VwBillStatisticRecord[0]));
            data = FXCollections.observableArrayList(ConnectToMySql.context
                    .selectFrom(VW_BILL_STATISTIC)
                    .fetchInto(VwBillStatisticRecord.class));
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
        return data;
    }

    public static ObservableList<VwBillStatisticRecord> filteringData(
            ObservableList<VwBillStatisticRecord> data,
            Predicate<VwBillStatisticRecord> predicate) {
        return data.stream()
                .filter(predicate)
                .collect(collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    public static ObservableList<VwBillStatisticRecord> sortingData(
            ObservableList<VwBillStatisticRecord> data,
            Comparator<VwBillStatisticRecord> comparator) {
        return data.stream()
                .sorted(comparator)
                .collect(collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    public static Function<VwBillStatisticRecord, String> makeFunction(String name) {
        switch (name.toLowerCase()) {
            case "ordertime":
                return (vwBillstatisticRecord) -> vwBillstatisticRecord.getOrdertime().toString();
            case "tablename":
                return VwBillStatisticRecord::getTablename;
            case "grandtotal":
                return (vwBillstatisticRecord) -> vwBillstatisticRecord.getGrandtotal().toString();
            case "sumdrink":
                return (vwBillstatisticRecord) -> vwBillstatisticRecord.getGrandtotal().toString();

        }
        return null;
    }

    @FXML
    private void filtering(ActionEvent event) {

        ObservableList<VwBillStatisticRecord> data = loadDataTableViewBill();
        if (data == null) {
            return;
        }

        if (chkOrderDate.isSelected()) {
            Timestamp f = Timestamp.valueOf(fromDate.getValue().atStartOfDay());
            Timestamp t = Timestamp.valueOf(toDate.getValue().atStartOfDay());
            Predicate<VwBillStatisticRecord> predicate = record -> {
                return (record.getOrdertime().after(f) && record.getOrdertime().before(t));
            };
            data = filteringData(data, predicate);
        }

        if (chkTotal.isSelected()) {
            try {
                double maxTotal = Double.parseDouble(txtMaxTotal.getText());
                double minTotal = Double.parseDouble(txtMinTotal.getText());
                Predicate<VwBillStatisticRecord> predicate = record -> {
                    return (minTotal <= record.getGrandtotal() && record.getGrandtotal() <= maxTotal);
                };
                data = filteringData(data, predicate);
            } catch (NumberFormatException formatException) {
                MessageBox.showAndWait(formatException.getClass().getSimpleName(),
                        formatException.getMessage(),
                        NotificationType.ERROR);
            }
        }
       
        bindingData.setItems(data);
    }

    @FXML
    private void sort(ActionEvent event) {

        ObservableList<VwBillStatisticRecord> data = loadDataTableViewBill();
        Comparator<VwBillStatisticRecord> comparator = null;
        System.out.println("Sort");
        for (String item : lsvSort.getTargetItems()) {
            System.out.println(item);
            Function<VwBillStatisticRecord, String> function = makeFunction(item);
            if (comparator == null) {
                comparator = Comparator.comparing(function);
            } else {
                comparator = comparator.thenComparing(Comparator.comparing(makeFunction(item)));
            }

        }
        if (comparator != null) {
            bindingData.setItems(sortingData(data, comparator));
        }
    }

    @FXML
    private void search(ActionEvent event) {
        ObservableList<VwBillStatisticRecord> data = loadDataTableViewBill();
       
        Predicate<VwBillStatisticRecord> predicateTable = (record -> {
            return record.getTablename().contains(txtTable.getText());
        });
        Predicate<VwBillStatisticRecord> predicateTable2 = (record -> {
            return record.getBillid().equals(txtID.getText());
        });
       
        bindingData.setItems(filteringData(  filteringData(data, predicateTable), predicateTable2));
    }

    @FXML
    private void exportToExcel(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Excel Files(*.xls)", "*.xls");
        chooser.getExtensionFilters().add(filter);
        // Show save dialog
        File file = chooser.showSaveDialog(((JFXButton) event.getSource()).getScene().getWindow());
        if (file != null) {
            saveXLSFile(file);

        }
    }


    private void saveXLSFile(File file) {
        try {
            //System.out.println("Clicked,waiting to export....");

            FileOutputStream fileOut;
            fileOut = new FileOutputStream(file);

            HSSFWorkbook workbook = new HSSFWorkbook();

            int numberOfCol = (int) VW_BILL_STATISTIC.fields().length;

            HSSFSheet workSheet = workbook.createSheet("Bills");
            workSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, numberOfCol + 2));
            HSSFCell captionCell = workSheet.createRow((short) 0).createCell(0);
            HSSFCellStyle captionStyle = workbook.createCellStyle();
            captionStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            captionCell.setCellValue("Bills statistic");
            captionCell.setCellStyle(captionStyle);
            HSSFRow header = workSheet.createRow((short) 1);
            for (int i = 1; i <= numberOfCol; i++) {
                String textColHeader = VW_BILL_STATISTIC.field(i - 1).getName();
                header.createCell(i).setCellValue(textColHeader);
            }

            HSSFRow dataRow = null;
            for (VwBillStatisticRecord row : bindingData.getItems()) {
                int index = bindingData.getItems().indexOf(row) + 2;
                dataRow = workSheet.createRow((short) index);

                dataRow.createCell(1).setCellValue(row.getBillid());
                dataRow.createCell(2).setCellValue(row.getTableid());
                dataRow.createCell(3).setCellValue(row.getOrdertime().toLocalDateTime().toString());
                dataRow.createCell(4).setCellValue(row.getTablename());
                dataRow.createCell(5).setCellValue(row.getGrandtotal());
                dataRow.createCell(6).setCellValue(row.getSumdrink().toString());

            }
            for (int i = 1; i <= numberOfCol; i++) {

                workSheet.autoSizeColumn(i);
            }
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();

            MessageBox.showAndDismiss("LƯU FILE EXCEL", "File excel của bạn đã được lưu", NotificationType.SUCCESS);
        } catch (IOException e) {
            MessageBox.showAndWait("LƯU FILE EXCEL",
                    "Có lỗi đã xảy ra. Không thể lưu file",
                    NotificationType.ERROR);
        }
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
