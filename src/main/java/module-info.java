module it.polimi.it.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires jdk.jfr;
    requires com.fasterxml.jackson.databind;


    opens it.polimi.it.galaxytrucker to javafx.fxml;
    exports it.polimi.it.galaxytrucker;
}