package multiplayer.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main Client Application class
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class Client extends Application {

    static ClientConnection clientConnection;
    static GameController controller;

    static int playerID;
    static int otherID;
    static int userID;
    static int opponentID;
    static int connectionStatus;

    static String character;
    static int characterId;

    static String enemyCharacter;
    static int enemyCharacterId;
    static int otherUserReady;

    static String jwt;
    public ImageView avatarImgView;

    JSONArray historyJsonArray;
    JSONObject historyJsonObject;
    public TableView<History> historyTable;
    public Text siegeTxt;
    public Text niederlagenTxt;

    public TextField username;
    public PasswordField password;
    public Text loginInfo;
    public Button characterSubmitBtn;
    public TextField email;
    public Label registerErrorLbl;

    @FXML
    public BorderPane borderPane;

    @FXML
    public Button userReadyBtn;

    /**
     * @return playerID ID of the player
     */
    public int getPlayerID() {
        return playerID;
    }
    /**
     * @return character Character of the player
     */
    public String getMyCharacter() {
        return character;
    }
    /**
     * @return character Character of the other player
     */
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
    /**
     * @return characterId Character id of the other player
     */
    public int getOtherCharacterId() {
        return clientConnection.receiveCharacter();
    }
    /**
     * @return opponentId ID of the other player
     */
    public int getOpponentId() {
        return clientConnection.receiveUserId();
    }
    /**
     * @return readyState Ready state of the other player
     */
    public int getOtherUserReady() {
        return clientConnection.receiveUserReady();
    }
    public int getConnection() {
        return connectionStatus;
    }
    /**
     * @param number Button number that was clicked by the player
     */
    public void setButtonNum(int number) {
        clientConnection.sendButtonNum(number);
    }
    /**
     * Gets the enemy movement
     */
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

    /**
     * Thread used to update the enemy movement
     */
    public void startUpdateThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                getEnemyMove();
            }
        });
        thread.start();
    }

    /**
     * Instantiates the connection to the server
     */
    public void connectToServer() {
        clientConnection = new ClientConnection();
    }

    /**
     * Starts the JavaFX view
     *
     * @param stage Initial JavaFX stage
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent login = FXMLLoader.load(getClass().getResource("/multiplayer.client/login.fxml"));
        Scene scene = new Scene(login);
        stage.setTitle("Player #" + playerID);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Starts the JavaFX game view
     *
     * @param event JavaFX event to be handled
     */
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

    /**
     * Set the ready state for the user and start the game when other player is ready
     *
     * @param event JavaFX event to be handled
     */
    public void userReady (javafx.event.ActionEvent event) throws IOException {

        userReadyBtn.setDisable(true);
        userReadyBtn.setText("waiting for other player...");

        clientConnection.sendUserReady();

        otherUserReady = -1;

        Thread t = new Thread() {
            public void run() {

                while (otherUserReady == -1) {
                    otherUserReady = getOtherUserReady();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            characterCoice (event);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                });

            }
        };
        t.start();

        System.out.println("Other user ready!");
    }

    /**
     * Send player information when ready
     *
     * @param event JavaFX event to be handled
     */
    public void playerReady (javafx.event.ActionEvent event) throws IOException {

        if (characterId == 0) {
            characterId = 1;
        }

        clientConnection.sendCharacter(characterId);
        clientConnection.sendUserId(userID);

        enemyCharacterId = -1;
        enemyCharacterId = getOtherCharacterId();
        opponentID = getOpponentId();

        System.out.println("Enemy choice is: " + enemyCharacterId);

        game(event);
    }

    /**
     * Sets the chosen characters inside the JavaFX game view
     *
     * @param event JavaFX event to be handled
     */
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

    /**
     * Opens the JavaFX character choice view
     *
     * @param event JavaFX event to be handled
     */
    public void characterCoice (javafx.event.ActionEvent event) throws IOException {
        System.out.println("Choose your character...");

        Parent lobby = FXMLLoader.load(getClass().getResource("/multiplayer.client/characterChoice.fxml"));
        Stage primaryStage = (Stage) borderPane.getScene().getWindow();
        Scene scene = new Scene(lobby);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Calls the user history from the server and opens the JavaFX statistics view
     *
     * @param event JavaFX event to be handled
     */
    public void statistics (javafx.event.ActionEvent event) throws IOException {
        System.out.println("Statistics...");

        try {
            HttpClient client = new DefaultHttpClient();
            URIBuilder uriBuilder = new URIBuilder("http://localhost:8080/history");
            uriBuilder.setParameter("userid", Integer.toString(userID));
            HttpGet getRequest = new HttpGet(uriBuilder.build());

            getRequest.setHeader(HttpHeaders.AUTHORIZATION, jwt);
            HttpResponse response = client.execute(getRequest);

            int responseStatus = response.getStatusLine().getStatusCode();

            if (responseStatus == 200) {
                historyJsonArray = new JSONArray(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/multiplayer.client/statistics.fxml"));
        loader.setController(this);
        Parent statistics = loader.load();

        // My HP column
        TableColumn<History, Integer> col1 = new TableColumn<>("My HP");
        col1.setMinWidth(150);
        col1.setCellValueFactory(new PropertyValueFactory<>("userHp"));
        // Opponent name column
        TableColumn<History, Integer> col2 = new TableColumn<>("Opponent name");
        col2.setMinWidth(150);
        col2.setCellValueFactory(new PropertyValueFactory<>("opponentName"));
        // Opponent HP column
        TableColumn<History, Integer> col3 = new TableColumn<>("Opponent HP");
        col3.setMinWidth(150);
        col3.setCellValueFactory(new PropertyValueFactory<>("opponentHp"));
        // Date/Time column
        TableColumn<History, Integer> col4 = new TableColumn<>("Date/Time");
        col4.setMinWidth(150);
        col4.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<History> history = getHistory();

        historyTable.setItems(history);

        List<History> victoryList = history.stream().filter(historyInstance -> historyInstance.getUserHp() > 0).collect(Collectors.toList());

        List<History> failureList = history.stream().filter( historyInstance -> historyInstance.getUserHp() == 0).collect(Collectors.toList());

        siegeTxt.setText(String.valueOf(victoryList.size()));
        niederlagenTxt.setText(String.valueOf(failureList.size()));

        historyTable.getColumns().addAll(col1, col2, col3, col4);

        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(statistics);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Creates an observable list for the history table inside the statistics view
     *
     * @return history Observable list containing the user history
     */
    public ObservableList<History> getHistory() {
        ObservableList<History> history = FXCollections.observableArrayList();
        for(int i=0; i<historyJsonArray.length(); i++) {
            historyJsonObject = historyJsonArray.getJSONObject(i);
            history.add(new History(historyJsonObject.optInt("id")
                    , historyJsonObject.optInt("userId")
                    , historyJsonObject.optInt("opponentId")
                    , historyJsonObject.optInt("userHp")
                    , historyJsonObject.optInt("opponentHp")
                    , historyJsonObject.optString("dateTime")
                    , historyJsonObject.optString("opponentname")));
        }
        return history;
    }

    /**
     * Handles the login of a user
     *
     * @param event JavaFX event to be handled
     */
    public void login(javafx.event.ActionEvent event) throws IOException {
        System.out.println("Logged in as Player #" + this.getPlayerID());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/multiplayer.client/lobby.fxml"));
        loader.setController(this);
        Parent lobby = loader.load();

        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(lobby);
        primaryStage.setScene(scene);
        primaryStage.show();

        byte[] profileImgByte = getProfileImg();
        avatarImgView.setImage(new Image(new ByteArrayInputStream(profileImgByte)));
    }

    /**
     * Take the user password and hashes it, calls login function
     *
     * @param event JavaFX event to be handled
     */
    public void auth(javafx.event.ActionEvent event) throws IOException {

        String pwHash = new CustomPasswordEncryptor().encryptPassword (password.getText());

        if (sendAuth(pwHash)) {
            if (getUserId(username.getText())) {
                login(event);
            }
        }
        else {
            loginInfo.setText("Bad credentials!");
            loginInfo.setFill(Color.RED);
            loginInfo.setVisible(true);
            return;
        }
    }

    /**
     * Handles the logout
     *
     * @param event JavaFX event to be handled
     */
    public void logout(javafx.event.ActionEvent event) throws IOException {
        System.out.println("Logged out...");

        jwt = null;

        Parent login = FXMLLoader.load(getClass().getResource("/multiplayer.client/login.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }

    /**
     * Sends the user credentials to the server
     *
     * @param pwHash Hashed user password
     *
     * @return Boolean True or false wether the authentication was successful or not
     */
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
                jwt = response.getHeaders(HttpHeaders.AUTHORIZATION)[0].getValue();
                return true;
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return false;
    }

    /**
     * Gets the user id based on the user name during the login
     *
     * @param username User name to the user id for
     *
     * @return Boolean True or false wether a user id exists for the user name
     */
    public boolean getUserId(String username) {
        try {
            HttpClient client = new DefaultHttpClient();
            URIBuilder uriBuilder = new URIBuilder("http://localhost:8080/users");
            uriBuilder.setParameter("username", username);
            HttpGet getRequest = new HttpGet(uriBuilder.build());

            getRequest.setHeader(HttpHeaders.AUTHORIZATION, jwt);
            HttpResponse response = client.execute(getRequest);

            int responseStatus = response.getStatusLine().getStatusCode();

            if (responseStatus == 200) {
                userID = Integer.parseInt(EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
                return true;
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Receives the default profile picture during the login
     *
     * @return byte[] Byte array that contains the bytes of the profile picture
     */
    public byte[] getProfileImg() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet("http://localhost:8080/users/image");

            getRequest.setHeader(HttpHeaders.AUTHORIZATION, jwt);
            HttpResponse response = client.execute(getRequest);

            int responseStatus = response.getStatusLine().getStatusCode();

            if (responseStatus == 200) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                response.getEntity().writeTo(baos);
                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * Sends the user credentials to register a new user
     */
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
            Must have at least one special symbol among !@#$%
            Password length should be between 8 and 20
        */
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%]).{8,20}$";
        boolean validPassword = new CustomInputValidator().isValidPassword(passwordString,regex);

        if (usernameString.equals("")) {
            registerErrorLbl.setVisible(true);
            registerErrorLbl.setText("username cannot not be empty");
            return;
        }

        if (!validPassword) {
            registerErrorLbl.setVisible(true);
            registerErrorLbl.setText("password must have at least one numeric character, one lowercase character, one uppercase character, one special character!@#$%, length should be between 8 and 20");
            return;
        }

        boolean isValidEmailAddress = new CustomInputValidator().isValidEmailAddress(emailString);
        if (!isValidEmailAddress) {
            registerErrorLbl.setVisible(true);
            registerErrorLbl.setText("email is invalid");
            return;
        }

        String pwHash = new CustomPasswordEncryptor().encryptPassword (passwordString);

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

    /**
     * Clears the info label
     */
    private void clearInfo () {
        registerErrorLbl.setVisible(false);
        registerErrorLbl.setText("");
    }

    /**
     * Starts the JavaFX register view
     *
     * @param event JavaFX event to be handled
     */
    public void register (javafx.event.ActionEvent event) throws IOException {
        System.out.println("register ()");

        Parent register = FXMLLoader.load(getClass().getResource("/multiplayer.client/register.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(register));
        primaryStage.show();
    }

    /**
     * Starts the JavaFX lobby view when the user clicked the cancel button
     *
     * @param event JavaFX event to be handled
     */
    public void cancel(javafx.event.ActionEvent event) throws IOException {
        Parent login = FXMLLoader.load(getClass().getResource("/multiplayer.client/login.fxml"));
        Stage primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }

    /**
     * Main methode of the Client Application
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.connectToServer();
        launch(args);
    }
}

