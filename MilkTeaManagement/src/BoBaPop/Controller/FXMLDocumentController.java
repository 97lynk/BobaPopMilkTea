package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import BoBaPop.Model.Routines;
import static BoBaPop.Model.Routines.isstaying;
import static BoBaPop.Model.Tables.*;
import BoBaPop.Model.routines.Isstaying;
import BoBaPop.Model.tables.records.BillsRecord;
import BoBaPop.Model.tables.records.CustomersRecord;
import BoBaPop.Model.tables.records.TablesRecord;
import BoBaPop.Model.tables.records.VwBilldetailsRecord;
import BoBaPop.Model.tables.records.VwItemsRecord;
import BoBaPop.Util.Animation;
import BoBaPop.Util.CustomizeNode;
import BoBaPop.Util.PropertiesNode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.jooq.DSLContext;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.jooq.Result;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.jooq.Record;

public class FXMLDocumentController implements Initializable {

    @FXML
    private TableView billTable;

    @FXML
    private TableView itemsTable;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXDatePicker dpkDateOrder;
    @FXML
    private JFXTimePicker tpkTimeOrder;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtCustomerID;
    @FXML
    private GridPane gpMap;
    @FXML
    private JFXCheckBox chkPaid;
    @FXML
    private JFXCheckBox chkStaying;
    @FXML
    private JFXDrawer navBar;
    @FXML
    private JFXHamburger hbg;

