package BoBaPop.Util;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.Node;

public class Animation {

    private static TranslateTransition translateTransition;
    private static FadeTransition fadeTransition;
    private static RotateTransition rotateTransition;

    public static void playTranslateTransition(
            Node node, Duration duration, Duration delay,
            double fromX, double toX,
            double fromY, double toY,
            int cycleCount, boolean autoReverse) {
        translateTransition = new TranslateTransition();
        translateTransition.setDelay(delay);
        translateTransition.setNode(node);
        translateTransition.setDuration(duration);
        translateTransition.setFromX(fromX);
        translateTransition.setToX(toX);
        translateTransition.setFromY(fromY);
        translateTransition.setToY(toY);
        translateTransition.setCycleCount(cycleCount);
        translateTransition.setAutoReverse(autoReverse);
        translateTransition.play();
    }

    public static void playTranslateTransition(
            Node node, Duration duration, Duration delay,
            double fromX, double toX,
            double fromY, double toY) {
        playTranslateTransition(node, duration, delay, fromX, toX, fromY, toY, 1, false);
    }

    public static void playFadeTransition(
            Node node, Duration duration, Duration delay,
            double fromValue, double toValue,
            int cycleCount, boolean autoReverse) {
        fadeTransition = new FadeTransition();
        fadeTransition.setNode(node);
        fadeTransition.setDuration(duration);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setCycleCount(cycleCount);
        fadeTransition.setAutoReverse(autoReverse);
        fadeTransition.setDelay(delay);
        fadeTransition.play();
    }

    public static void playFadeTransition(
            Node node, Duration duration, Duration delay,
            double fromValue, double toValue) {
        playFadeTransition(node, duration, delay, fromValue, toValue, 1, false);
    }

    public static void playRotateTransition(
            Node node, Duration duration, Duration delay,
            double byAngle,
            int cycleCount, boolean autoReverse) {
        rotateTransition = new RotateTransition();
        rotateTransition.setNode(node);
        rotateTransition.setDelay(delay);
        rotateTransition.setDuration(duration);
        rotateTransition.setByAngle(byAngle);
        rotateTransition.setCycleCount(cycleCount);
        rotateTransition.setAutoReverse(autoReverse);
        rotateTransition.play();
    }

    public static void playRotateTransition(
            Node node, Duration duration, Duration delay,
            double fromAngle, double toAngle,
            int cycleCount, boolean autoReverse) {
        rotateTransition = new RotateTransition();
        rotateTransition.setNode(node);
        rotateTransition.setDuration(duration);
        rotateTransition.setFromAngle(fromAngle);
        rotateTransition.setToAngle(toAngle);
        rotateTransition.setCycleCount(cycleCount);
        rotateTransition.setAutoReverse(autoReverse);
        rotateTransition.play();
    }
}
