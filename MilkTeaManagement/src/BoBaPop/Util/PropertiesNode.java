package BoBaPop.Util;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class PropertiesNode {

    private GridPane pane;
    private int numberOfNode;
    private double hCell;
    private double wCell;
    private double wNode;
    private Grid grid;

    public PropertiesNode(GridPane pane, int numberOfNode) {
        this.pane = pane;
        this.numberOfNode = numberOfNode;
        initialize();
    }

    public void initialize() {
        //tính số hàng, cột cho gridpane
        ConfigureCell fc = new ConfigureCell();
        grid = fc.make(numberOfNode + 1);

        //lấy kích thước GridPane
        double hPane = pane.getPrefHeight();
        double wPane = pane.getPrefWidth();
        if (hPane / wPane != 0.6) {
            return;
        }
        //tính kích thước mỗi ô
        hCell = (hPane - pane.getPadding().getTop() * 2) / grid.getRow(); //chiều cao
        wCell = (wPane - pane.getPadding().getLeft() * 2) / grid.getCol(); //chiều rộng
        wNode = Math.min(hCell, wCell);//Chiều rộng cho button

        //thêm cột
        pane.getColumnConstraints().clear();
        for (int i = 0; i < grid.getCol(); i++) {
            pane.getColumnConstraints().add(new ColumnConstraints(wCell));
        }
        //thêm dòng
        pane.getRowConstraints().clear();
        for (int i = 0; i < grid.getRow(); i++) {
            pane.getRowConstraints().add(new RowConstraints(hCell));
        }
    }

    public GridPane getPane() {
        return pane;
    }

    public int getNumberOfNode() {
        return numberOfNode;
    }

    public double gethCell() {
        return hCell;
    }

    public double getwCell() {
        return wCell;
    }

    public double getwNode() {
        return wNode;
    }

    public Grid getGrid() {
        return grid;
    }
    
  
}
