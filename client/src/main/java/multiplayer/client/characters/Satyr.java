package multiplayer.client.characters;

import javafx.scene.image.Image;

/**
 * Satyr character for factory pattern
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class Satyr {

    Image[] idle = new Image[12];
    Image[] walk = new Image[18];
    Image[] attack = new Image[11];

    /**
     * Stores the images for the idle animation in an array
     *
     * @return idle Idle image array
     */
    public Image[] getIdle() {
        for(int i = 0; i < 12; i++) {
            idle[i] = new Image(getClass().getResource("/multiplayer.client/graphics/satyr/Idle/Satyr_02_Idle_" + i + ".png").toString());
        }
        return idle;
    }

    /**
     * Stores the images for the walk animation in an array
     *
     * @return walk Walk image array
     */
    public Image[] getWalk() {
        for(int i = 0; i < 18; i++) {
            walk[i] = new Image(getClass().getResource("/multiplayer.client/graphics/satyr/Walking/Satyr_02_Walking_" + i + ".png").toString());
        }
        return walk;
    }

    /**
     * Stores the images for the attack animation in an array
     *
     * @return attack Attack image array
     */
    public Image[] getAttack() {
        for(int i = 0; i < 11; i++) {
            attack[i] = new Image(getClass().getResource("/multiplayer.client/graphics/satyr/Attacking/Satyr_02_Attacking_" + i + ".png").toString());
        }
        return attack;
    }

}