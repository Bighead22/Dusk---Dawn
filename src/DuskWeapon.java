import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class DuskWeapon extends Weapon {

    private double delayCounter = 0;
    private double attackSpeed = 100;
    private Rectangle pHitbox = DuskWeapon.this.getHitbox();

    public DuskWeapon(int width, int height, int damage, double attackSpeed, String frame1, String frame2) {
        super(width, height, damage, attackSpeed, frame1, frame2);
        this.attackSpeed = attackSpeed;
    }
    @Override
    public void visualHit(int x, int y) {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if ((Gdx.input.getX() > x*4.0625)){
                setX(x+8);
                setY(y-3);
                setTexture("assets/Weapon/DuskSlashR.png");
            }
            if ((Gdx.input.getX() < x*4.0625)){
                setX(x-20);
                setY(y-3);
                setTexture("assets/Weapon/DuskSlashL.png");
            }
        }else{
            setX(10000);
        }
    }


}
