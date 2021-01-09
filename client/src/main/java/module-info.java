module multiplayer.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens multiplayer.client to javafx.fxml;
    exports multiplayer.client;
}