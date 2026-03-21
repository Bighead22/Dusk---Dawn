import com.badlogic.gdx.Gdx;

public class Weapon extends GameObject{

    private int damage;
    private double delayCounter = 0;
    private double attackSpeed = 2.0; // 2 seconds

    public Weapon(int x, int y, int width, int height, String imagePath, int damage, double attackSpeed){
        super(x, y, width, height, imagePath);
        this.damage = damage;
        this.attackSpeed = attackSpeed;
    }
    public int getDamage(){
        return damage;
    }
    public void setDamage(int damage){
        this.damage = damage;
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
