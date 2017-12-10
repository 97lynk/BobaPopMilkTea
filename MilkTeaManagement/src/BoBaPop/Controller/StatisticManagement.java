package BoBaPop.Controller;

import BoBaPop.DA.ConnectToMySql;
import static BoBaPop.Model.Tables.*;
import BoBaPop.Model.routines.SpGrossRevenueByDrink;
import BoBaPop.Model.routines.SpQuantityDrinkSoldByPeriod;
import com.jfoenix.controls.JFXDatePicker;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import BoBaPop.Model.routines.SpGrossRevenueByDrinktype;
import BoBaPop.Model.routines.SpGrossRevenueOfDrink;
import BoBaPop.Model.routines.SpGrossRevenueOfDrinktype;
import BoBaPop.Model.routines.SpQuantityDrinktypeSoldByPeriod;
import BoBaPop.Util.MyFunction;
import BoBaPop.Util.MyRecord;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import org.jooq.Record;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StatisticManagement implements Initializable {

    @FXML
    private JFXDatePicker toDate;
    @FXML
    private JFXDatePicker fromDate;

    @FXML
    private LineChart<String, Double> lineChart;
    @FXML
    private PieChart pieChart;

    @FXML
    private Label txtStatistic;
    @FXML
    private Label txtBy;

    @FXML
    private FontAwesomeIconView iconChart;

    private boolean clickDrink = true;
    private boolean clickQuantity = true;

    private final String DRINK_TEXT = "Thống kê thức uống";
    private final String DRINK_TYPE_TEXT = "Thống kê nhóm thức uống";
    private final String QUANTITY_TEXT = "Số lượng trong";
    private final String REVENUE_TEXT = "Doanh thu trong";
    private final String LINE_CHART_ICON = "LINE_CHART";
    private final String PIE_CHART_ICON = "PIE_CHART";

    private List<PieChart.Data> listDataPieChart = new ArrayList<>();
    private List<XYChart.Series<String, Double>> listDataLineChart = new ArrayList<>();
    @FXML
    private TableView<MyRecord> tableData = new TableView<>();
    private TableColumn<MyRecord, String> c1 = new TableColumn<>();
    private TableColumn<MyRecord, Double> c2 = new TableColumn<>();
    @FXML
    private Label txtDay;

    private void changeDate(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        if (toDate.getValue().isBefore(fromDate.getValue())) {
            LocalDate localDate = fromDate.getValue();
            fromDate.setValue(toDate.getValue());
            toDate.setValue(localDate);
        }
        LocalDate fDate = LocalDate.from(fromDate.getValue());
        LocalDate tDate = LocalDate.from(toDate.getValue());
        txtDay.setText(ChronoUnit.DAYS.between(fDate, tDate) + " ngày");
        buildChart(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
        pieChart.setStartAngle(90.0);
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        c2.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableData.getColumns().addAll(c1, c2);
        fromDate.valueProperty().addListener(this::changeDate);
        toDate.valueProperty().addListener(this::changeDate);
        fromDate.setValue(LocalDate.now().plusMonths(-1));
        clickQuantityDrink(null);
    }

    @FXML
    private void buildChart(ActionEvent event) {
        LocalDateTime dateTimeFrom = fromDate.getValue().atStartOfDay();
        LocalDateTime dateTimeTo = toDate.getValue().atStartOfDay();
        Timestamp timestampFrom = Timestamp.valueOf(dateTimeFrom);
        Timestamp timestampTo = Timestamp.valueOf(dateTimeTo);
        if (clickDrink) {
            if (clickQuantity) { // vẽ piechart số lượng theo thức uống
                listDataPieChart.clear();
                pieChart.getData().clear();
                SpQuantityDrinkSoldByPeriod data = new SpQuantityDrinkSoldByPeriod();
                data.setFromdate(timestampFrom);
                data.setTodate(timestampTo);
                data.execute(ConnectToMySql.context.configuration());

                data
                        .getResults().get(0).forEach(b -> {
                    listDataPieChart.add(new PieChart.Data(b.get(0, String.class
                    ), b.get(1, Integer.class
                    )));
                });
                pieChart.setData(FXCollections.observableArrayList(listDataPieChart));
                StringBuilder builder = new StringBuilder();

            } else { // vẽ linechart doanh thu theo thức uống
                listDataLineChart.clear();
                lineChart.getData().clear();
                SpGrossRevenueByDrink dt = new SpGrossRevenueByDrink();
                dt.setFromdate(timestampFrom);
                dt.setTodate(timestampTo);
                dt.execute(ConnectToMySql.context.configuration());

                for (Record record : dt.getResults().get(0)) {
                    LocalDateTime a = fromDate.getValue().atStartOfDay();
                    LocalDateTime b = fromDate.getValue().atStartOfDay().plusDays(3);
                    XYChart.Series<String, Double> series = new XYChart.Series<>();
                    series
                            .setName(record.get(0, String.class
                            ));

                    int id = ConnectToMySql.context.fetchAny(DRINKS,
                            DRINKS.DRINKNAME.eq(record.get(0, String.class
                            ))).getDrinkid();
                    for (int i = 0; i < ChronoUnit.DAYS.between(dateTimeFrom, dateTimeTo) / 3; i++) {

                        SpGrossRevenueOfDrink data = new SpGrossRevenueOfDrink();
                        data.setFromdate(Timestamp.valueOf(a));
                        data.setTodate(Timestamp.valueOf(b));
                        data.setDrinkid(id);
                        a = b;
                        b = b.plusDays(3);
                        data.execute(ConnectToMySql.context.configuration());

                        double sum
                                = data.getResults().get(0).stream().mapToDouble((r) -> {
                                    return r.get(1, Double.class
                                    );
                                }).sum();

                        series.getData()
                                .add(new XYChart.Data(a.getDayOfMonth() + "/" + a.getMonthValue(), sum));

                    }
                    listDataLineChart.add(series);
                }
                lineChart.getData().addAll(listDataLineChart);
            }

        } else {
            if (clickQuantity) {
                listDataPieChart.clear();
                pieChart.getData().clear();

                SpQuantityDrinktypeSoldByPeriod data = new SpQuantityDrinktypeSoldByPeriod();
                data.setFromdate(timestampFrom);
                data.setTodate(timestampTo);
                data.execute(ConnectToMySql.context.configuration());
                data
                        .getResults().get(0).forEach(b -> {
                    listDataPieChart.add(new PieChart.Data(b.get(0, String.class
                    ), b.get(1, Integer.class
                    )));
                });
                pieChart.setData(FXCollections.observableArrayList(listDataPieChart));
            } else {
                listDataLineChart.clear();
                lineChart.getData().clear();
                lineChart.setTitle("Quantity sold drink by type");

                //danh sách các loại và tổng danh thu từng loại
                SpGrossRevenueByDrinktype dt = new SpGrossRevenueByDrinktype();
                dt.setFromdate(timestampFrom);
                dt.setTodate(timestampTo);

                dt.execute(ConnectToMySql.context.configuration());

                for (Record record : dt.getResults().get(0)) {
                    LocalDateTime a = fromDate.getValue().atStartOfDay();
                    LocalDateTime b = fromDate.getValue().atStartOfDay().plusDays(3);
                    XYChart.Series<String, Double> series = new XYChart.Series<>();
                    series
                            .setName(record.get(0, String.class
                            ));

                    int id = ConnectToMySql.context.fetchAny(DRINKTYPES,
                            DRINKTYPES.DRINKTYPENAME.eq(record.get(0, String.class
                            ))).getDrinktypeid();
                    for (int i = 0; i < ChronoUnit.DAYS.between(dateTimeFrom, dateTimeTo) / 3; i++) {
                        SpGrossRevenueOfDrinktype data = new SpGrossRevenueOfDrinktype();
                        data.setFromdate(Timestamp.valueOf(a));
                        data.setTodate(Timestamp.valueOf(b));
                        data.setDrinktypeid(id);
                        a = b;
                        b = b.plusDays(3);
                        data.execute(ConnectToMySql.context.configuration());

                        double sum
                                = data.getResults().get(0).stream().mapToDouble((r) -> {
                                    return r.get(1, Double.class
                                    );
                                }).sum();
                        series.getData()
                                .add(new XYChart.Data(a.getDayOfMonth() + "/" + a.getMonthValue(), sum));

                    }
                    listDataLineChart.add(series);
                }
                lineChart.getData().addAll(listDataLineChart);
            }
        }
    }

    @FXML
    private void viewChartData(ActionEvent event) {
        c1.setText((clickDrink) ? "Tên thức uống" : "Nhóm thức uống");
        c2.setText((clickQuantity) ? "Đã bán (ly)" : "Doanh thu (vnđ)");
        tableData.getItems().clear();
        if (clickQuantity) {
            tableData.getItems().addAll(FXCollections
                    .observableArrayList(listDataPieChart.stream().map(p -> {
                        return new MyRecord(p.getName(), p.getPieValue());
                    }).collect(Collectors.toList())));

        } else {
            listDataLineChart.stream()
                    .forEach(p -> {
                        String name = p.getName();
                        Double sum = p.getData().stream().mapToDouble(XYChart.Data::getYValue).sum();
                        tableData.getItems().add(new MyRecord(name, sum));
                        List<XYChart.Data<String, Double>> data = p.getData();
                        data.forEach(d -> {
                            tableData.getItems().add(new MyRecord(
                                    "\t" + d.XValueProperty().get(),
                                    d.YValueProperty().get()));
                        });

                    });
        }

    }

    @FXML
    private void saveChartAsPng(ActionEvent event) {
        if (clickQuantity) {
            MyFunction.saveNodeAsPicture(pieChart, pieChart.getParent().getScene().getWindow());
        } else {
            MyFunction.saveNodeAsPicture(lineChart, pieChart.getParent().getScene().getWindow());
        }
    }

    @FXML
    private void clickRevenueDrink(ActionEvent event) {
        clickDrink = true;
        clickQuantity = false;

        txtStatistic.setText(DRINK_TEXT);
        txtBy.setText(REVENUE_TEXT);
        pieChart.setVisible(false);
        lineChart.setVisible(true);
        iconChart.setGlyphName(LINE_CHART_ICON);

        buildChart(event);
        viewChartData(event);
    }

    @FXML
    private void clickQuantityDrink(ActionEvent event) {
        clickDrink = true;
        txtStatistic.setText(DRINK_TEXT);

        clickQuantity = true;
        txtBy.setText(QUANTITY_TEXT);
        pieChart.setVisible(true);
        lineChart.setVisible(false);

        iconChart.setGlyphName(PIE_CHART_ICON);
        buildChart(event);
        viewChartData(event);
    }

    @FXML
    private void clickRevenueType(ActionEvent event) {
        clickDrink = false;
        txtStatistic.setText(DRINK_TYPE_TEXT);

        clickQuantity = false;
        txtBy.setText(REVENUE_TEXT);
        pieChart.setVisible(false);
        lineChart.setVisible(true);
        iconChart.setGlyphName(LINE_CHART_ICON);
        buildChart(event);
        viewChartData(event);
    }

    @FXML
    private void clickQuantityType(ActionEvent event) {
        clickDrink = false;
        txtStatistic.setText(DRINK_TYPE_TEXT);

        clickQuantity = true;
        txtBy.setText(QUANTITY_TEXT);
        pieChart.setVisible(true);
        lineChart.setVisible(false);
        iconChart.setGlyphName(PIE_CHART_ICON);
        buildChart(event);
        viewChartData(event);
    }

    @FXML
    private void clickPre(ActionEvent event) {
        LocalDate fDate = LocalDate.from(fromDate.getValue());
        LocalDate tDate = LocalDate.from(toDate.getValue());
        long range = ChronoUnit.DAYS.between(fDate, tDate);
        fromDate.setValue(fDate.plusDays(-range));
        toDate.setValue(tDate.plusDays(-range));
    }

    @FXML
    private void clickNext(ActionEvent event) {
        LocalDate fDate = LocalDate.from(fromDate.getValue());
        LocalDate tDate = LocalDate.from(toDate.getValue());
        long range = ChronoUnit.DAYS.between(fDate, tDate);
        fromDate.setValue(fDate.plusDays(range));
        toDate.setValue(tDate.plusDays(range));
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

  
}
