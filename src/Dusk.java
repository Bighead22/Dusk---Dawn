import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class Dusk extends Player{
    private Rectangle hitbox;

    public Dusk(int x, int y, int speed, int health) {
        super(x, y, 8, 19, "assets/DuskAssets/DuskI.png", speed, health);
        this.hitbox = new Rectangle((int) x, (int) y, 5, 5);
    }
    @Override
    public Rectangle getHitbox(){
        return hitbox;
    }
    @Override
    public void move(double deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            setX(getX() - (getSpeed() * deltaTime));
            walkAnimation("assets/DuskAssets/DuskLeftL.png", "assets/DuskAssets/DuskLeftR.png");
        }else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            setX(getX() + (getSpeed() * deltaTime));
             walkAnimation("assets/DuskAssets/DuskRightL.png", "assets/DuskAssets/DuskRightR.png");
        }else{
            setTexture("assets/DuskAssets/DuskI.png");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            setY(getY() + (getSpeed() * deltaTime));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            setY(getY() - (getSpeed() * deltaTime));
        }
    }
    
}
