package Controllers;

import javafx.scene.image.ImageView;

public class MyImageView extends ImageView {
    GRIDSTATE gridstate = GRIDSTATE.EMPTY;
    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    private int rowNumber;
    private  int columnNumber;

}
