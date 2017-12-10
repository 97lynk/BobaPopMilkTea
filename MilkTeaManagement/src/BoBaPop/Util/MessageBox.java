package BoBaPop.Util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class MessageBox {

    public static void showAndDismiss(String title, String body, NotificationType type, Duration duration) {
        TrayNotification trayNotification = new TrayNotification(title, body, type);
        trayNotification.showAndDismiss(duration);
        System.out.println("=====================================");
        System.out.println(title);
        System.out.println(body);
    }

    public static void showAndDismiss(String title, String body, NotificationType type) {
        showAndDismiss(title, body, type, Duration.millis(1000));
    }

    public static void showAndWait(String title, String body, NotificationType type) {
        TrayNotification trayNotification = new TrayNotification();
        trayNotification.setTray(title, body, type);
        trayNotification.showAndWait();
        System.out.println(trayNotification.getMessage());
    }

    public static Optional<ButtonType> show(String title, String header, String content, Alert.AlertType type, ButtonType... buttons) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().addAll(buttons);
        alert.getDialogPane().setMaxSize(
                Screen.getPrimary().getBounds().getWidth() * 0.3,
                Screen.getPrimary().getBounds().getHeight() * 0.3);
        System.out.println(title);
        System.out.println(header);
        System.out.println(content);
        return alert.showAndWait();

    }

    public static Optional<ButtonType> showException(
            String title, String header, Node node,
            Alert.AlertType type, ButtonType... buttons) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(node);
        alert.getButtonTypes().addAll(buttons);
        alert.getDialogPane().setMaxSize(
                Screen.getPrimary().getBounds().getWidth() * 0.3,
                Screen.getPrimary().getBounds().getHeight() * 0.3);
        System.out.println(title);
        System.out.println(header);
        return alert.showAndWait();
    }
}
