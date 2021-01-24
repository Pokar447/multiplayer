package multiplayer.client;

// Java imports
import java.io.IOException;
import java.net.*;
import java.util.*;

// JavaFX imports
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import multiplayer.client.characters.Character;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import static org.apache.commons.text.CharacterPredicates.DIGITS;

public class GameController implements Initializable {

    // General
    Client client = new Client();
    static Character myCharacter = new Character();
    static Character otherCharacter = new Character();

    // Character choice
    public String myChoice;
    public String otherChoice;

    // Player ImageViews
    public static ImageView myPlayer;
    public static ImageView otherPlayer;
    public ImageView player1;
    public ImageView player2;

    public Text myPlayerLifepoints;
    public Text otherPlayerLifepoints;

    public Text playerWonTxt;
    public Button exitGameBtn;

    // Move Timer
    public Timer myMoveTimer;
    public Timer otherMoveTimer;

    // Width & height of the usable stage for collision detection
    public static int stageWidth = 1200;
    public static int stageHeight = 720;

    // State helper
    public boolean myIsMoving = false;
    public boolean otherIsMoving = false;

    private static int countdown = 10;
    public Text countdownTxt;

    // Initialize client GUI
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkConnection();
        checkPlayer();
        myCharacter.createCharacter(myChoice);
        otherCharacter.createCharacter(otherChoice);
        myCharacter.idleAnimation(myPlayer, myCharacter.idleTimeline, myCharacter.idle);
        otherCharacter.idleAnimation(otherPlayer, otherCharacter.idleTimeline, otherCharacter.idle);

