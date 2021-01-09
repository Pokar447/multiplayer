package multiplayer.client.characters;

import javafx.scene.image.Image;

public class Satyr {

    Image[] idle = new Image[12];
    Image[] walk = new Image[18];
    Image[] attack = new Image[11];

    public Image[] getIdle() {
        for(int i = 0; i < 12; i++) {
            idle[i] = new Image(getClass().getResource("/multiplayer.client/graphics/satyr/Idle/Satyr_02_Idle_" + i + ".png").toString());
        }
        return idle;
    }

    public Image[] getWalk() {
        for(int i = 0; i < 18; i++) {
            walk[i] = new Image(getClass().getResource("/multiplayer.client/graphics/satyr/Walking/Satyr_02_Walking_" + i + ".png").toString());
        }
        return walk;
    }

    public Image[] getAttack() {
        for(int i = 0; i < 11; i++) {
            attack[i] = new Image(getClass().getResource("/multiplayer.client/graphics/satyr/Attacking/Satyr_02_Attacking_" + i + ".png").toString());
        }
        return attack;
    }

}