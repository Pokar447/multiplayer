package multiplayer.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {

    static ClientConnection clientConnection;
    static GameController controller;

    static int playerID;
    static int otherID;
    static int connectionStatus;

    static String character;
    static int characterId;

    static String enemyCharacter;
    static int enemyCharacterId;
    static int otherUserReady;

    public TextField username;
    public PasswordField password;
    public Text loginInfo;

    // Getter & Setter
    public int getPlayerID() {
        return playerID;
    }
    public String getMyCharacter() {
        return character;
    }
    public String getOtherCharacter() {
        switch (enemyCharacterId) {
            case 1:
                enemyCharacter = "GOLEM";
                break;
            case 2:
                enemyCharacter = "MINOTAUR";
            break;
            case 3:
                enemyCharacter = "SATYR";
            break;
            case 4:
                enemyCharacter = "WRAITH";
            break;
        }
        return enemyCharacter;
    }
    public int getOtherCharacterId() {
        return clientConnection.receiveCharacter();
    }
    public int getOtherUserReady() {
        return clientConnection.receiveUserReady();
    }
    public int getConnection() {
        return connectionStatus;
    }
    public void setButtonNum(int number) {
        clientConnection.sendButtonNum(number);
    }
    public void getEnemyMove() {
        /**
         * 0 == LEFT (press)
         * 1 == RIGHT (press)
         * 2 == UP (press)
         * 3 == SPACE (press)
         *
         * 4 == DEFAULT
         *
         * 5 == LEFT (release)
         * 6 == RIGHT (release)
         * 7 == UP (release)
         * 8 == SPACE (release)
         */
        int keyNumber = clientConnection.receiveButtonNum();

        if(keyNumber <= 1) {
            controller.moveEnemy(keyNumber);
        } else if(keyNumber == 3) {
            controller.enemyAttack(controller);
        } else if(keyNumber == 5 || keyNumber == 6) {
            controller.stopEnemy();
        } else if(keyNumber == 8) {
            controller.stopEnemyAttack();
        }

    }

    // Enemy update Thread
    public void startUpdateThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                getEnemyMove();
            }
        });
        thread.start();
    }

    // Connect to server
    public void connectToServer() {
        clientConnection = new ClientConnection();
    }

    // Start GUI
    @Override
    public void start(Stage stage) throws Exception {
        Parent login = FXMLLoader.load(getClass().getResource("/multiplayer.client/login.fxml"));
        Scene scene = new Scene(login);
        stage.setTitle("Player #" + playerID);
        stage.setScene(scene);
        stage.show();
    }

    public void game (javafx.event.ActionEvent event) throws IOException {
        System.out.println("start game...");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/multiplayer.client/game.fxml"));
        Parent main = loader.load();
        controller = loader.getController();
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getRoot().requestFocus();
    }

    public void userReady (javafx.event.ActionEvent event) throws IOException {

        clientConnection.sendUserReady();

        otherUserReady = -1;
        otherUserReady = getOtherUserReady();

        System.out.println("Other user ready!");

        characterCoice (event);
    }

    public void playerReady (javafx.event.ActionEvent event) throws IOException {

        if (characterId == 0) {
            characterId = 1;
        }

        clientConnection.sendCharacter(characterId);

        enemyCharacterId = -1;
        enemyCharacterId = getOtherCharacterId();

        System.out.println("Enemy choice is: " + enemyCharacterId);

        game(event);
    }

    public void setPlayer (javafx.event.ActionEvent event) {

        Node source = (Node) event.getSource();
        String id = source.getId();

        switch (id) {
            case "golemBtn":
                character = "GOLEM";
                characterId = 1;
                break;
            case "minotaurBtn":
                character = "MINOTAUR";
                characterId = 2;
                break;
            case "satyrBtn":
                character = "SATYR";
                characterId = 3;
                break;
            case "wraithBtn":
                character = "WRAITH";
                characterId = 4;
                break;
        }
    }

    // Login handler
    public void characterCoice (javafx.event.ActionEvent event) throws IOException {
        System.out.println("Choose xour character...");

        Parent lobby = FXMLLoader.load(getClass().getResource("/multiplayer.client/characterChoice.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(lobby);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Login handler
    public void login(javafx.event.ActionEvent event) throws IOException {
        System.out.println("Logged in as Player #" + this.getPlayerID());

        Parent lobby = FXMLLoader.load(getClass().getResource("/multiplayer.client/lobby.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(lobby);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void auth(javafx.event.ActionEvent event) throws IOException {
        System.out.println("Login as " + username.getText() + ", Pwd: " + password.getText());

        if (username.getText().equals("test") && password.getText().equals("test")) {
            login(event);
        }
        else {
            loginInfo.setText("Bad credentials!");
            loginInfo.setFill(Color.RED);
            loginInfo.setVisible(true);
            return;
        }
    }

    // Logout handler
    public void logout(javafx.event.ActionEvent event) throws IOException {
        System.out.println("Logged out...");

        Parent login = FXMLLoader.load(getClass().getResource("/multiplayer.client/login.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }

    // Register handler
    public void register (javafx.event.ActionEvent event) throws IOException {
        System.out.println("register ()");

        Parent register = FXMLLoader.load(getClass().getResource("/multiplayer.client/register.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(register));
        primaryStage.show();
    }

    public void cancle (javafx.event.ActionEvent event) throws IOException {
        Parent login = FXMLLoader.load(getClass().getResource("/multiplayer.client/login.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }

    // Main
    public static void main(String[] args) {
        Client client = new Client();
        client.connectToServer();
        launch(args);
    }
}

