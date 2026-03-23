import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;


public class EnemyWeapon extends Weapon {

    
    private double delayCounter = 0;
    private double attackSpeed = 100;
    private Rectangle pHitbox = EnemyWeapon.this.getHitbox();

    public EnemyWeapon(int width, int height, int damage, double attackSpeed, String frame1, String frame2) {
        super(width, height, damage, attackSpeed, frame1, frame2);
        this.attackSpeed = attackSpeed;
        
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
    public double getAttackSpeed() {
        return attackSpeed;
    }


    public void updateAndAttack(int x, int y, Player player) {
    delayCounter += Gdx.graphics.getDeltaTime();

    // 1. Move weapon off-screen by default (hiding it)
    setX(10000);
    setY(10000);

    // 2. Only "Attack" if the cooldown is over
    if (delayCounter >= this.attackSpeed) {
        // Move the weapon to the mouse/player position
        setX(x);
        setY(y);

      
      
            if (this.getHitbox().overlaps(player.getHitbox())) {
                player.setHealth(player.getHealth() - this.getDamage());
                
            }
        

        // 4. Reset timer AFTER checking all enemies
        delayCounter = 0;
    }
    }
    
    
}
