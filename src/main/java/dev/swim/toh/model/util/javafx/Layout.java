package dev.swim.toh.model.util.javafx;

import javafx.scene.control.TableView;

public class Layout {

    public static void updateTableHeight(TableView<?> tableView) {
        int rows = tableView.getItems().size();
        double rowHeight = tableView.getFixedCellSize();
        double headerHeight = 28;
        tableView.setPrefHeight(headerHeight + rowHeight * rows);
    }
}
