import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class GameObject {
    // AP Standard: private instance variables
    private double x;
    private double y;
   

    private int width;
    private int height;
    private Texture image;
    private Rectangle hitbox;

    // AP Standard: constructors
    public GameObject(double x, double y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = new Texture(imagePath);
        
        
        this.hitbox = new Rectangle((int) x, (int) y, width, height);
    }


    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public void setWidth(int width){
        this.width = width;
        hitbox.setSize(width, height);
    }
    public void setHeight(int height){
        this.height = height;
        hitbox.setSize(width, height);
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public Rectangle getHitbox(){
        return hitbox;
    }
    

   

    public void setX(double x){
        this.x = x;
        hitbox.setPosition((int) x, (int) y);
    }

    public void setY(double y){
        this.y = y;
        hitbox.setPosition((int) x, (int) y);
    }

    public void setTexture(String imagePath) {

    this.image.dispose(); 
    this.image = new Texture(imagePath);
    }


    public void draw(SpriteBatch batch) {
        batch.draw(image, (int) x, (int) y, width, height);
    }


    public void move(double deltaTime) {

    }
}