        startCountdownTimer();
    }

    private void startCountdownTimer () {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (countdown > 0) {
                    countdown--;
                    countdownTxt.setText(Integer.toString(countdown));
                }
                if (countdown == 0) {
                    client.startUpdateThread();
                    countdownTxt.setVisible(false);
                    timer.cancel();
                    timer.purge();
                }
                System.out.println(countdown);
            }
        };
        timer.schedule(task, 0, 1000);
    }

    // Check client connection
    public void checkConnection() {
        if(client.getConnection() == 1) {
        }
        else {
        }
    }

    // Check who is which player
    public void checkPlayer() {
        if(client.getPlayerID() == 1) {
            myPlayer = player1;
            otherPlayer = player2;
            myChoice = client.getMyCharacter();
            otherChoice = client.getOtherCharacter();
            myCharacter.direction = 1;
            otherCharacter.direction = -1;
        } else {
            myPlayer = player2;
            otherPlayer = player1;
            myChoice = client.getMyCharacter();
            otherChoice = client.getOtherCharacter();
            myCharacter.direction = -1;
            otherCharacter.direction = 1;
        }
    }

    // Handle key press
    public void keyPressed(KeyEvent event) {
        if (countdown == 0) {
            switch (event.getCode()) {
                case LEFT:
                    movePlayer("LEFT");
                    client.setButtonNum(0);
                    break;
                case RIGHT:
                    movePlayer("RIGHT");
                    client.setButtonNum(1);
                    break;
                case UP:
                    System.out.println("UP pressed");
                    client.setButtonNum(2);
                    break;
                case SPACE:
                    playerAttack();
                    client.setButtonNum(3);
                    break;
                default:
                    break;
            }
        }
    }

    // Handle key release
    public void keyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                stopPlayer();
                client.setButtonNum(5);
                break;
            case RIGHT:
                stopPlayer();
                client.setButtonNum(6);
                break;
            case UP:
                System.out.println("UP released");
                client.setButtonNum(7);
                break;
            case SPACE:
                stopPlayerAttack();
                client.setButtonNum(8);
                break;
            default:
                break;
        }
    }

    void setScale (ImageView imageView, int direction, boolean enemyMovement) {
        if (!enemyMovement) {
            if (direction < 0) {
                // Rinks
                if (client.getPlayerID() == 1) {
                    imageView.setScaleX(-2);

                } else {
                    imageView.setScaleX(2);
                }
            } else if (direction > 0) {
                // Rechts
                if (client.getPlayerID() == 1) {
                    imageView.setScaleX(2);
                } else {
                    imageView.setScaleX(-2);
                }
            }
        } else {
            if (direction < 0) {
                // Links
                if (client.getPlayerID() == 1) {
                    imageView.setScaleX(2);
                } else {
                    imageView.setScaleX(-2);
                }
            } else if (direction > 0) {
                // Rechts
                if (client.getPlayerID() == 1) {
                    imageView.setScaleX(-2);
                } else {
                    imageView.setScaleX(2);
                }
            }
        }

    }

    // Move player
    public void movePlayer(String key) {
        if(myIsMoving == false) {
            myIsMoving = true;
            myMoveTimer = new Timer();

            switch (key) {
                case "LEFT":
                    myCharacter.leftIsPressed = true;

                    myCharacter.direction = -1;
                    setScale (myPlayer, myCharacter.direction, false);

                    myCharacter.startAnimation(myPlayer, myCharacter.idleTimeline, myCharacter.walk, "LEFT");
                    myMoveTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (myPlayer.getLayoutX() < 0) {
                                myPlayer.relocate(myPlayer.getLayoutX(), myPlayer.getLayoutY());
                            }
                            else {
                                myPlayer.relocate(myPlayer.getLayoutX()-10, myPlayer.getLayoutY());
                            }
                        }
                    }, 50, 50);
                    break;
                case "RIGHT":
                    myCharacter.rightIsPressed = true;

                    myCharacter.direction = 1;
                    setScale (myPlayer, myCharacter.direction, false);

                    myCharacter.startAnimation(myPlayer, myCharacter.idleTimeline, myCharacter.walk, "RIGHT");
                    myMoveTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (myPlayer.getLayoutX() > stageWidth) {
                                myPlayer.relocate(myPlayer.getLayoutX(), myPlayer.getLayoutY());
                            }
                            else {
                                myPlayer.relocate(myPlayer.getLayoutX()+10, myPlayer.getLayoutY());
                            }
                        }
                    }, 50, 50);
                    break;
            }

        }
    }

    // Stop player
    public void stopPlayer() {
        if(myIsMoving == true) {
            myCharacter.rightIsPressed = false;
            myCharacter.leftIsPressed = false;
            myCharacter.walkIsActive = false;
            myIsMoving = false;
            myMoveTimer.cancel();
            myCharacter.walkTimeline.stop();
            myCharacter.idleTimeline.play();
        }
    }

    // Player attack
    public void playerAttack() {
        myCharacter.spaceIsPressed = true;
        myCharacter.attackAnimation(myPlayer, myCharacter.attack);

        if (collisionDetected(myPlayer, myCharacter, otherPlayer, otherCharacter)) {
            otherCharacter.lifepoints--;
            System.out.println("Other character LP: " + otherCharacter.lifepoints);
            otherPlayerLifepoints.setText(Integer.toString(otherCharacter.lifepoints));

            if (otherCharacter.lifepoints == 0) {
                playerWonTxt.setText("You win!");
                playerWonTxt.setVisible(true);
                startGameOverTimer();
            }
        }
    }

    private boolean collisionDetected (ImageView attackingPlayer, Character attackingCharacter, ImageView player2, Character otherCharacter) {

        boolean directionOk = checkDirection(attackingPlayer, attackingCharacter, player2);

        System.out.println("directionOk " + directionOk);

        if (directionOk) {
            Bounds player1Bounds = attackingPlayer.getBoundsInParent();
            Bounds player2Bounds = player2.getBoundsInParent();

            if (player2Bounds.intersects(player1Bounds.getMinX(), player1Bounds.getMinY(), player1Bounds.getWidth(), player1Bounds.getHeight())) {
                System.out.println("HIT");
                return true;
            }
        }
        return false;
    }

    private boolean checkDirection (ImageView attackingPlayer, Character attackingCharacter, ImageView player2) {
        if (attackingCharacter.direction > 0) {
            // Guckt nach rechts
            return attackingPlayer.getLayoutX() < player2.getLayoutX();
        } else {
            // Guckt nach links
            return attackingPlayer.getLayoutX() > player2.getLayoutX();
        }
    }

    private void startGameOverTimer () {
        countdown = 5;
        Timer gameOverTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (countdown > 0) {
                    countdown--;
                    System.out.println("Going back to lobby in " + countdown + "s");
                }
                if (countdown == 0) {

                    //todo send back to lobby
                    gameOverTimer.cancel();
                    gameOverTimer.purge();
                    exitGameBtn.setVisible(true);
                }
                System.out.println(countdown);
            }
        };
        gameOverTimer.schedule(task, 0, 1000);
    }

    // Stop player attack
    public void stopPlayerAttack() {
        myCharacter.spaceIsPressed = false;
    }

    // Exit game handler
    public void exitGame (javafx.event.ActionEvent event) throws IOException {
        client.statistics(event);

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost:8080/history");

            StringEntity input = new StringEntity("{" +
                    "\"userId\":\""+ Client.userID +"\"," +
                    "\"opponentId\":\"" + Client.opponentID + "\"," +
                    "\"userHp\":\""+ myPlayerLifepoints.getText() +"\"," +
                    "\"opponentHp\":\""+ otherPlayerLifepoints.getText() +"\"" +
                    "}");
            input.setContentType("application/json");
            postRequest.setEntity(input);

            RandomStringGenerator generator = new RandomStringGenerator.Builder()
                    .withinRange('0', 'z')
                    .filteredBy(DIGITS)
                    .build();

            String randomString = generator.generate(32);

            String secret = randomString.substring(0,1) + Client.userID + randomString.substring(2,28) + Client.opponentID + randomString.substring(29);

            postRequest.setHeader(HttpHeaders.AUTHORIZATION, Client.jwt);
            postRequest.setHeader("secret", secret);

            HttpResponse response = client.execute(postRequest);

            int responseStatus = response.getStatusLine().getStatusCode();

            System.out.println(responseStatus);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    // Move enemy player
    public void moveEnemy(int keyNumber) {
        if(otherIsMoving == false) {
            otherIsMoving = true;
            otherMoveTimer = new Timer();

            switch (keyNumber) {
                case 0:
                    otherCharacter.leftIsPressed = true;

                    otherCharacter.direction = -1;
                    setScale (otherPlayer, otherCharacter.direction, true);

                    otherCharacter.startAnimation(otherPlayer, otherCharacter.idleTimeline, otherCharacter.walk, "LEFT");
                    System.out.println("0: " + otherCharacter.walk);
                    otherMoveTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (otherPlayer.getLayoutX() < 0) {
                                otherPlayer.relocate(otherPlayer.getLayoutX(), otherPlayer.getLayoutY());
                            }
                            else {
                                otherPlayer.relocate(otherPlayer.getLayoutX()-10, otherPlayer.getLayoutY());
                            }
                        }
                    }, 50, 50);
                    break;
                case 1:
                    otherCharacter.rightIsPressed = true;

                    otherCharacter.direction = 1;
                    setScale (otherPlayer, otherCharacter.direction, true);

                    otherCharacter.startAnimation(otherPlayer, otherCharacter.idleTimeline, otherCharacter.walk, "RIGHT");
                    otherMoveTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            if (otherPlayer.getLayoutX() > stageWidth) {
                                otherPlayer.relocate(otherPlayer.getLayoutX(), otherPlayer.getLayoutY());
                            }
                            else {
                                otherPlayer.relocate(otherPlayer.getLayoutX()+10, otherPlayer.getLayoutY());
                            }
                        }
                    }, 50, 50);
                    break;
            }
        }
    }

    // Stop enemy movement
    public void stopEnemy() {
        if(otherIsMoving == true) {
            otherCharacter.rightIsPressed = false;
            otherCharacter.leftIsPressed = false;
            otherCharacter.walkIsActive = false;
            otherIsMoving = false;
            otherMoveTimer.cancel();
            otherCharacter.walkTimeline.stop();
            otherCharacter.idleTimeline.play();
        }
    }

    // Enemy attack
    public void enemyAttack(GameController controller) {
        otherCharacter.spaceIsPressed = true;
        otherCharacter.attackAnimation(otherPlayer, otherCharacter.attack);

        if (collisionDetected(otherPlayer, otherCharacter, myPlayer, myCharacter)) {
            myCharacter.lifepoints--;
            System.out.println("My Character LP: " + myCharacter.lifepoints);
            controller.myPlayerLifepoints.setText(Integer.toString(myCharacter.lifepoints));

            if (myCharacter.lifepoints == 0) {
                controller.playerWonTxt.setText("You lost!");
                controller.playerWonTxt.setVisible(true);
                startGameOverTimer();
            }
        }
    }

    // Stop enemy attack
    public void stopEnemyAttack() {
        otherCharacter.spaceIsPressed = false;
    }
}
