import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;


public class PlayerWeapon extends Weapon {

    private int damage;
    private double delayCounter = 0;
    private double attackSpeed; // 2 seconds
    private Rectangle pHitbox = PlayerWeapon.this.getHitbox();

    public PlayerWeapon(int width, int height, String imagePath, int damage, double attackSpeed) {
        super(width, height, imagePath, damage, attackSpeed);
    }

    public void updateAndAttack(int x, int y, ArrayList<Enemy> enemies) {
    delayCounter += Gdx.graphics.getDeltaTime();

    // 1. Move weapon off-screen by default (hiding it)
    setX(10000);
    setY(10000);

    // 2. Only "Attack" if the cooldown is over
    if (delayCounter >= attackSpeed) {
        // Move the weapon to the mouse/player position
        setX(x);
        setY(y);

        // 3. Check every enemy for a collision
        for (Enemy e : enemies) {
            if (this.getHitbox().overlaps(e.getHitbox())) {
                e.setHealth(e.getHealth() - this.getDamage());
                System.out.println("Hit enemy! Health: " + e.getHealth());
            }
        }

        // 4. Reset timer AFTER checking all enemies
        delayCounter = 0;
    }
    }
    
}
