import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

public class Player extends GameObject {

    private double delayCounter = 0;
    private double walkAnimationSpeed;
    private int speed;
    private int health;
    private int xP;
    
    public Player(int x, int y, int width, int height, String imagePath, int speed, int health) {
        super(x, y, width, height, imagePath);
        this.speed = speed;
        this.walkAnimationSpeed = 1.0 / (double) (speed);
        this.health = health;
        this.xP=0;
    }
    public void move(){
    }
    public void ablity(ArrayList<Enemy> enemies, double deltaTime){
    }
    public int getxP() {
        return xP;
    }
    public void setxP(int xP) {
        this.xP = xP;
    }
    public void setSpeed(int speed){
        this.speed = speed;
    }
    public double getWalkAnimationSpeed(){
        return walkAnimationSpeed;
    }
    public int getSpeed(){
        return speed;
    }
    public int getHealth(){
        return health;
    }
    public void setHealth(int health){
        this.health = health;
    }

    public void walkAnimation(String f1, String f2) {
    delayCounter += Gdx.graphics.getDeltaTime();

        if (delayCounter < walkAnimationSpeed) {
            // First half of the animation
            setTexture(f1);
        } else if (delayCounter < walkAnimationSpeed * 2) {
            // Second half of the animation
            setTexture(f2);
        } else {
            // Reset the cycle
            delayCounter = 0;
        }
    }
    
}
