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
    
    private ArrayList<GameObject> objectsToAdd = new ArrayList<>();
    private SpriteBatch batch;
    private ArrayList<GameObject> activeObjects;
    
    private FitViewport viewport;
    private OrthographicCamera camera;
    private Texture img;
    
    private BitmapFont font;
    private int framerate = 60;
    private int randomX = (int)(Math.random() * 320);
    private int randomY = (int)(Math.random() * 180);
    private boolean hasGameStarted = false;
    private boolean hasGameEnded = false;

    private ArrayList<Enemy> enemies;
    private int enemyCount = 5;
    private int enemySpeed = 2;
    private int enemyHealth = 100;
    private int enemyAttackDamage = 15;

    private Player player;
    private PlayerWeapon playerWeapon;
    private int health = 150;
    private int score = 0;
    private int level = 0;
    private int xpThreshold = 100;
    private int attackDamage = 20;
    private int attackRange = 35;
    private int attackCooldown = 10;
    private boolean isLevelingUp = false;
    
    private float time = 1.0f;
    

    @Override
    public void create() {
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(0.4f);
        font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        img = new Texture("assets\\backGroundv4.png");
        float worldWidth = 320; 
        float worldHeight = 180;
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        
        activeObjects = new ArrayList<GameObject>();
        

        player = new Dawn(0, 0, 20,health);
        activeObjects.add(player);

        playerWeapon = new PlayerWeapon( attackRange, attackRange, attackDamage, attackCooldown, "assets/Weapon/explosionF1.png", "assets/Weapon/explosionF2.png");
        activeObjects.add(playerWeapon);

        
        // makes enemies and adds them to activeObjects

        enemies = new ArrayList<Enemy>();

        spawnEnemies(enemyCount);
        
    }

    //render() is the game loop, called approx 60 times per second
    @Override
    public void render() {
        
        
        // Boilerplate: Clear the screen to black each frame
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (player.getHealth() <= 0) {
            hasGameEnded = true;
        }

        if (hasGameEnded) {
            // 3. Draw Lose Screen
            batch.begin();
            batch.draw(img, 0, 0); // Draw background
            font.draw(batch, "GAME OVER", 130, 100);
            font.draw(batch, "Press R to Restart", 120, 80);
            batch.end();

            // Restart logic
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                // Reset health and state (you might want a full reset method)
                resetGame();
                hasGameEnded = false;
            }
        } else {

            double deltaTime = ((double) Gdx.graphics.getDeltaTime() * time);

            if (isLevelingUp) {
                LevelUp();
            }

            // what happens in the game
            for(GameObject game : activeObjects){
                game.move(deltaTime);

                playerWeapon.updateAndAttack((int) player.getX(), (int) player.getY(), enemies);
                playerWeapon.visualHit((int) player.getX(), (int) player.getY());
            
            
                enemies.forEach(enemy -> enemy.getkilled());
                enemies.forEach(enemy -> enemy.move(deltaTime, player));
                enemies.forEach(enemy -> enemy.attack(player));

                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                player.setSpeed(100);
                }else{
                player.setSpeed(20);
                }
            }

            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
            
                if (enemy.getHealth() <= 0) {

                    activeObjects.remove(enemy);
                    enemies.remove(i);

                    // Reset enemy instead of removing for now to keep it simple
                    enemy.setHealth(100);
                    enemy.setX((float)Math.random() * 320); // Teleport him so he doesn't stay on player
                    enemy.setY((float)Math.random() * 180);

                    player.setxP(player.getxP() + 20);

                    if (player.getxP() >= xpThreshold) {
                        isLevelingUp = true;
                        time = 0.05f;
                    }
                }
            }
            //Note: Anything drawn must be between .begin() and .end()
            batch.begin();
            batch.draw(img, 0, 0);
            // TODO 6: Write a loop to iterate through activeObjects and call draw(batch).
            for(GameObject game : activeObjects){
                game.draw(batch);
            }
            // hud section
            font.draw(batch, "Your Health: " + player.getHealth(), 5, 175);
            font.draw(batch, "XP: " + player.getxP() + "/" + xpThreshold, 5, 170);
            font.draw(batch, "Level: " + level, 5, 165);
            batch.end();

            for (int i = activeObjects.size() - 1; i >= 0; i--) {
                GameObject obj = activeObjects.get(i);
                //loop to wrap objects around the screen
                if (obj.getX() > 320 && obj.getX() < 600) {
                    obj.setX(0);
                }
                if (obj.getY() > 180 && obj.getY() < 600) {
                    obj.setY(0);
                }
                if (obj.getY() < 0) {
                    obj.setY(180);
                }
                if (obj.getX() < 0) {
                    obj.setX(320);
                }
            }
            if (!objectsToAdd.isEmpty()) {
                activeObjects.addAll(objectsToAdd);
                objectsToAdd.clear();
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

    private void spawnEnemies(int count) {
    for (int i = 0; i < count; i++) {
        int spawnX = 0;
        int spawnY = 0;
        int padding = 30; // How far off-screen they start
        
        // Pick a random side: 0=Left, 1=Right, 2=Top, 3=Bottom
        int side = (int)(Math.random() * 4);

        switch (side) {
            case 0: // Left side
                spawnX = -padding;
                spawnY = (int)(Math.random() * 180);
                break;
            case 1: // Right side
                spawnX = 320 + padding;
                spawnY = (int)(Math.random() * 180);
                break;
            case 2: // Top side
                spawnX = (int)(Math.random() * 320);
                spawnY = 180 + padding;
                break;
            case 3: // Bottom side
                spawnX = (int)(Math.random() * 320);
                spawnY = -padding;
                break;
        }

            Enemy newEnemy = new Enemy(spawnX, spawnY, 9, 9, 
            "assets/Enemys/WalkerF1.png", "assets/Enemys/WalkerF2.png", 
            "assets/Enemys/WalkerF1.png", "assets/Enemys/WalkerF2.png", 
            enemySpeed, enemyHealth, enemyAttackDamage);

            enemies.add(newEnemy);
            objectsToAdd.add(newEnemy); 
        }
    }

    // the HUD methods are here becuese i was lazzy
    public void LevelUp() {
    

    // add the selection screen here

    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { // Use isKeyJustPressed to avoid double-triggering
        level++;
        xpThreshold += 20;
        player.setxP(0);
        
        // Scale difficulty
        enemyCount += 1;
        enemySpeed += 1;
        enemyHealth += 20;
        enemyAttackDamage += 5;
        
        spawnEnemies(enemyCount);
        
        // Reset the game state
        time = 1.0f;
        isLevelingUp = false; 
        }
    }
    public void resetGame() {
        // Reset player stats
        // hud section
        player.setHealth(150);
        
    }
}
