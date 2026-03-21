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
    private Dusk dusk;
    private Weapon weapon;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Texture img;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        img = new Texture("assets\\\\backGround.png");
        float worldWidth = 320; 
        float worldHeight = 180;
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        
        activeObjects = new ArrayList<GameObject>();
        
        // TODO 3: Instantiate your Player subclass and add it to activeObjects.

        dusk = new Dusk(0, 0);
        activeObjects.add(dusk);
        weapon = new Weapon(10000, 10000, 20, 20, "assets\\fish_pink.png", 10, 0.1);
        activeObjects.add(weapon);

        // TODO 4: Write a for-loop to instantiate 5 Enemy objects at different 
        //         starting Y-coordinates and add them to activeObjects.
        int startingY = 100;
        for(int i = 0; i < 5; i++){
            activeObjects.add(new Enemy(400, startingY + (60 * i), 50, 50, "assets\\fish_pink.png"));
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
            weapon.Hit((int) dusk.getX(), (int) dusk.getY());
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                
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

        for(int i = activeObjects.size() - 1; i >= 0; i--){
            if(activeObjects.get(i) instanceof Enemy){
                if(dusk.getHibox().overlaps(activeObjects.get(i).getHibox())){
                    activeObjects.remove(i);
                } 
            }
        }

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