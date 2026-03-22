import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;


public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private ArrayList<GameObject> activeObjects;
    private Dawn dusk;
    private PlayerWeapon playerWeapon;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Texture img;
    private ArrayList<Enemy> enemies;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        img = new Texture("assets\\\\backGroundv3.png");
        float worldWidth = 320; 
        float worldHeight = 180;
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        
        activeObjects = new ArrayList<GameObject>();
        
        // TODO 3: Instantiate your Player subclass and add it to activeObjects.

        dusk = new Dawn(0, 0, 20,100);
        activeObjects.add(dusk);

        playerWeapon = new PlayerWeapon( 20, 20, "assets\\fish_pink.png", 10, 0.1);
        activeObjects.add(playerWeapon);

        // TODO 4: Write a for-loop to instantiate 5 Enemy objects at different 
        //         starting Y-coordinates and add them to activeObjects.
        enemies = new ArrayList<Enemy>();

        for (int i = 0; i < 10; i++) {
            // Create an enemy with some offset so they aren't all on top of each other
            int x = 100 + (i * 60); 
            int y = 100;
            
            Enemy newEnemy = new Enemy(x, y, 50, 50, "assets\\fish_pink.png", 2, 100);
            
            // Add the new enemy to the array
            enemies.add(newEnemy);
            activeObjects.add(newEnemy);
        }
    }

    //render() is the game loop, called approx 60 times per second
    @Override
    public void render() {
        // Boilerplate: Clear the screen to black each frame
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // --- AP REVIEW: CASTING ---
        // Gdx.graphics.getDeltaTime() returns a float. 
        // We cast it to a double to stay strictly within the AP CSA Java standards.
        double deltaTime = ((double) Gdx.graphics.getDeltaTime());

        // --- AP REVIEW: POLYMORPHISM ---
        // TODO 5: Write a standard or enhanced for-loop to iterate through activeObjects.
        // For each object, call its move() method.

        for(GameObject game : activeObjects){
            game.move(deltaTime);
            playerWeapon.updateAndAttack((int) dusk.getX(), (int) dusk.getY(), enemies);
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                dusk.setSpeed(100);
            }else{
                dusk.setSpeed(20);
            }
        }
        
        
        //Note: Anything drawn must be between .begin() and .end()
        batch.begin();
        batch.draw(img, 0, 0);
        // TODO 6: Write a loop to iterate through activeObjects and call draw(batch).
        for(GameObject game : activeObjects){
            game.draw(batch);
        }


        batch.end();

        // --- AP REVIEW: ARRAYLIST TRAVERSAL & REMOVAL ---
        // TODO 7: Write collision logic. 
        // You must iterate through the list to check if the player overlaps with enemies.
        // See the cheat sheet for the overlap method!
        // NOTE: If you are removing items from an ArrayList, how must you structure 
        // your for-loop to avoid skipping elements?

        //write how to hit thing here

    }
    @Override
    public void resize(int width, int height) {
    // 3. This is CRITICAL for the stretch to happen
    viewport.update(width, height, true); 
    }
    
    @Override
    public void dispose() {
        batch.dispose();
    }
}