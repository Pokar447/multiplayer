package multiplayer.client.characters;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Character factory pattern for instantiating different character types
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class Character {

    public Timeline idleTimeline = new Timeline();
    public Timeline walkTimeline = new Timeline();
    public Timeline attackTimeline = new Timeline();

    public Image[] idle;
    public Image[] walk;
    public Image[] attack;

    public int direction;

    public int lifepoints = 5;

    public boolean leftIsPressed = false;
    public boolean rightIsPressed = false;
    public boolean spaceIsPressed = false;
    public boolean walkIsActive = false;
    public boolean attackIsActive = false;

    /**
     * Creates characters based on the given string
     *
     * @param type Character type
     */
    public void createCharacter(String type) {
        switch(type) {
            case "GOLEM":
                Golem golem = new Golem();
                idle = golem.getIdle();
                walk = golem.getWalk();
                attack = golem.getAttack();
                System.out.println("GOLEM");
                break;
            case "MINOTAUR":
                Minotaur minotaur = new Minotaur();
                idle = minotaur.getIdle();
                walk = minotaur.getWalk();
                attack = minotaur.getAttack();
                System.out.println("MINOTAUR");
                break;
            case "SATYR":
                Satyr satyr = new Satyr();
                idle = satyr.getIdle();
                walk = satyr.getWalk();
                attack = satyr.getAttack();
                System.out.println("SATYR");
                break;
            case "WRAITH":
                Wraith wraith = new Wraith();
                idle = wraith.getIdle();
                walk = wraith.getWalk();
                attack = wraith.getAttack();
                System.out.println("WRAITH");
                break;
            default:
                System.out.println("createCharacter() in Character.java");
                break;
        }
    }

    /**
     * Starts the countdown before the game starts
     *
     * @param player Imageview of the player
     * @param idle Idle timeline
     * @param  state Image array
     */
    public void idleAnimation(ImageView player, Timeline idle, Image[] state) {
        if(attackIsActive == false) {
            stopAnimations(idle);
            idleTimeline.setCycleCount(Timeline.INDEFINITE);

            for(int i = 0; i < state.length; i++) {
                int finalI = i;
                if (i == 0) {
                    idleTimeline.getKeyFrames().add(new KeyFrame(
                            Duration.millis(i),
                            (ActionEvent event) -> {
                                player.setImage(state[finalI]);
                            }
                    ));
                } else {
                    idleTimeline.getKeyFrames().add(new KeyFrame(
                            Duration.millis(i * 100),
                            (ActionEvent event) -> {
                                player.setImage(state[finalI]);
                            }
                    ));
                }
            }

            idleTimeline.play();
        }
    }

    /**
     * Starts an animation
     *
     * @param player Imageview of the player
     * @param idle Idle timeline
     * @param  state Image array
     * @param direction Direction the player faces
     */
    public void startAnimation(ImageView player, Timeline idle, Image[] state, String direction) {
        System.out.println("1: " + state);
        if(walkIsActive == false && attackIsActive == false) {
            stopAnimations(idle);
            walkTimeline.setCycleCount(Timeline.INDEFINITE);

            if(direction == "left") {
                for(int i = 18; i > state.length; i--) {
                    int finalI = i;
                    if (i == 0) {
                        walkTimeline.getKeyFrames().add(new KeyFrame(
                                Duration.millis(i),
                                (ActionEvent event) -> {
                                    player.setImage(state[finalI]);
                                }
                        ));
                    } else {
                        walkTimeline.getKeyFrames().add(new KeyFrame(
                                Duration.millis(i * 50),
                                (ActionEvent event) -> {
                                    player.setImage(state[finalI]);
                                }
                        ));
                    }
                }
            }
            else {
                for(int i = 0; i < state.length; i++) {
                    int finalI = i;
                    if (i == 0) {
                        walkTimeline.getKeyFrames().add(new KeyFrame(
                                Duration.millis(i),
                                (ActionEvent event) -> {
                                    player.setImage(state[finalI]);
                                }
                        ));
                    } else {
                        walkTimeline.getKeyFrames().add(new KeyFrame(
                                Duration.millis(i * 50),
                                (ActionEvent event) -> {
                                    player.setImage(state[finalI]);
                                }
                        ));
                    }
                }
            }
        }

        if(leftIsPressed == true || rightIsPressed == true) {
            if(walkIsActive == false) {
                walkIsActive = true;
                walkTimeline.play();
            }
        }

    }

    /**
     * Starts the attack animation
     *
     * @param player Imageview of the player
     * @param  state Image array
     */
    public void attackAnimation(ImageView player, Image[] state) {
        stopAnimations(idleTimeline);
        attackIsActive = true;
        attackTimeline.setCycleCount(1);

        for(int i = 0; i < state.length; i++) {
            int finalI = i;
            if (i == 0) {
                attackTimeline.getKeyFrames().add(new KeyFrame(
                        Duration.millis(i),
                        (ActionEvent event) -> {
                            player.setImage(state[finalI]);
                        }
                ));
            } else {
                attackTimeline.getKeyFrames().add(new KeyFrame(
                        Duration.millis(i * 50),
                        (ActionEvent event) -> {
                            player.setImage(state[finalI]);
                        }
                ));
            }
        }

        attackTimeline.play();

        attackTimeline.setOnFinished(event -> {
            if (spaceIsPressed == true) {
                attackAnimation(player, attack);
            } else {
                attackIsActive = false;
                if(walkIsActive == true) {
                    walkTimeline.play();
                } else {
                    idleTimeline.play();
                }
            }
        });
    }

    /**
     * Stops an animation and starts the idle animation
     *
     * @param idle Idle timeline
     */
    public void stopAnimations(Timeline idle) {
        idle.stop();
        walkTimeline.stop();
    }

}
