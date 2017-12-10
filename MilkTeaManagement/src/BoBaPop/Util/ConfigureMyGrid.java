package BoBaPop.Util;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

public class ConfigureMyGrid {

    public static Grid grid;

    public static void initialize(GridPane pane, int numberOfNode) {
        //tính số hàng, cột cho gridpane
        if (numberOfNode <= 0) {
            return;
        }
        int row = 1;
        int col = 1;
        while (true) {
            if (row * col >= numberOfNode) {
                grid = new Grid(row, col);
                break;
            }
            if (row == col) {
                col++;
            } else {
                row++;
            }
        }

        //thêm cột
        pane.getColumnConstraints().clear();
        for (int i = 0; i < grid.getCol(); i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.prefWidthProperty().bind(pane.prefWidthProperty().divide(grid.getCol()));
            pane.getColumnConstraints().add(cc);
        }
        //thêm dòng
        pane.getRowConstraints().clear();
        for (int i = 0; i < grid.getRow(); i++) {
            RowConstraints rc = new RowConstraints();
            rc.prefHeightProperty().bind(pane.prefHeightProperty().divide(grid.getRow()));

            pane.getRowConstraints().add(rc);
        }
    }

}
