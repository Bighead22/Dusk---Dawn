import com.badlogic.gdx.Gdx;

public class Player extends GameObject {

    private double delayCounter = 0;
    private double walkAnimationSpeed;
    private int speed;
    
    public Player(int x, int y, int width, int height, String imagePath, int speed){
        super(x, y, width, height, imagePath);
        this.speed = speed;
        this.walkAnimationSpeed = 1.0 / (double) (speed);
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
