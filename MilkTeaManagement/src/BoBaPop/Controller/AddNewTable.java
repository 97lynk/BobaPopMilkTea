/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BoBaPop.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class AddNewTable implements Initializable {

    @FXML
    private JFXButton btnAddTable;
    @FXML
    private AnchorPane addTablePane;
    @FXML
    private JFXTextField txtTableName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void clickAdd(ActionEvent event) {
        btnAddTable.setVisible(false);
        addTablePane.setVisible(true);
        txtTableName.requestFocus();
    }

    @FXML
    private void clickClose(ActionEvent event) {
        btnAddTable.setVisible(true);
        addTablePane.setVisible(false);

    }
}
