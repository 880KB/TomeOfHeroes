module dev.swim.toh {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.swim.toh to javafx.fxml;
    exports dev.swim.toh;
}