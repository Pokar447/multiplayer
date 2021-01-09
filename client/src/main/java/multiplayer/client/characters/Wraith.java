package multiplayer.client.characters;

import javafx.scene.image.Image;

public class Wraith {

    Image[] idle = new Image[12];
    Image[] walk = new Image[12];
    Image[] attack = new Image[10];

    public Image[] getIdle() {
        for(int i = 0; i < 12; i++) {
            idle[i] = new Image(getClass().getResource("/multiplayer.client/graphics/wraith/Idle/Wraith_03_Idle_" + i + ".png").toString());
        }
        return idle;
    }

    public Image[] getWalk() {
        for(int i = 0; i < 12; i++) {
            walk[i] = new Image(getClass().getResource("/multiplayer.client/graphics/wraith/Walking/Wraith_03_Walking_" + i + ".png").toString());
        }
        return walk;
    }

    public Image[] getAttack() {
        for(int i = 0; i < 10; i++) {
            attack[i] = new Image(getClass().getResource("/multiplayer.client/graphics/wraith/Attacking/Wraith_03_Attack_" + i + ".png").toString());
        }
        return attack;
    }

}