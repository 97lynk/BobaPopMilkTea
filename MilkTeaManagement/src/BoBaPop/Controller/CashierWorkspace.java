package BoBaPop.Controller;

// import model 
import static BoBaPop.Model.Routines.ftIsStaying;
import static BoBaPop.Model.Tables.*;
import BoBaPop.DA.ConnectToMySql;
import BoBaPop.Model.routines.SpDeleteBill;
import BoBaPop.Model.routines.SpPaying;
import BoBaPop.Model.tables.records.BillsRecord;
import BoBaPop.Model.tables.records.TablesRecord;
import BoBaPop.Model.tables.records.VwBillDetailsRecord;
import BoBaPop.Model.tables.records.VwItemsRecord;
import BoBaPop.Model.tables.records.VwTodayBillsRecord;
import BoBaPop.Util.Animation;
import BoBaPop.Util.CustomizeNode;
import BoBaPop.Util.MessageBox;
import BoBaPop.Util.ConfigureMyGrid;
import BoBaPop.Util.MyFunction;

// import javafx node, layout và control
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.*;
import java.io.FileInputStream;
import javafx.event.ActionEvent;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.SplitPane;

import javafx.scene.layout.GridPane;
import org.jooq.Result;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.util.StringConverter;
import org.jooq.Record;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JRException;
import org.jooq.exception.DataAccessException;
import tray.notification.NotificationType;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.jooq.Table;

public class CashierWorkspace implements Initializable {

    @FXML
    private VBox rootPane;
//    private AnchorPane rootPane;
    private JFXDrawer navBar;

    //tab các bàn 
    @FXML
    private AnchorPane paneBackGround;
    @FXML
    private JFXButton btnPay;
    @FXML
    private JFXButton btnModify;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnAdd;

    @FXML
    private GridPane paneMap;
    @FXML
    private Label lblBillID;
    @FXML
    private JFXDatePicker dpkDateOrder;
    @FXML
    private JFXTimePicker tpkTimeOrder;
    @FXML
    private JFXComboBox<TablesRecord> cbxTable;
    @FXML
    private Label txtValidTable;
    @FXML
    private JFXCheckBox chkPaid;
    @FXML
    private JFXCheckBox chkStaying;
    @FXML
    private JFXButton btnSave;

    //tab lịch sử các bill
    @FXML
    private TableView<VwTodayBillsRecord> tableBill;

    //tab list items
    private TableView tableItem;

    @FXML
    private AnchorPane listItemsPane;

    private VBox rightListItemsPane;

    //
    private Integer billID = null;
    private boolean isAdd = false;
    @FXML
    private Tab itemsTab;
    @FXML
    private Tab tableTab;

