module multiplayer.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.httpcomponents.httpclient;
    requires httpcore;

    opens multiplayer.client to javafx.fxml;
    exports multiplayer.client;
}