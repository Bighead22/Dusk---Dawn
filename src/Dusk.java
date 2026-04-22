import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class Dusk extends Player{

    public Dusk(int x, int y, int speed, int health) {
        super(x, y, 9, 19, "assets/DuskAssets/DuskI.png", speed, health);
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
    @Override
    public void ablity(ArrayList<Enemy> enemies,double deltaTime){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && Gdx.input.isKeyPressed(Input.Keys.A)){
            setTexture("assets/DuskAssets/DuskDashL.png");
            setX(getX() - 5);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && Gdx.input.isKeyPressed(Input.Keys.D)){
            setTexture("assets/DuskAssets/DuskDashR.png");
            setX(getX() + 5);
        }
    }

}
