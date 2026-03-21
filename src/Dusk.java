import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Dusk extends Player {

    
    

    public Dusk(int x, int y, int speed, int health) {
        super(x, y, 9, 19, "assets/duskAssets/DuskI.png", speed, health);
    }

    @Override
    public void move(double deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            setX(getX() - (getSpeed() * deltaTime));
            
            walkAnimation("assets/duskAssets/duskLeftL.png", "assets/duskAssets/duskLeftR.png");
            
        }else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            setX(getX() + (getSpeed() * deltaTime));
            
             walkAnimation("assets/duskAssets/duskRightL.png", "assets/duskAssets/duskRightR.png");

        }else{
            setTexture("assets/duskAssets/DuskI.png");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            setY(getY() + (getSpeed() * deltaTime));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            setY(getY() - (getSpeed() * deltaTime));
        }

    }

}