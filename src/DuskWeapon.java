import java.util.ArrayList;

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
                setX(x-5);
                setY(y-7);
                setTexture("assets/Weapon/DuskSlashR.png");
            }
            if ((Gdx.input.getX() < x*4.0625)){
                setX(x-19);
                setY(y-7);
                setTexture("assets/Weapon/DuskSlashL.png");
            }
        }else{
            setX(10000);
        }
    }
    @Override
    public void updateAndAttack(int x, int y, ArrayList<Enemy> enemies) {
    delayCounter += Gdx.graphics.getDeltaTime();

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            if ((Gdx.input.getX() > x*4.0625)){
                setX(x-5);
                setY(y-7);
                for (Enemy e : enemies) {
                    if (this.getHitbox().overlaps(e.getHitbox())) {
                        e.setHealth(e.getHealth() - this.getDamage()/5);
                        System.out.println("Hit enemy! Health: " + e.getHealth());

                    }
                }
                setTexture("assets/Weapon/DuskSlashR.png");
            }
            if ((Gdx.input.getX() < x*4.0625)){
                setX(x-19);
                setY(y-7);
                for (Enemy e : enemies) {
                    if (this.getHitbox().overlaps(e.getHitbox())) {
                        e.setHealth(e.getHealth() - this.getDamage());
                        System.out.println("Hit enemy! Health: " + e.getHealth());

                    }
                }
                setTexture("assets/Weapon/DuskSlashL.png");
            }
        }else{
            setX(10000);
        }
    }
}



