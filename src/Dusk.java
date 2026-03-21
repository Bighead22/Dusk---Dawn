import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Dusk extends Player {

    
    

    public Dusk(int x, int y, int speed){
        super(x, y, 9, 19, "assets\\dusk.png", speed);
    }

    @Override
    public void move(double deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            setX(getX() - (getSpeed() * deltaTime));
            
            walkAnimation("assets\\duskLeftL.png", "assets\\duskLeftR.png");
            
        }else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            setX(getX() + (getSpeed() * deltaTime));
            
            

        }else{
            setTexture("assets\\dusk.png");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            setY(getY() + (getSpeed() * deltaTime));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            setY(getY() - (getSpeed() * deltaTime));
        }

    }

}