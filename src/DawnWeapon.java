import com.badlogic.gdx.math.Rectangle;

public class DawnWeapon extends Weapon {

    private double delayCounter = 0;
    private double attackSpeed = 100;
    private Rectangle pHitbox = DawnWeapon.this.getHitbox();

    public DawnWeapon(int width, int height, int damage, double attackSpeed, String frame1, String frame2) {
        super(width, height, damage, attackSpeed, frame1, frame2);
        this.attackSpeed = attackSpeed;
    }


}
