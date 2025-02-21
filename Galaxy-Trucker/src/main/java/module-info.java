module org.example.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.galaxytrucker to javafx.fxml;
    exports org.example.galaxytrucker;
}