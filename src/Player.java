import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Player extends GameObject {

    private int speed = 200;
    
    public Player(int x, int y, String imagePath){
        super(x, y, 50, 50, imagePath);
    }

}