    // event khi thay đổi combobox table 
    private void changeListenerComboboxTable(ObservableValue<? extends TablesRecord> observable, TablesRecord oldValue, TablesRecord newValue) {
        btnPay.setDisable(false);
        try {
            txtValidTable.setText("");  // xóa cảnh báo
            // lấy tableID mới dựa vào sự chọn lựa combobox
            int selectedTableID = ((TablesRecord) newValue).getTableid();

            if (billID != null) {

                // lấy tableID dựa vào billID
                int tableID = ConnectToMySql.context
                        .selectFrom(BILLS)
                        .where(BILLS.BILLID.eq(billID))
                        .fetchAny(BILLS.TABLEID);

                // nếu 2 tableID bằng nhau => chọn lại bàn hiện tại
                if (tableID == selectedTableID) {
                    System.out.println("return at line: " + 152);
                    return;
                }

            }

            // nếu 2 TableID khác nhau => chọn bàn mới
            /// kiểm tra xem bàn mới có trống
            Byte isStayingNewTable = ConnectToMySql.context
                    .select(ftIsStaying(selectedTableID))
                    .fetchAnyInto(Byte.class);

            if (isStayingNewTable == null) {
                return;
            }
            if (isStayingNewTable == 1) {
                txtValidTable.setText("Có người ngồi rồi"); // bật cảnh báo
                btnSave.setDisable(true);
            } else {
                txtValidTable.setText("");  // xóa cảnh báo
                btnSave.setDisable(false);
            }

        } catch (NumberFormatException | DataAccessException ex) {
            MessageBox.showException("Exception",
                    ex.getClass().getSimpleName(),
                    new javafx.scene.control.TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

    // action event khi click các button trên map
    private void clickCells(ActionEvent e) {
        btnAdd.setDisable(false);
        btnPay.setDisable(false);
        btnModify.setDisable(false);
        btnDelete.setDisable(false);

        //  System.out.println("You clicked button...");
        //  System.out.println("... and data:");
        // lấy button đã được click 
        JFXButton resourse = (JFXButton) e.getSource();
        // xóa thông tin
        clearInfoBill();
        try {
            billID = null;
            // lấy BillID dựa vào AccessibleText
            if (resourse.getAccessibleText() != null
                    && resourse.getAccessibleText().trim().length() > 0) {
                billID = Integer.parseInt(resourse.getAccessibleText());
                VwBillDetailsRecord billdetailsRecord
                        = ConnectToMySql.context.fetchAny(VW_BILL_DETAILS, VW_BILL_DETAILS.BILLID.eq(billID));
                //   System.out.println("Bill ID : " + billID);
                if (billdetailsRecord != null) {
                    // hiện thông tin lên các textfield, combobox,...
                    showInfoBill(billdetailsRecord);
                }
            }

            // select lại combobox dựa vào AccessibleHelp
            if (resourse.getAccessibleHelp() != null
                    && resourse.getAccessibleHelp().trim().length() > 0) {
                int tableId = Integer.parseInt(resourse.getAccessibleHelp());
                TablesRecord tablesRecord = ConnectToMySql.context.fetchAny(TABLES, TABLES.TABLEID.eq(tableId));

                //  System.out.println("TableID ID : " + tableId);
                if (tablesRecord != null) {
                    cbxTable.getSelectionModel().select(tablesRecord);

                    // System.out.println(tablesRecord);
                }

            }
        } catch (NumberFormatException ex) {
            MessageBox.showException("Exception",
                    ex.getClass().getSimpleName(),
                    new javafx.scene.control.TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
        btnPay.setDisable(billID == null);
    }

    @FXML
    private AnchorPane paneInfoBill;
    @FXML
    private JFXTabPane myTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        SplitPane sp = (SplitPane) listItemsPane.lookup("#sliptPane");
        rightListItemsPane = (VBox) sp.getItems().get(1);
        System.out.println(sp);
        // tìm table view item trong tab 2
        tableItem = (TableView) rightListItemsPane.lookup("#tableItem");

        // cấu hình combobox table
        //// hiển thị tên bàn
        cbxTable.setConverter(new StringConverter<TablesRecord>() {
            @Override
            public String toString(TablesRecord object) {
                return object.getTablename();
            }

            @Override
            public TablesRecord fromString(String string) {
                return new TablesRecord();
            }
        });
        //// thêm dữ liệu vào combobox
        cbxTable.getItems().addAll(FXCollections.observableArrayList(
                ConnectToMySql.context.selectFrom(TABLES).fetch()));
        //// event khi chọn item trong combobox
        cbxTable.valueProperty().addListener(this::changeListenerComboboxTable);

        // binding kích thước của grid pane bàn với form gốc
        paneMap.prefHeightProperty().bind(rootPane.heightProperty().subtract(150));
        paneMap.prefWidthProperty().bind(rootPane.widthProperty().subtract(400));

        // event khi thay đổi kích thước của form gốc
        rootPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            clickReloadData(null);
        });
        rootPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            clickReloadData(null);
        });

        // set background image cho paneBackground
        String urlImage = getClass().getResource("/BoBaPop/images/bg_dridpane.jpg").toExternalForm();
        paneBackGround.setStyle("-fx-background-image: url('" + urlImage + "');"
                + "-fx-background-size:cover;");

        myTab.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
                    if (newValue.equals(itemsTab)) {
                        tableBill.getItems().clear();
                        if (lblBillID.getAccessibleText() != null
                                && lblBillID.getAccessibleText().trim().length() > 0) {
                            billID = Integer.parseInt(lblBillID.getAccessibleText());
                            VwBillDetailsRecord billdetailsRecord
                                    = ConnectToMySql.context.fetchAny(VW_BILL_DETAILS, VW_BILL_DETAILS.BILLID.eq(billID));
                            loadTableViewItem(billdetailsRecord);
                        }
                    }
                    if (newValue.equals(tableTab)) {
                        clickReloadData(null);
                    }
                });

        //thêm cột cho tableview bills
        loadGridMap();
        loadStructureTableViewBill();
        loadDataTableViewBill();

        // load cấu trúc cho bảng
    }

    private void loadStructureTableViewBill() {
        // load structure cho bảng (các cột)
        VW_TODAY_BILLS.fieldStream()
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
    private void loadDataTableViewBill() {
        tableBill.getItems().clear();
        // load data cho bảng
        try {
            ObservableList<VwTodayBillsRecord> data
                    = FXCollections.observableArrayList(ConnectToMySql.context
                            .selectFrom(VW_TODAY_BILLS)
                            .fetchInto(VwTodayBillsRecord.class));
            tableBill.setItems(data);
        } catch (DataAccessException ex) {
            MessageBox.showException("Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new javafx.scene.control.TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

    //load dữ liệu, trạng thái bàn lên gridpane
    private void loadGridMap() {
        // xóa cũ hết trong pane
        paneMap.getChildren().clear();
        try {
            // TreeMap (Key: tableID, Value: Vw_BilldetailsRecord | TableRecord)
            Map<Integer, Record> listData = new TreeMap<>();
            // dùng stream duyệt danh sách các bàn
            ConnectToMySql.context.selectFrom(TABLES).where(TABLES.TABLEID.notEqual(0)).fetchInto(TablesRecord.class).stream()
                    .forEach(tablesRecord -> {
                        Record record = null;
                        // kiểm tra bàn có đang trống
                        Byte isStaying = ConnectToMySql.context
                                .select(ftIsStaying(tablesRecord.getTableid()))
                                .fetchAnyInto(Byte.class);
                        if (isStaying == null) {
                            MessageBox.show(
                                    "Exception",
                                    "Warning",
                                    "\"isStaying\" variable is null!",
                                    Alert.AlertType.WARNING);
                            return;
                        }
                        // bàn trống thì lấy hóa đơn cuối cùng
                        if (isStaying == 0) {
                            record = ConnectToMySql.context
                                    .selectFrom(VW_BILL_DETAILS)
                                    .where(VW_BILL_DETAILS.TABLEID.eq(tablesRecord.getTableid())
                                            .and(VW_BILL_DETAILS.ISSTAYING.eq(Byte.valueOf("0"))))
                                    .orderBy(VW_BILL_DETAILS.ORDERTIME.asc())
                                    .fetchAny();

                        } // bàn có khách thì lấy hóa đơn hiện tại
                        else {
                            record = ConnectToMySql.context
                                    .selectFrom(VW_BILL_DETAILS)
                                    .where(VW_BILL_DETAILS.TABLEID.eq(tablesRecord.getTableid())
                                            .and(VW_BILL_DETAILS.ISSTAYING.eq(Byte.valueOf("1"))))
                                    .orderBy(VW_BILL_DETAILS.ORDERTIME.asc())
                                    .fetchAny();
                        }
                        // bàn chưa có hóa đơn nào từ lúc setup thì lấy TableRecord đó
                        if (record == null) {
                            record = tablesRecord;
                        }
                        // thêm vào map
                        listData.put(tablesRecord.getTableid(), record);

                    });
            addCells(listData);
        } catch (DataAccessException ex) {
            MessageBox.showException("Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new javafx.scene.control.TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

    // customize và thêm các button vào gridpane
    public void addCells(Map<Integer, Record> listTable) {
        // cấu hình, tính kích thước từ paneMap
        ConfigureMyGrid.initialize(paneMap, listTable.size());
        // duyệt và thêm cell
        listTable.entrySet().forEach(entry -> {
            Integer tableID = entry.getKey();
            Record record = entry.getValue();
            // button nào phía trên trong 1 cell
            JFXButton button = CustomizeNode.createMyButton(entry);
            button.prefWidthProperty().bind(paneMap.prefWidthProperty().divide(ConfigureMyGrid.grid.getCol()));
            button.prefHeightProperty().bind(paneMap.prefHeightProperty().divide(ConfigureMyGrid.grid.getRow()));

            // label thông tin phụ nằm dưới button
            Label another = CustomizeNode.createMyLabel(entry);
            another.prefWidthProperty().bind(paneMap.prefWidthProperty().divide(ConfigureMyGrid.grid.getCol()));
            another.setPrefHeight(0.0);

            // event cho button 
            //// hover mouse vào
            button.setOnMouseEntered(event -> {
                another.setOpacity(1.0);

                //animation mờ button đi
                Animation.playFadeTransition(button,
                        Duration.millis(200), Duration.ZERO,
                        1.0, 0.0);
                //animaiton drop down cho another label
                Timeline timeline = new Timeline();
                KeyFrame keyFrame;
                keyFrame = new KeyFrame(Duration.ZERO,
                        new KeyValue(another.prefHeightProperty(), 0));
                timeline.getKeyFrames().add(keyFrame);
                keyFrame = new KeyFrame(new Duration(300),
                        new KeyValue(another.prefHeightProperty(), button.getPrefHeight(), Interpolator.EASE_IN));
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            });
            /// mouse rời đi
            button.setOnMouseExited(event -> {
                //ẩn label 
                //another.setOpacity(0.0);
                Animation.playFadeTransition(another,
                        Duration.millis(200), Duration.millis(500),
                        1.0, 0.0);
                //hiện lại button
                Animation.playFadeTransition(button,
                        Duration.millis(200), Duration.millis(500),
                        0.0, 1.0);

            });

            // click button
            button.setOnAction(this::clickCells);

            // thêm 2 node vào cell
            int index = listTable.keySet().stream().collect(Collectors.toList()).indexOf(tableID);
            paneMap.add(another,
                    index % ConfigureMyGrid.grid.getCol(),
                    index / ConfigureMyGrid.grid.getCol());

            paneMap.add(button,
                    index % ConfigureMyGrid.grid.getCol(),
                    index / ConfigureMyGrid.grid.getCol());

        });
    }

    // đưa dữ liệu lên các textfield, combobox...
    private void showInfoBill(VwBillDetailsRecord record) {
        clearInfoBill();
        if (record != null) {
            lblBillID.setText("Bill ID is " + record.getBillid());
            lblBillID.setAccessibleText(record.getBillid().toString());
            tpkTimeOrder.setValue(record.getOrdertime().toLocalDateTime().toLocalTime());
            dpkDateOrder.setValue(record.getOrdertime().toLocalDateTime().toLocalDate());
            chkStaying.setSelected(record.getIsstaying() == 1);
            chkPaid.setSelected(record.getIspaid() == 1);
            cbxTable.getSelectionModel().select(new TablesRecord(record.getTableid(), record.getTablename()));
        }
    }

    // xóa dữ liệu lên các textfield, combobox...
    private void clearInfoBill() {
        tpkTimeOrder.setValue(null);
        dpkDateOrder.setValue(null);

        chkStaying.setSelected(false);
        chkPaid.setSelected(false);
        lblBillID.setText("");
        lblBillID.setAccessibleText(null);
        txtValidTable.setText("");
    }

    //load dữ liệu cho tableview Item
    private void loadTableViewItem(VwBillDetailsRecord billdetailsRecord) {
        tableItem.setAccessibleText(null);
        tableItem.getItems().clear();
        tableItem.getColumns().clear();
        if (billdetailsRecord == null) {
            return;
        }

        tableItem.setAccessibleText(billdetailsRecord.getBillid().toString());
        try {
            Result<VwItemsRecord> result = ConnectToMySql.context
                    .fetch(VW_ITEMS, VW_ITEMS.BILLID.eq(billdetailsRecord.getBillid()));
            // khởi tạo structure cho tableview item
            ObservableList data = FXCollections.observableArrayList(result);
            Arrays.stream(result.fields()).forEach(f -> {
                TableColumn column = new TableColumn(f.getName());
                column.setCellValueFactory(new PropertyValueFactory(f.getName().toLowerCase()));
                // ẩn các cột id
                if (f.getName().toLowerCase().contains("id")) {
                    column.setVisible(false);
                }
                tableItem.getColumns().add(column);
            });
            // thêm data vào
            tableItem.setItems(data);

        } catch (DataAccessException ex) {
            MessageBox.showException("Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new javafx.scene.control.TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clickPayButton(ActionEvent event) {

        try {

            // Fields for report
            if (billID == null) {
                return;
            }
//            SpChargedGrandTotal chargedGrandTotal = new SpChargedGrandTotal();
//            chargedGrandTotal.setBillId(billID);
//            chargedGrandTotal.execute(ConnectToMySql.context.configuration());
            SpPaying paying = new SpPaying();
            paying.setBillId(billID);
            paying.execute(ConnectToMySql.context.configuration());

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("BillID", billID);

            FileInputStream inputStream = new FileInputStream(MyFunction.BILL_JASPER);
            JasperPrint print = JasperFillManager.fillReport(inputStream, parameters, ConnectToMySql.createConnection());
            JasperViewer.viewReport(print, false);
            
            clickReloadData(event);
        } catch (ClassNotFoundException | SQLException | JRException | IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void clickAddButton(ActionEvent event) {
        clickModifyButton(event);
        isAdd = true;
        billID = null;
        clearInfoBill();

        dpkDateOrder.setValue(LocalDate.now());
        tpkTimeOrder.setValue(LocalTime.now());
        chkStaying.setSelected(true);
        int i = cbxTable.getSelectionModel().getSelectedIndex();
        cbxTable.getSelectionModel().selectFirst();
        cbxTable.getSelectionModel().selectLast();
        cbxTable.getSelectionModel().select(i);
    }

    @FXML
    private void clickModifyButton(ActionEvent event) {
        isAdd = false;
        paneBackGround.setDisable(true);
        paneInfoBill.setDisable(false);
    }

    @FXML
    private void clickDeleteButton(ActionEvent event) throws IOException {

        // hỏi xem có xóa ko?
        Optional<ButtonType> result = MessageBox.show(
                "Xác nhận thao tác", "Xóa hóa đơn ID: " + billID,
                "Bạn có muốn xóa hóa đơn này?",
                Alert.AlertType.NONE, ButtonType.YES, ButtonType.NO);

        // chọn khác yes thì out
        if (result.get() != ButtonType.YES) {
            return;
        }

        try {
            SpDeleteBill deleteBill = new SpDeleteBill();
            deleteBill.setBillid(billID);
            deleteBill.execute(ConnectToMySql.context.configuration());

            clickReloadData(event);
            MessageBox.showAndDismiss("XÓA HÓA ĐƠN", "Hóa đơn đã bị xóa", NotificationType.SUCCESS);
        } catch (DataAccessException ex) {
            MessageBox.showException("Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new javafx.scene.control.TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void clickReloadData(ActionEvent event) {
        loadGridMap();
        loadDataTableViewBill();
    }

    //sự kiện khi click vào button save
    @FXML
    private void clickSaveBill(ActionEvent event) {
        try {
            // lấy các thông tin trên textfield, combobox, dtp,...
            int tableID = ((TablesRecord) cbxTable.getValue()).getTableid();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String dateTimeString = dpkDateOrder.getValue().toString() + " "
                    + tpkTimeOrder.getValue().format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime orderDateTime = LocalDateTime.parse(dateTimeString, formatter);

            // thêm mới bill
            if (isAdd) {
                BillsRecord billsRecord = ConnectToMySql.context.newRecord(BILLS);
                billsRecord.setTableid(tableID);
                billsRecord.setIspaid((byte) 0);
                billsRecord.setIsstaying((byte) 1);
                billsRecord.setOrdertime(Timestamp.valueOf(orderDateTime));
                billsRecord.setGrandtotal(0.0);
                billsRecord.store();

                MessageBox.showAndDismiss("THÊM HÓA ĐƠN", "Hóa đơn mới đã được lưu", NotificationType.SUCCESS);
                clickReloadData(event);

                System.out.println("You created new bill:");
                System.out.println(billsRecord);
            } else { // update bill
                BillsRecord billsRecord = ConnectToMySql.context.fetchAny(BILLS, BILLS.BILLID.eq(billID));
                if (billsRecord == null) {
                    System.out.println("return at line: 714");
                    return;
                }
                int oldTableID = billsRecord.getTableid();

                if (tableID == oldTableID) {
                    txtValidTable.setText("");
                }

                billsRecord.setTableid(tableID);
                billsRecord.setOrdertime(Timestamp.valueOf(orderDateTime));
                billsRecord.setIspaid((byte) (chkPaid.isSelected() ? 1 : 0));
                billsRecord.setIsstaying((byte) (chkStaying.isSelected() ? 1 : 0));
                billsRecord.update();

                MessageBox.showAndDismiss("CẬP NHẬT HÓA ĐƠN", "Hóa đơn đã được thay đổi", NotificationType.SUCCESS);

                System.out.println("You updated bill:");
                System.out.println(billsRecord);
            }
            // load lại
            clickReloadData(event);
            clickCancelBill(event);
        } catch (NumberFormatException | DataAccessException ex) {
            MessageBox.showException("Lỗi xảy ra",
                    ex.getClass().getSimpleName(),
                    new javafx.scene.control.TextArea(ex.getMessage()),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clickCancelBill(ActionEvent event) {
        paneBackGround.setDisable(false);
        paneInfoBill.setDisable(true);
    }

    @FXML
    private void clickLogout(ActionEvent event) {
        try {
            Parent login = FXMLLoader.load(new URL(MyFunction.LOGIN_URI));
            Scene scene = new Scene(login);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(MyFunction.iconApp);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.show();
            Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            thisStage.setOnHidden(null);
            thisStage.close();
        } catch (IOException e) {

        }
    }

    @FXML
    private void clickTakeAwayButton(ActionEvent event) {
        BillsRecord billsRecord = ConnectToMySql.context.newRecord(BILLS);
        billsRecord.setTableid(0);
        billsRecord.setIspaid((byte) 1);
        billsRecord.setIsstaying((byte) 0);
        billsRecord.setOrdertime(Timestamp.valueOf(LocalDateTime.now()));
        billsRecord.setGrandtotal(0.0);
        billsRecord.store();
        VwBillDetailsRecord billdetailsRecord = ConnectToMySql.context.fetchAny(VW_BILL_DETAILS, VW_BILL_DETAILS.BILLID.eq(billsRecord.getBillid()));
        showInfoBill(billdetailsRecord);
        loadTableViewItem(billdetailsRecord);
        

        tableItem.setAccessibleText(billsRecord.getBillid() + "");
        myTab.getSelectionModel().select(itemsTab);
    }
}
