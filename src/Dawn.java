import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Dawn extends Player {

    
    

    public Dawn(int x, int y, int speed, int health) {
        super(x, y, 9, 19, "assets/DawnAssets/DawnI.png", speed, health);
    }

    @Override
    public void move(double deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            setX(getX() - (getSpeed() * deltaTime));
            walkAnimation("assets/DawnAssets/DawnLeftL.png", "assets/DawnAssets/DawnLeftR.png");
        }else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            setX(getX() + (getSpeed() * deltaTime));
             walkAnimation("assets/DawnAssets/DawnRightL.png", "assets/DawnAssets/DawnRightR.png");
        }else{
            setTexture("assets/DawnAssets/DawnI.png");
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
        if (this.getHealth() > 1) {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                this.setHealth(1);
                for (Enemy e : enemies) {
                    e.setHealth(-2147483647);
                    System.out.println("Hit enemy! Health: " + e.getHealth());
                }
            }
        }
    }

}