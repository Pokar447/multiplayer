package multiplayer.client.characters;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Character {

    // Player Timelines
    public Timeline idleTimeline = new Timeline();
    public Timeline walkTimeline = new Timeline();
    public Timeline attackTimeline = new Timeline();

    // Character Images
    public Image[] idle;
    public Image[] walk;
    public Image[] attack;

    public int lifepoints = 5;

    // State Helper
    public boolean leftIsPressed = false;
    public boolean rightIsPressed = false;
    public boolean spaceIsPressed = false;
    public boolean walkIsActive = false;
    public boolean attackIsActive = false;

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

    // Default idle animation
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

    // Walk animation
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

    // Attack animation
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

    // Stop animations
    public void stopAnimations(Timeline idle) {
        idle.stop();
        walkTimeline.stop();
    }

}
