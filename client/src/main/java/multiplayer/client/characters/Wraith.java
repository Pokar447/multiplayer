package multiplayer.client.characters;

import javafx.scene.image.Image;

/**
 * Wraith character for factory pattern
 *
 * @author      Nora Kühnel <nora.kuhnel@stud.th-luebeck.de>
 * @author      Jorn Ihlenfeldt <<jorn.ihlenfeldt@stud.th-luebeck.de>
 *
 * @version     1.0
 */
public class Wraith {

    Image[] idle = new Image[12];
    Image[] walk = new Image[12];
    Image[] attack = new Image[10];

    /**
     * Stores the images for the idle animation in an array
     *
     * @return idle Idle image array
     */
    public Image[] getIdle() {
        for(int i = 0; i < 12; i++) {
            idle[i] = new Image(getClass().getResource("/multiplayer.client/graphics/wraith/Idle/Wraith_03_Idle_" + i + ".png").toString());
        }
        return idle;
    }

    /**
     * Stores the images for the walk animation in an array
     *
     * @return walk Walk image array
     */
    public Image[] getWalk() {
        for(int i = 0; i < 12; i++) {
            walk[i] = new Image(getClass().getResource("/multiplayer.client/graphics/wraith/Walking/Wraith_03_Walking_" + i + ".png").toString());
        }
        return walk;
    }

    /**
     * Stores the images for the attack animation in an array
     *
     * @return attack Attack image array
     */
    public Image[] getAttack() {
        for(int i = 0; i < 10; i++) {
            attack[i] = new Image(getClass().getResource("/multiplayer.client/graphics/wraith/Attacking/Wraith_03_Attack_" + i + ".png").toString());
        }
        return attack;
    }

}