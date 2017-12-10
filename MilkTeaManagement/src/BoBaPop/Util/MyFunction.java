package BoBaPop.Util;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import tray.notification.NotificationType;

public class MyFunction {

    // title cho mấy stage
    public static final String LOGIN_TITLE = "Đăng nhập";
    public static final String CASHIER_TITLE = "Ứng dụng quản lý";
    public static final String MANAGER_TITLE = "Quản lý";
    public static final String BILL_TITLE = "Quản lý hóa đơn";
    public static final String DRINK_TITLE = "Quản lý thức uống";
    public static final String STATISTIC_TITLE = "Quản lý thức uống";
    public static final String TABLE_TITLE = "Quản lý bàn";
    public static final String USER_TITLE = "Quản lý người dùng";
    public static final String ABOUT_TITLE = "Thông tin ứng dụng";

 
    public static final String LOGIN_URI = MyFunction.class.getResource("/BoBaPop/View/Login.fxml").toString();
    public static final String CASHIER_URI = MyFunction.class.getResource("/BoBaPop/View/Cashier/CashierWorkspace.fxml").toString();
    public static final String MANAGER_URI = MyFunction.class.getResource("/BoBaPop/View/Manager/ManagerWorkspace.fxml").toString();
    public static final String BILL_URI = MyFunction.class.getResource("/BoBaPop/View/Manager/BillManagement.fxml").toString();
    public static final String DRINK_URI = MyFunction.class.getResource("/BoBaPop/View/Manager/DrinkManagement.fxml").toString();
    public static final String STATISTIC_URI = MyFunction.class.getResource("/BoBaPop/View/Manager/StatisticManagement.fxml").toString();
    public static final String TABLE_URI = MyFunction.class.getResource("/BoBaPop/View/Manager/TableManagement.fxml").toString();
    public static final String ABOUT_URI = MyFunction.class.getResource("/BoBaPop/View/About.fxml").toString();
    public static final String TABLE_CELL_URI = MyFunction.class.getResource("/BoBaPop/View/Manager/Table.fxml").toString();
    public static final String USER_URI = MyFunction.class.getResource("/BoBaPop/View/Manager/UserManagement.fxml").toString();
    public static final String BILL_JASPER =  "src/BoBaPop/Report/BillReport.jasper";
    
    
    public static final String ADMIN = "admin";
    public static final String CASHIER = "cashier";
    public static void saveNodeAsPicture(Node node, Window parent) {
        FileChooser chooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
                "Portable Network Graphic file (*.png)", "*.png",
                "JPEG file (*.jpg)", "*.jpg",
                "GIF file (*.gif)", "*.gif");
        chooser.getExtensionFilters().add(filter);
        // Show save dialog
        File file = chooser.showSaveDialog(parent);
        if (file != null) {
            WritableImage image = node.snapshot(new SnapshotParameters(), null);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                MessageBox.showAndDismiss(
                        "THÀNH CÔNG", "File đã được lưu",
                        NotificationType.SUCCESS);
            } catch (IOException ex) {
                MessageBox.showAndWait(
                        "Lỗi đã xảy ra " + ex.getClass().getSimpleName(),
                        ex.getMessage(),
                        NotificationType.ERROR);
            }
        }
    }

    public static final Image iconApp = new Image("/BoBaPop/images/icon.png");
}
