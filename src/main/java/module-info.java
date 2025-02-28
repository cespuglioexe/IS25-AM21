module it.polimi.it.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.it.galaxytrucker to javafx.fxml;
    exports it.polimi.it.galaxytrucker;
}