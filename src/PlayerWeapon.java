import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class PlayerWeapon extends Weapon {

    private int damage;
    private double delayCounter = 0;
    private double attackSpeed = 2.0; // 2 seconds
    private Rectangle pHitbox = PlayerWeapon.this.getHitbox();

    public PlayerWeapon(int width, int height, String imagePath, int damage, double attackSpeed) {
        super(width, height, imagePath, damage, attackSpeed);
    }

    public void Hit(int x, int y){
        delayCounter += Gdx.graphics.getDeltaTime();
        setX(10000);
        setY(10000);
        if (delayCounter >= attackSpeed) {
            setX(x);
            setY(y);
            delayCounter = 0; 
        }
        
    }
    
}
