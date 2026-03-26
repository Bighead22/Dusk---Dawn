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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    private int playerSpeed = 20;
    private int health = 150;
    private int playerLevelUp = 1;

    private Texture healthUpgradeImg;
    private Texture SpeedUpgradeImg;

    private int score = 0;
    private int level = 0;
    private int xpThreshold = 100;

    private int attackDamage = 20;
    private int attackRange = 35;
    private int attackCooldown = 10;

    private boolean isLevelingUp = false;

    private Texture WeponUpgradeImg;
    private Texture WeponSpeedUpgradeImg;
    private Texture WeponSizeUpgradeImg;
    private int damageLevelUp = 1;
    private int highscore = level;
    private boolean frame = true;
    
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
        

        player = new Dawn(160, 45, playerSpeed,health);
        activeObjects.add(player);

        playerWeapon = new PlayerWeapon( attackRange, attackRange, attackDamage, attackCooldown, "assets/Weapon/explosionF1.png", "assets/Weapon/explosionF2.png");
        activeObjects.add(playerWeapon);

        
        // makes enemies and adds them to activeObjects

        enemies = new ArrayList<Enemy>();

        spawnEnemies(enemyCount);

        WeponUpgradeImg = new Texture("assets/upgrades/weponUpgrade.png");
        WeponSpeedUpgradeImg = new Texture("assets/upgrades/attackSpeed.png");
        WeponSizeUpgradeImg = new Texture("assets/upgrades/sizeUp.png");
        healthUpgradeImg = new Texture("assets/upgrades/hpUp.png");
        SpeedUpgradeImg = new Texture("assets/upgrades/speedUp.png");
        
    }

    //render() is the game loop, called approx 60 times per second
    @Override
    public void render() {

        if (level > highscore) {
            highscore = level;
        }
        
        health = player.getHealth();
        player.setHealth(health);
        playerWeapon.setDamage(attackDamage);
        playerWeapon.setAttackSpeed(attackCooldown);
        playerWeapon.setWidth(attackRange);
        playerWeapon.setHeight(attackRange);
        player.setSpeed(playerSpeed);
        
        // Boilerplate: Clear the screen to black each frame
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (player.getHealth() <= 0) {
            if (frame) {
                try {
                    appendToFile(highscore, "src\\Scores.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                frame = false;
            }
            hasGameEnded = true;
            highscore = 0;
        }

        if (hasGameEnded) {
            // 3. Draw Lose Screen
            batch.begin();
            batch.draw(img, 0, 0); // Draw background
            font.draw(batch, "GAME OVER", 130, 100);
            font.draw(batch, "Press R to Restart", 120, 80);
            batch.end();
            for (Enemy enemy : enemies) {
                enemy.setHealth(0); // Stop enemies from moving or attacking
            }

            // Restart logic
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                frame = true;
                // Reset health and state (you might want a full reset method)
                resetGame();
                hasGameEnded = false;
                spawnEnemies(5); // Respawn enemies
            }
        } else {

            double deltaTime = ((double) Gdx.graphics.getDeltaTime() * time);



            // what happens in the game
            for(GameObject game : activeObjects){
                game.move(deltaTime);

                playerWeapon.updateAndAttack((int) player.getX(), (int) player.getY(), enemies);
                playerWeapon.visualHit((int) player.getX(), (int) player.getY());
            
            
                enemies.forEach(enemy -> enemy.getkilled());
                enemies.forEach(enemy -> enemy.move(deltaTime, player));
                enemies.forEach(enemy -> enemy.attack(player));

                if (Gdx.input.isKeyPressed(Input.Keys.R)){
                    player.setSpeed(100);
                }else{
                    player.setSpeed(20);
                }
            }

            for (int i = 0; i < enemies.size(); i++){
                for (int j = 0; j < enemies.size(); j++){
                    if (i != j) {
                        Enemy enemy1 = enemies.get(i);
                        Enemy enemy2 = enemies.get(j);
                        if (enemy1.getHitbox().overlaps(enemy2.getHitbox())) {
                            // Simple separation logic: move them apart
                            float overlapX = (float) (Math.min((float)(enemy1.getX() + enemy1.getWidth()), (float)(enemy2.getX() + enemy2.getWidth())) - Math.max(enemy1.getX(), enemy2.getX()));
                            float overlapY = (float) (Math.min((float)(enemy1.getY() + enemy1.getHeight()), (float)(enemy2.getY() + enemy2.getHeight())) - Math.max(enemy1.getY(), enemy2.getY()));

                            if (overlapX < overlapY) {
                                // Move horizontally
                                if (enemy1.getX() < enemy2.getX()) {
                                    enemy1.setX(enemy1.getX() - overlapX / 2);
                                    enemy2.setX(enemy2.getX() + overlapX / 2);
                                } else {
                                    enemy1.setX(enemy1.getX() + overlapX / 2);
                                    enemy2.setX(enemy2.getX() - overlapX / 2);
                                }
                            } else {
                                // Move vertically
                                if (enemy1.getY() < enemy2.getY()) {
                                    enemy1.setY(enemy1.getY() - overlapY / 2);
                                    enemy2.setY(enemy2.getY() + overlapY / 2);
                                } else {
                                    enemy1.setY(enemy1.getY() + overlapY / 2);
                                    enemy2.setY(enemy2.getY() - overlapY / 2);
                                }
                            }
                        }
                    }
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

            if (isLevelingUp) {
                drawLevelUpScreen(); // Call the helper we made above
                for (Enemy enemy : enemies) {
                    enemy.setX(2147483647 );
                }
            }

            // TODO 6: Write a loop to iterate through activeObjects and call draw(batch).
            for(GameObject game : activeObjects){
                game.draw(batch);
            }
            // hud section
            font.draw(batch, "Your Health: " + player.getHealth(), 5, 175);
            font.draw(batch, "XP: " + player.getxP() + "/" + xpThreshold, 5, 170);
            font.draw(batch, "Level: " + level, 5, 165);
            font.draw(batch, "Damage: " + playerWeapon.getDamage(), 5, 160);
            batch.end();

            if (isLevelingUp) {
                player.setX(10000);
                LevelUp();
            }

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
    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || (Gdx.input.getX() > 640) && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        if (damageLevelUp == 1) {
            attackDamage += attackDamage * 0.1;
        }
        if (damageLevelUp == 2) {
            attackCooldown -= attackCooldown * 0.1;
        }
        if (damageLevelUp == 3) {
            attackRange += attackRange * 0.1;
        }
    } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || (Gdx.input.getX() < 640) && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        if (damageLevelUp == 1) {
            health += health * 0.3;
        }
        if (damageLevelUp == 2) {
            playerSpeed += playerSpeed * 0.3;
        }
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            player.setX(160);
            level++;
            xpThreshold += 20;
            player.setxP(0);
            
            // Scale difficulty
            enemyCount += 1;
            if (level % 3 == 0) { // Every 3 levels, increase enemy speed
                enemySpeed += 1;
            }
            enemyHealth += 20;
            enemyAttackDamage += 5;
            
            spawnEnemies(enemyCount);
            
            // Reset the game state
            time = 1.0f;
            isLevelingUp = false;
            player.setHealth(health);
            playerWeapon.setDamage(attackDamage);
            playerWeapon.setAttackSpeed(attackCooldown);
            playerWeapon.setWidth(attackRange);
            playerWeapon.setHeight(attackRange);
            player.setSpeed(playerSpeed);
            damageLevelUp = (int)(Math.random() * 3)+ 1;
            playerLevelUp = (int)(Math.random() * 2)+ 1;
        }
    }
    public void resetGame() {
        // Reset player stats
        // hud section
        level = 0;
        xpThreshold = 100;
        score = 0;
        player.setxP(0);
        attackDamage = 20;
        attackCooldown = 10;
        attackRange = 35;
        enemyCount = 5;
        enemySpeed = 2;
        enemyAttackDamage = 15;
        player.setHealth(150);
        player.setSpeed(20);
        
    }
    private void drawLevelUpScreen() {
    
    if (damageLevelUp == 1){
        batch.draw(WeponUpgradeImg, 160, 90-45); //damge
    }
    if (damageLevelUp == 2){
        batch.draw(WeponSpeedUpgradeImg, 160, 90-45); //size
    }
    if (damageLevelUp == 3){
        batch.draw(WeponSizeUpgradeImg, 160, 90-45); //speed
    }
    if (playerLevelUp == 1){
        batch.draw(healthUpgradeImg, 160-90, 90-45);// health
    }
    if (playerLevelUp == 2){
        batch.draw(SpeedUpgradeImg, 160-90, 90-45);// speed
    }

    // 3. Draw the text instructions
    
    }
    public void appendToFile(int input, String filePath)throws IOException {
        
            Files.write(
                Paths.get(filePath), 
                (input + System.lineSeparator()).getBytes(), 
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND
            );
            System.out.println("Saved\n");
        
    }
}
