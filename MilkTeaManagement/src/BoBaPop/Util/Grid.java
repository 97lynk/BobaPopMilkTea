package BoBaPop.Util;

public class Grid {

    private int row;
    private int col;

    public Grid(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getCountCell() {
        return row * col;
    }

}
