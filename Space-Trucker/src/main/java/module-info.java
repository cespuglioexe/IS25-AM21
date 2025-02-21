module org.example.spacetrucker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.spacetrucker to javafx.fxml;
    exports org.example.spacetrucker;
}