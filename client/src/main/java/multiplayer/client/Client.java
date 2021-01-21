package multiplayer.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    String jwt;

    public TextField username;
    public PasswordField password;
    public Text loginInfo;
    public Button characterSubmitBtn;
    public TextField email;
    public Label registerErrorLbl;

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
        characterSubmitBtn.setDisable(false);
    }

    // Character choice handler
    public void characterCoice (javafx.event.ActionEvent event) throws IOException {
        System.out.println("Choose xour character...");

        Parent lobby = FXMLLoader.load(getClass().getResource("/multiplayer.client/characterChoice.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(lobby);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Statistics handler
    public void statistics (javafx.event.ActionEvent event) throws IOException {
        System.out.println("Statistics...");

        Parent statistics = FXMLLoader.load(getClass().getResource("/multiplayer.client/statistics.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(statistics);
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

    // Authentication handler
    public void auth(javafx.event.ActionEvent event) throws IOException {

        String pwHash = encryptPassword (password.getText());

        if (sendAuth(pwHash)) {
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

        jwt = null;

        Parent login = FXMLLoader.load(getClass().getResource("/multiplayer.client/login.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }

    // Send credentials for authentication
    public boolean sendAuth (String pwHash) {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost:8080/login");

            StringEntity input = new StringEntity("{\"username\":\""+ username.getText()+"\",\"password\":\"" + pwHash + "\"}");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = client.execute(postRequest);

            int responseStatus = response.getStatusLine().getStatusCode();

            if (responseStatus == 200) {
                jwt = response.getHeaders(HttpHeaders.AUTHORIZATION)[0].toString();
                return true;
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return false;
    }

    // Send credentials to register new user
    public void sendRegisterUser () {

        clearInfo();

        String usernameString = username.getText();
        String passwordString = password.getText();
        String emailString = email.getText();

        /*
            regex explanation:
            Must have at least one numeric character
            Must have at least one lowercase character
            Must have at least one uppercase character
            Must have at least one special symbol among @#$%
            Password length should be between 8 and 20
        */
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        boolean validPassword = isValidPassword(passwordString,regex);

        if (!validPassword) {
            registerErrorLbl.setVisible(true);
            registerErrorLbl.setText("password must have at least one numeric character, one lowercase character, one uppercase character, length should be between 8 and 20");
            return;
        }

        boolean isValidEmailAddress = isValidEmailAddress(emailString);
        if (!isValidEmailAddress) {
            registerErrorLbl.setVisible(true);
            registerErrorLbl.setText("email is invalid");
            return;
        }

        String pwHash = encryptPassword (passwordString);

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost:8080/users/sign-up");

            StringEntity input = new StringEntity("{\"username\":\""+ usernameString +"\",\"email\":\"" + emailString +"\",\"password\":\"" + pwHash + "\"}");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = client.execute(postRequest);

            int responseStatus = response.getStatusLine().getStatusCode();

            if (responseStatus == 400) {
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                registerErrorLbl.setVisible(true);
                registerErrorLbl.setText(responseBody);

            } else if (responseStatus == 200) {
                registerErrorLbl.setVisible(true);
                registerErrorLbl.setText("OK");
            }

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    private String encryptPassword (String password) {

        String algorithm = "SHA";

        byte[] plainText = password.getBytes();

        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            md.reset();
            md.update(plainText);
            byte[] encodedPassword = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < encodedPassword.length; i++) {
                if ((encodedPassword[i] & 0xff) < 0x10) {
                    sb.append("0");
                }

                sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void clearInfo () {
        registerErrorLbl.setVisible(false);
        registerErrorLbl.setText("");
    }

    public void register (javafx.event.ActionEvent event) throws IOException {
        System.out.println("register ()");

        Parent register = FXMLLoader.load(getClass().getResource("/multiplayer.client/register.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(register));
        primaryStage.show();
    }

    public static boolean isValidPassword(String password,String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidEmailAddress(String email) {
        // create the EmailValidator instance
        EmailValidator validator = EmailValidator.getInstance();

        // check for valid email addresses using isValid method
        return validator.isValid(email);
    }

    public void cancel(javafx.event.ActionEvent event) throws IOException {
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

