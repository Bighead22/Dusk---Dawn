import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


import java.util.ArrayList;


public class MyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private ArrayList<GameObject> activeObjects;
    private Player player;
    private PlayerWeapon playerWeapon;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Texture img;
    private ArrayList<Enemy> enemies;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(0.5f);
        font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        img = new Texture("assets\\backGroundv3.png");
        float worldWidth = 320; 
        float worldHeight = 180;
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        
        activeObjects = new ArrayList<GameObject>();
        

        player = new Dawn(0, 0, 20,100);
        activeObjects.add(player);

        playerWeapon = new PlayerWeapon( 25, 25, 10, 25, "assets/Weapon/laserF1.png", "assets/Weapon/laserF2.png");
        activeObjects.add(playerWeapon);

        
        // makes enemies and adds them to activeObjects

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
        try {
            Thread.sleep(33); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Boilerplate: Clear the screen to black each frame
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      

        double deltaTime = ((double) Gdx.graphics.getDeltaTime());

       



        // what happens in the game

        for(GameObject game : activeObjects){
            game.move(deltaTime);

            playerWeapon.updateAndAttack((int) player.getX(), (int) player.getY(), enemies);
            playerWeapon.visualHit((int) player.getX(), (int) player.getY());
            enemies.forEach(enemy -> enemy.getkilled());
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                player.setSpeed(100);
            }else{
                player.setSpeed(20);
            }
        }
        
        
        //Note: Anything drawn must be between .begin() and .end()
        batch.begin();

        


        batch.draw(img, 0, 0);
        // TODO 6: Write a loop to iterate through activeObjects and call draw(batch).
        for(GameObject game : activeObjects){
            game.draw(batch);
        }


        font.draw(batch, "Hello, LibGDX!", 50, 50);
        batch.end();

        for (int i = activeObjects.size() - 1; i >= 0; i--) {
            GameObject obj = activeObjects.get(i);
    
            // Check if it's past the 20,000 threshold
            if (obj.getX() > 20000 || obj.getY() > 20000) {
                activeObjects.remove(i);
        
                // 2. If the object was also an Enemy, remove it from the enemies list too
                if (obj instanceof Enemy) {
                    enemies.remove((Enemy) obj);
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
        font.dispose();
    }
}