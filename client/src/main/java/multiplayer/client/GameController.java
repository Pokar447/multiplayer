package multiplayer.client;

// Java imports
import java.net.*;
import java.util.*;

// JavaFX imports
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import multiplayer.client.characters.Character;

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

    // Move Timer
    public Timer myMoveTimer;
    public Timer otherMoveTimer;

    //width & height of the usable stage for collision detection
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
        } else {
            myPlayer = player2;
            otherPlayer = player1;
            myChoice = client.getMyCharacter();
            otherChoice = client.getOtherCharacter();
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

    // Move player
    public void movePlayer(String key) {
        if(myIsMoving == false) {
            myIsMoving = true;
            myMoveTimer = new Timer();

            switch (key) {
                case "LEFT":
                    myCharacter.leftIsPressed = true;
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

        if (collisionDetected(myPlayer, otherPlayer)) {
            otherCharacter.lifepoints--;
            System.out.println("Other character LP: " + otherCharacter.lifepoints);
            otherPlayerLifepoints.setText(Integer.toString(otherCharacter.lifepoints));

            if (otherCharacter.lifepoints == 0) {
                playerWonTxt.setText("You win!");
                playerWonTxt.setVisible(true);
            }
        }
    }

    private boolean collisionDetected (ImageView player1, ImageView player2) {

        Bounds player1Bounds = player1.getBoundsInParent();
        Bounds player2Bounds = player2.getBoundsInParent();

        if (player2Bounds.intersects(player1Bounds.getMinX(), player1Bounds.getMinY(), player1Bounds.getWidth()/3, player1Bounds.getHeight())) {
            System.out.println("HIT");
            return true;
        }
        return false;
    }

    // Stop player attack
    public void stopPlayerAttack() {
        myCharacter.spaceIsPressed = false;
    }

    // Move enemy player
    public void moveEnemy(int keyNumber) {
        if(otherIsMoving == false) {
            otherIsMoving = true;
            otherMoveTimer = new Timer();

            switch (keyNumber) {
                case 0:
                    otherCharacter.leftIsPressed = true;
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

        if (collisionDetected(otherPlayer, myPlayer)) {
            myCharacter.lifepoints--;
            System.out.println("My Character LP: " + myCharacter.lifepoints);
            controller.myPlayerLifepoints.setText(Integer.toString(myCharacter.lifepoints));

            if (myCharacter.lifepoints == 0) {
                controller.playerWonTxt.setText("You lost!");
                controller.playerWonTxt.setVisible(true);
            }
        }
    }

    // Stop enemy attack
    public void stopEnemyAttack() {
        otherCharacter.spaceIsPressed = false;
    }
}
