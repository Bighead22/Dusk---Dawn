import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Dusk extends GameObject {

    private int speed = 200;
    
    public Dusk(int x, int y){
        super(x, y, 27, 51, "assets\\dusk.png");
        
    }
    
    @Override
    public void move(double deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            setX(getX() - (speed * deltaTime));
            setTexture("assets\\DuskLeft.png");
        }else{
            setTexture("assets\\dusk.png");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            setX(getX() + (speed * deltaTime));
            setTexture("assets\\DuskRight.png");
        }else{
            setTexture("assets\\dusk.png");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            setY(getY() + (speed * deltaTime));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            setY(getY() - (speed * deltaTime));
        }

    }

}