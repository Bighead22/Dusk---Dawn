import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player extends GameObject {

    private int speed = 200;
    
    public Player(double x, double y){
        super(x, y, 50, 50, "assets\\ship.png");
    }

    @Override
    public void move(double deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            setX(getX() - (speed * deltaTime));
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            setX(getX() + (speed * deltaTime));
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            setY(getY() + (speed * deltaTime));
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            setY(getY() - (speed * deltaTime));
        }

    }

}