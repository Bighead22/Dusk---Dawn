import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class Weapon extends GameObject{

    private int damage;
    private double delayCounter = 0;
    private double attackSpeed; // 2 seconds
    private String frame2;
    private String frame1;

    public Weapon(int width, int height, int damage, double attackSpeed, String frame1, String frame2) {
        super(10000, 10000, width, height, frame1);
        this.frame1 = frame1;
        this.frame2 = frame2;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }
    public int getDamage(){
        return damage;
    }
    public void setDamage(int damage){
        this.damage = damage;
    }
    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
    public double getAttackSpeed() {
        return attackSpeed;
    }


    public void visualHit(int x, int y) {
    delayCounter += Gdx.graphics.getDeltaTime();

    if (delayCounter < attackSpeed / 8) {
        setX(x-13);
        setY(y-7);
        setTexture(frame1);
    } 
    else if (delayCounter < attackSpeed / 4) {
        setX(x-13);
        setY(y-7);
        setTexture(frame2);
    } 
    else if (delayCounter < attackSpeed) {
        setX(10000);
        setY(10000);
    } 
    else {
        delayCounter = 0;
    }
}
    public void updateAndAttack(int x, int y, ArrayList<Enemy> enemies) {
    }

}
