open module it.polimi.it.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires jdk.jfr;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires java.rmi;
    requires java.sql;
    requires org.fusesource.jansi;
    requires java.naming;
    requires java.net.http;
    requires java.smartcardio;

    exports it.polimi.it.galaxytrucker.messages;
    exports it.polimi.it.galaxytrucker.messages.servermessages;
    exports it.polimi.it.galaxytrucker.messages.clientmessages;
}