module it.polimi.it.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.desktop;
    requires junit;


    opens it.polimi.it.galaxytrucker to javafx.fxml;
    exports it.polimi.it.galaxytrucker;
}