    @FXML
    private JFXButton btnSave;
    @FXML
    private Tab tabListBill;
    @FXML
    private JFXButton btnClose;
    @FXML
    private AnchorPane paneBackGround;
    @FXML
    private Label lblBillID;
    @FXML
    private JFXComboBox cbxTable;
    @FXML
    private Label txtValidTable;
    private AnchorPane paneChangeTable;
    @FXML
    private JFXButton btnModify;
    @FXML
    private JFXButton btnPay;
    @FXML
    private AnchorPane paneInfoBill;
    @FXML
    private AnchorPane panePaying;
    @FXML
    private JFXTextField txtGrandTotal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            ObservableList<TablesRecord> options
                    = FXCollections.observableArrayList(
                            context.selectFrom(TABLES).fetch()
                    );
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
            cbxTable.getItems().addAll(options);
        } catch (Exception ex) {
        }
        cbxTable.valueProperty().addListener((observable, oldValue, newValue) -> {
            try (DSLContext context = ConnectToMySql.usingDSLContext()) {
                int billID = Integer.parseInt(lblBillID.getAccessibleText());
                int tableID = context.selectFrom(BILLS).where(BILLS.BILLID.eq(billID)).fetchAny(BILLS.TABLEID);

                int selectedTableID = ((TablesRecord) newValue).getTableid();
                if (tableID == selectedTableID) {
                    txtValidTable.setText("");
                    btnSave.setDisable(false);
                    return;
                }
                Byte isStayingNewTable = context.select(isstaying(selectedTableID)).fetchAnyInto(Byte.class);
                if (isStayingNewTable == 1) {
                    txtValidTable.setText("Có người ngồi rồi");
                    btnSave.setDisable(true);
                } else {
                    txtValidTable.setText("");
                    btnSave.setDisable(false);
                }

            } catch (Exception ex) {

            }
        });

        loadDataForCell();
        try {
            String urlImage = getClass().getResource("/BoBaPop/images/bg_dridpane.jpg").toExternalForm();
            paneBackGround.setStyle("-fx-background-image: url('" + urlImage + "');"
                    + "-fx-background-size:cover;");

            //thêm nav silde bar
            VBox vBox = FXMLLoader.load(getClass().getResource("/BoBaPop/View/ManagerNavigationBar.fxml"));
            navBar.setSidePane(vBox);
            navBar.close();//tắt chưa vẽ

            //event cho hamburger để ẩn/hiện slide bar 
            HamburgerSlideCloseTransition burgerClose = new HamburgerSlideCloseTransition(hbg);
            burgerClose.setRate(-1);
            hbg.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                burgerClose.setRate(burgerClose.getRate() * -1);
                burgerClose.play();
                if (navBar.isShown()) {
                    navBar.close();
                } else {
                    navBar.open();

                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //thêm cột cho tableview bills
        VW_BILLDETAILS
                .fieldStream()
                .forEach(col -> {
                    TableColumn column = new TableColumn(col.getName());
                    column.setCellValueFactory(new PropertyValueFactory(col.getName().toLowerCase()));
                    if (col.getName().contains("ID")) {
                        column.setVisible(false);
                    }
                    billTable.getColumns().add(column);
                });
        loadDataForTableView();
    }

    private void loadDataForCell() {
        gpMap.getChildren().clear();
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            Map<Integer, Record> listData = new TreeMap<>();
            context.selectFrom(TABLES)
                    .fetchInto(TablesRecord.class)
                    .stream().forEach(tablesRecord -> {
                        Record record = null;
                        Byte isStaying = context.select(isstaying(tablesRecord.getTableid())).fetchAny(BILLS.ISSTAYING);
                        //bàn trống lấy hóa đơn cuối cùng
                        if (isStaying == 0) {
                            record = context
                                    .selectFrom(VW_BILLDETAILS)
                                    .where(VW_BILLDETAILS.TABLEID.eq(tablesRecord.getTableid()).and(VW_BILLDETAILS.ISSTAYING.eq(Byte.valueOf("0"))))
                                    .orderBy(VW_BILLDETAILS.ORDERTIME.asc())
                                    .fetchAny();
                            //lấy hóa đơn hiện tại
                        } else {
                            record = context
                                    .selectFrom(VW_BILLDETAILS)
                                    .where(VW_BILLDETAILS.TABLEID.eq(tablesRecord.getTableid()).and(VW_BILLDETAILS.ISSTAYING.eq(Byte.valueOf("1"))))
                                    .orderBy(VW_BILLDETAILS.ORDERTIME.asc())
                                    .fetchAny();
                        }
                        //bàn chưa có hóa đơn nào từ lúc setup
                        if (record == null) {
                            record = tablesRecord;

                        }
                        listData.put(tablesRecord.getTableid(), record);
                    });
            addCell(listData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void addCell(Map<Integer, Record> listTable) {
        //thêm các cài đặt và thêm các button vào gridpane
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            PropertiesNode configure = new PropertiesNode(gpMap, listTable.size());
            listTable.entrySet().forEach(entry -> {
                Integer tableID = entry.getKey();
                Record record = entry.getValue();
                JFXButton button = CustomizeNode.createMyButton(entry, configure.getwCell(), configure.gethCell());
                Label another = CustomizeNode.createMyLabel(entry, configure.getwCell(), configure.gethCell());

                //event 
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
                            new KeyValue(another.prefWidthProperty(), configure.getwCell()),
                            new KeyValue(another.prefHeightProperty(), 0));
                    timeline.getKeyFrames().add(keyFrame);
                    keyFrame = new KeyFrame(new Duration(300),
                            new KeyValue(another.prefWidthProperty(), configure.getwCell()),
                            new KeyValue(another.prefHeightProperty(), configure.gethCell(), Interpolator.EASE_IN));
                    timeline.getKeyFrames().add(keyFrame);
                    timeline.play();

                });
                button.setOnMouseExited(event -> {
                    //ẩn label 
                    //another.setOpacity(0.0);
                    Animation.playFadeTransition(another,
                            Duration.millis(200), Duration.millis(1000),
                            1.0, 0.0);
                    //hiện lại button
                    Animation.playFadeTransition(button,
                            Duration.millis(200), Duration.millis(1000),
                            0.0, 1.0);
                });
                button.addEventHandler(ActionEvent.ACTION, event -> {
                    JFXButton resourse = (JFXButton) event.getSource();
                    clearInfoBill();
                    if (resourse.getAccessibleText() != null
                            && resourse.getAccessibleText().trim().length() > 0) {
                        VwBilldetailsRecord billdetailsRecord = context.selectFrom(VW_BILLDETAILS)
                                .where(VW_BILLDETAILS.BILLID.eq(Integer.parseInt(resourse.getAccessibleText())))
                                .fetchAny();
                        showInfoBill(billdetailsRecord);
                    }
                    cbxTable.getSelectionModel().select(context.fetchAny(TABLES,
                            TABLES.TABLEID.eq(Integer.parseInt(resourse.getAccessibleHelp()))));
                });
                int index = listTable.keySet().stream().collect(Collectors.toList()).indexOf(tableID);
                gpMap.add(another,
                        index % configure.getGrid().getCol(),
                        index / configure.getGrid().getCol());
                gpMap.add(button,
                        index % configure.getGrid().getCol(),
                        index / configure.getGrid().getCol());

            });

            //button thêm bàn cuối cùng
            JFXButton btnAddTable = new JFXButton("+");
            btnAddTable.setStyle("-fx-background-color: rgba(0, 150, 136, 0.3);"
                    + "-fx-background-radius: 0;"
                    + "-fx-text-fill: white;"
                    + "-fx-font-size: 60px;"
            );
            //size
            btnAddTable.setPrefSize(configure.getwCell(), configure.gethCell());
            //alignment
            btnAddTable.setAlignment(Pos.CENTER); //nội dung trong button
            gpMap.add(btnAddTable,
                    listTable.size() % configure.getGrid().getCol(),
                    listTable.size() / configure.getGrid().getCol());
            JFXTextField field = new JFXTextField();
            field.setPrefWidth(configure.getwCell() - 5);
            field.setStyle("-fx-background-radius: 0;"
                    + "-fx-text-fill: white;"
                    + "-fx-font-size: 15px;");
            field.setOpacity(0.0);
            field.setOnAction(event -> {
                field.setOpacity(0.0);
                System.out.println(field.getText());
                TablesRecord newTable = context.newRecord(TABLES);
                newTable.setTablename(field.getText());
                newTable.store();
                loadDataForCell();
            });
            gpMap.add(field,
                    listTable.size() % configure.getGrid().getCol(),
                    listTable.size() / configure.getGrid().getCol());
            btnAddTable.setOnAction(event -> {
                btnAddTable.setOpacity(0.0);
                field.setOpacity(1.0);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void loadDataForTableView() {
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            List<VwBilldetailsRecord> collect
                    = context.selectFrom(VW_BILLDETAILS)
                            .fetchInto(VwBilldetailsRecord.class
                            );
            ObservableList<VwBilldetailsRecord> table = FXCollections.observableArrayList(collect);
            billTable.setItems(table);

        } catch (Exception e) {

        }
    }

    @FXML
    public void clickItem(MouseEvent event) {
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            VwBilldetailsRecord record = (VwBilldetailsRecord) billTable.getSelectionModel().getSelectedItem();
            Result<VwItemsRecord> result = context
                    .selectFrom(VW_ITEMS)
                    .where(VW_ITEMS.BILLID.eq(record.getBillid()))
                    .fetch();
            System.out.println(result);
            ObservableList table = FXCollections.observableArrayList(result);
            itemsTable.getColumns().clear();
            Arrays.stream(result.fields()).forEach(f -> {
                TableColumn column = new TableColumn(f.getName());
                column.setCellValueFactory(new PropertyValueFactory(f.getName().toLowerCase()));
                if (f.getName().toLowerCase().contains("id")) {
                    column.setVisible(false);
                }
                itemsTable.getColumns().add(column);
            });
            itemsTable.setItems(table);
            clearInfoBill();
            showInfoBill(record);
        } catch (Exception ex) {

        }
    }

    @FXML
    private void clickCloseButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clickSearchCustomer(ActionEvent event) {
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            int customerID = Integer.parseInt(txtCustomerID.getText());
            CustomersRecord customersRecord = context
                    .fetchAny(CUSTOMERS, CUSTOMERS.CUSTOMERID.eq(customerID));
            txtCustomerName.setText(customersRecord.getFullname());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void btnSaveBill(ActionEvent event) {
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            int billID = Integer.parseInt(lblBillID.getAccessibleText());
            int tableID = ((TablesRecord) cbxTable.getValue()).getTableid();
            int selectedTableID = ((TablesRecord) cbxTable.getValue()).getTableid();
             Byte isStayingNewTable = context.select(isstaying(tableID)).fetchAnyInto(Byte.class);
             if (tableID == selectedTableID) {
                txtValidTable.setText("");
                isStayingNewTable = 0;
            }
           
            if (isStayingNewTable == 1) {
                txtValidTable.setText("Có người ngồi rồi");
                System.out.println("ko update");
                return;
            }

            int customerID = Integer.parseInt(txtCustomerID.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String dateTimeString = dpkDateOrder.getValue().toString() + " "
                    + tpkTimeOrder.getValue().format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime orderDateTime = LocalDateTime.parse(dateTimeString, formatter);

            BillsRecord billsRecord = context.fetchOne(BILLS, BILLS.BILLID.eq(billID));
            billsRecord.setCustomerid(customerID);
            billsRecord.setTableid(tableID);
            billsRecord.setOrdertime(Timestamp.valueOf(orderDateTime));
            billsRecord.setIspaid((byte) (chkPaid.isSelected() ? 1 : 0));
            billsRecord.setIsstaying((byte) (chkStaying.isSelected() ? 1 : 0));
            billsRecord.update();
            loadDataForCell();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void showInfoBill(VwBilldetailsRecord record) {
        if (record != null) {
            //cbxTable.getSelectionModel().select(new TablesRecord(record.getTableid(), record.getTablename()));
            lblBillID.setText("Bill ID is " + record.getBillid());
            lblBillID.setAccessibleText(record.getBillid().toString());
            tpkTimeOrder.setValue(record.getOrdertime().toLocalDateTime().toLocalTime());
            dpkDateOrder.setValue(record.getOrdertime().toLocalDateTime().toLocalDate());
            txtCustomerName.setText(record.getFullname());
            txtCustomerID.setText(record.getCustomerid().toString());
            chkStaying.setSelected(record.getIsstaying() == 1);
            chkPaid.setSelected(record.getIspaid() == 1);
        }
    }

    private void clearInfoBill() {
        tpkTimeOrder.setValue(null);
        dpkDateOrder.setValue(null);
        txtCustomerName.setText("");
        txtCustomerID.setText("");
        chkStaying.setSelected(false);
        chkPaid.setSelected(false);
        lblBillID.setText("");
        lblBillID.setAccessibleText(null);
        txtValidTable.setText("");
    }

    private void clickChangeTable(ActionEvent event) {
        Animation.playFadeTransition(paneChangeTable, Duration.millis(1000), Duration.ZERO, 0.0, 1.0);
        Animation.playFadeTransition(btnModify, Duration.millis(1000), Duration.ZERO, 1.0, 0.0);
        Animation.playFadeTransition(btnPay, Duration.millis(1000), Duration.ZERO, 1.0, 0.0);
        Animation.playFadeTransition(paneInfoBill, Duration.millis(1000), Duration.ZERO, 1.0, 0.0);
        btnModify.setVisible(false);
        btnPay.setVisible(false);
        paneInfoBill.setVisible(false);
    }

    private void clickCloseTablePane(ActionEvent event) {
        Animation.playFadeTransition(paneChangeTable, Duration.millis(1000), Duration.ZERO, 0.0, 1.0);
    }

    @FXML
    private void clickButtonPay(ActionEvent event) {
        panePaying.setVisible(true);
        panePaying.setVisible(true);
        gpMap.setDisable(true);
        btnSave.setDisable(true);
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            int billID = Integer.parseInt(lblBillID.getAccessibleText());
            double grandTotal = context
                    .select(Routines.chargedgrandtotal(billID))
                    .fetchAnyInto(Double.class);
            System.out.println("grand total = " + grandTotal);
            txtGrandTotal.setText(grandTotal + "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Timeline timeline = new Timeline();
        KeyFrame frame1 = new KeyFrame(Duration.ZERO,
                new KeyValue(panePaying.opacityProperty(), 0.0),
                new KeyValue(panePaying.translateXProperty(), 200));
        KeyFrame frame2 = new KeyFrame(Duration.millis(300),
                new KeyValue(panePaying.opacityProperty(), 1.0, Interpolator.EASE_BOTH),
                new KeyValue(panePaying.translateXProperty(), 0, Interpolator.EASE_BOTH));
        timeline.getKeyFrames().addAll(frame1, frame2);
        timeline.play();

    }

    @FXML
    private void clickClosePayPane(ActionEvent event) {
        panePaying.setVisible(false);
        gpMap.setDisable(false);
        btnSave.setDisable(false);
        txtGrandTotal.setText("");
    }

    @FXML
    private void clickOKPayPane(ActionEvent event) {
        try (DSLContext context = ConnectToMySql.usingDSLContext()) {
            int billID = Integer.parseInt(lblBillID.getAccessibleText());
            double grandTotal = Double.parseDouble(txtGrandTotal.getText());

            BillsRecord billsRecord = context.fetchAny(BILLS, BILLS.BILLID.eq(billID));
            billsRecord.setGrandtotal(grandTotal);
            billsRecord.update();
            clearInfoBill();
            loadDataForCell();
        } catch (Exception e) {
            System.out.println("OK: " + e.getMessage());
        }
        clickClosePayPane(null);
    }

    @FXML
    private void clickModifyButton(ActionEvent event) {
        btnSave.setDisable(false);
        btnPay.setDisable(true);
    }

    @FXML
    private void clickCancel(ActionEvent event) {
        btnSave.setDisable(true);
        btnPay.setDisable(false);
    }

}
