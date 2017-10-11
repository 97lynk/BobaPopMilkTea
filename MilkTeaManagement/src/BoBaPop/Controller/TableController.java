/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BoBaPop.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.*;

public class TableController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private AnchorPane drapPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void andchorDetected(MouseEvent event) {
        Dragboard db = label.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(label.getId());
        db.setContent(content);
        System.out.print("onDragDetected");

        event.consume();
    }

    @FXML
    private void drapOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
        event.consume();
    }
}
