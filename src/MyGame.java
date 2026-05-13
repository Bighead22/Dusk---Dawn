import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
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
    private Texture startS;
    private Texture selc;

    private BitmapFont font;
    private int framerate = 20;
    private int randomX = (int)(Math.random() * 320);
    private int randomY = (int)(Math.random() * 180);
    private boolean hasGameStarted = false;
    private boolean hasCharacterSelected = false;
    private boolean hasGameEnded = false;
    private boolean hasStart = false;

    private ArrayList<Enemy> enemies;
    private int enemyCount = 5;
    private int enemySpeed = 2;
    private int enemyHealth = 100;
    private int enemyAttackDamage = 15;

    private Player player;
    private Weapon playerWeapon;

    private int playerSpeed = 20;
    private int health = 150;
    private int playerLevelUp = 1;

    private Texture healthUpgradeImg;
    private Texture SpeedUpgradeImg;

    private int score = 0;
    private int level = 0;
    private int xpThreshold = 100;

    private int attackDamage = 5;
    private int attackRange = 35;
    //35 dawn
    //20 dusk
    private int attackCooldown = 10;

    private boolean isLevelingUp = false;

    private Texture WeponUpgradeImg;
    private Texture WeponSpeedUpgradeImg;
    private Texture WeponSizeUpgradeImg;
    private int damageLevelUp = 1;
    private int highscore = level;
    private boolean frame = true;
    private Music backGroundMusic;
    

    // Track which character was last chosen so resetGame can recreate the right one
    private boolean isDusk = true;

    private float time = 1.0f;


    @Override
    public void create() {
        batch = new SpriteBatch();

        backGroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Sound/AGlacierEventuallyFarts.mp3"));

        font = new BitmapFont();
        font.getData().setScale(0.4f);
        font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        img = new Texture("assets\\backGroundv4.png");
        startS = new Texture("assets\\StartScreen.png");
        selc = new Texture("assets/SScreen.png");

        float worldWidth = 320;
        float worldHeight = 180;
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);

        activeObjects = new ArrayList<GameObject>();
        enemies = new ArrayList<Enemy>();

        player = new Dusk(160, 45, playerSpeed, health);
        player.setHitbox(5);
        activeObjects.add(player);

        playerWeapon = new DuskWeapon(attackRange, attackRange, attackDamage, attackCooldown,
            "assets/Weapon/explosionF1.png", "assets/Weapon/explosionF2.png");
        activeObjects.add(playerWeapon);

        spawnEnemies(enemyCount);

        WeponUpgradeImg      = new Texture("assets/upgrades/weponUpgrade.png");
        WeponSpeedUpgradeImg = new Texture("assets/upgrades/attackSpeed.png");
        WeponSizeUpgradeImg  = new Texture("assets/upgrades/sizeUp.png");
        healthUpgradeImg     = new Texture("assets/upgrades/hpUp.png");
        SpeedUpgradeImg      = new Texture("assets/upgrades/speedUp.png");
    }

    @Override
    public void render() {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backGroundMusic.play();
        backGroundMusic.setLooping(true);
        backGroundMusic.setVolume(0.5f); // Range 0.0 to 1.0

        // STATE 1: Start Screen
        if (!hasGameStarted) {
            batch.begin();
            batch.draw(startS, 0, 0);
            try {
                font.draw(batch, "Current High Score: " + getHighScore("src\\Scores.txt"), 130, 10);
                font.draw(batch, "Press ENTER to Start", 130, 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            batch.end();
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                hasGameStarted = true;
            }
            return;
        }

        // STATE 2: Character Select Screen
        if (!hasCharacterSelected) {
            batch.begin();
            batch.draw(selc, 0, 0);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A) ||
               (Gdx.input.getX() < 640 && Gdx.input.isButtonPressed(Input.Buttons.RIGHT))) {
                // Dusk
                isDusk = true;
                hasCharacterSelected = true;
                playerSpeed = 30;
                player.setSpeed(30);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D) ||
               (Gdx.input.getX() > 640 && Gdx.input.isButtonPressed(Input.Buttons.RIGHT))) {
                // Dawn — swap player and weapon
                isDusk = false;
                activeObjects.remove(player);
                player = new Dawn(160, 45, playerSpeed, health);
                player.setHealth(300);
                attackDamage = 1;
                player.setHitbox(5);
                activeObjects.add(player);
                playerSpeed = 15;
                player.setSpeed(15);

                activeObjects.remove(playerWeapon);
                playerWeapon = new PlayerWeapon(attackRange, attackRange, attackDamage, attackCooldown,
                    "assets/Weapon/explosionF1.png", "assets/Weapon/explosionF2.png");
                activeObjects.add(playerWeapon);

                hasCharacterSelected = true;
            }
            return;
        }

        // Frame-rate limiter
        try {
            Thread.sleep((1 / framerate) * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

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

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Save high score once on death
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
            batch.begin();
            batch.draw(img, 0, 0);
            font.draw(batch, "GAME OVER", 130, 100);
            font.draw(batch, "Press R to Restart", 120, 80);
            batch.end();

            for (Enemy enemy : enemies) {
                enemy.setHealth(0);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                resetGame();
            }

        } else {

            double deltaTime = ((double) Gdx.graphics.getDeltaTime() * time);

            for (GameObject game : activeObjects) {
                game.move(deltaTime);
                player.ablity(enemies, deltaTime);

                playerWeapon.updateAndAttack((int) player.getX(), (int) player.getY(), enemies);
                playerWeapon.visualHit((int) player.getX(), (int) player.getY());

                enemies.forEach(enemy -> enemy.getkilled());
                enemies.forEach(enemy -> enemy.move(deltaTime, player));
                enemies.forEach(enemy -> enemy.attack(player));
            }

            // Enemy-enemy separation
            for (int i = 0; i < enemies.size(); i++) {
                for (int j = 0; j < enemies.size(); j++) {
                    if (i != j) {
                        Enemy enemy1 = enemies.get(i);
                        Enemy enemy2 = enemies.get(j);
                        if (enemy1.getHitbox().overlaps(enemy2.getHitbox())) {
                            float overlapX = (float)(Math.min(enemy1.getX() + enemy1.getWidth(),  enemy2.getX() + enemy2.getWidth())
                                                   - Math.max(enemy1.getX(), enemy2.getX()));
                            float overlapY = (float)(Math.min(enemy1.getY() + enemy1.getHeight(), enemy2.getY() + enemy2.getHeight())
                                                   - Math.max(enemy1.getY(), enemy2.getY()));

                            if (overlapX < overlapY) {
                                if (enemy1.getX() < enemy2.getX()) {
                                    enemy1.setX(enemy1.getX() - overlapX / 2);
                                    enemy2.setX(enemy2.getX() + overlapX / 2);
                                } else {
                                    enemy1.setX(enemy1.getX() + overlapX / 2);
                                    enemy2.setX(enemy2.getX() - overlapX / 2);
                                }
                            } else {
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

            // Enemy death / XP
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);

                if (enemy.getHealth() <= 0) {
                    activeObjects.remove(enemy);
                    enemies.remove(i);
                    i--; // adjust index after removal

                    player.setxP(player.getxP() + 20);

                    if (player.getxP() >= xpThreshold) {
                        isLevelingUp = true;
                        time = 0.05f;
                    }
                }
            }

            batch.begin();
            batch.draw(img, 0, 0);

            if (isLevelingUp) {
                drawLevelUpScreen();
                for (Enemy enemy : enemies) {
                    enemy.setX(2147483647);
                }
            }

            for (GameObject game : activeObjects) {
                game.draw(batch);
            }

            font.draw(batch, "Your Health: " + player.getHealth(), 5, 175);
            font.draw(batch, "XP: " + player.getxP() + "/" + xpThreshold, 5, 170);
            font.draw(batch, "Level: " + level, 5, 165);
            font.draw(batch, "Damage: " + playerWeapon.getDamage(), 5, 160);
            batch.end();

            if (isLevelingUp) {
                player.setX(10000);
                LevelUp();
            }

            // Screen wrapping
            for (int i = activeObjects.size() - 1; i >= 0; i--) {
                GameObject obj = activeObjects.get(i);
                if (obj.getX() > 320 && obj.getX() < 600) obj.setX(0);
                if (obj.getY() > 180 && obj.getY() < 600) obj.setY(0);
                if (obj.getY() < 0) obj.setY(180);
                if (obj.getX() < 0) obj.setX(320);
            }

            if (!objectsToAdd.isEmpty()) {
                activeObjects.addAll(objectsToAdd);
                objectsToAdd.clear();
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        backGroundMusic.dispose();
    }

    private void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            int spawnX = 0;
            int spawnY = 0;
            int padding = 30;
            int side = (int)(Math.random() * 4);

            switch (side) {
                case 0: spawnX = -padding;        spawnY = (int)(Math.random() * 180); break;
                case 1: spawnX = 320 + padding;   spawnY = (int)(Math.random() * 180); break;
                case 2: spawnX = (int)(Math.random() * 320); spawnY = 180 + padding;   break;
                case 3: spawnX = (int)(Math.random() * 320); spawnY = -padding;        break;
            }

            Enemy newEnemy = new Enemy(spawnX, spawnY, 9, 9,
                "assets/Enemys/WalkerF1.png", "assets/Enemys/WalkerF2.png",
                "assets/Enemys/WalkerF1.png", "assets/Enemys/WalkerF2.png",
                enemySpeed, enemyHealth, enemyAttackDamage);

            enemies.add(newEnemy);
            objectsToAdd.add(newEnemy);
        }
    }

    public void LevelUp() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
           ((Gdx.input.getX() > 640) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT))) {
            if (damageLevelUp == 1) attackDamage  += (attackDamage * 0.1) + 5;
            if (damageLevelUp == 2) attackCooldown -= attackCooldown * 0.1;
            if (damageLevelUp == 3) attackRange    += attackRange * 0.1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                  ((Gdx.input.getX() < 640) && Gdx.input.isButtonPressed(Input.Buttons.RIGHT))) {
            if (playerLevelUp == 1) health      += (health * 0.3) + 25;
            if (playerLevelUp == 2) playerSpeed += (playerSpeed * 0.3) + 5;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT)  ||
            Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {

            player.setX(160);
            level++;
            xpThreshold += 20;
            player.setxP(0);

            enemyCount += 1;
            if (level % 5 == 0) {
                enemySpeed += 1;
                enemyAttackDamage += 20;
            }
            enemyHealth += 15;

            spawnEnemies(enemyCount);

            time = 1.0f;
            isLevelingUp = false;
            player.setHealth(health);
            playerWeapon.setDamage(attackDamage);
            playerWeapon.setAttackSpeed(attackCooldown);
            playerWeapon.setWidth(attackRange);
            playerWeapon.setHeight(attackRange);
            player.setSpeed(playerSpeed);
            damageLevelUp = (int)(Math.random() * 3) + 1;
            playerLevelUp = (int)(Math.random() * 2) + 1;
        }
    }

    public void resetGame() {
        // 1. Clear all enemies from activeObjects and the enemies list
        for (Enemy enemy : enemies) {
            activeObjects.remove(enemy);
        }
        enemies.clear();

        // 2. Remove old player and weapon from activeObjects
        activeObjects.remove(player);
        activeObjects.remove(playerWeapon);

        // 3. Reset all stats to defaults
        level            = 0;
        xpThreshold      = 100;
        score            = 0;
        attackDamage     = 20;
        attackCooldown   = 10;
        attackRange      = 35;
        enemyCount       = 5;
        enemySpeed       = 2;
        enemyHealth      = 100;
        enemyAttackDamage = 15;
        playerSpeed      = 20;
        health           = 150;
        isLevelingUp     = false;
        time             = 1.0f;
        frame            = true;
        damageLevelUp    = (int)(Math.random() * 3) + 1;
        playerLevelUp    = (int)(Math.random() * 2) + 1;

        // 4. Re-create the correct player and weapon based on last character choice
        if (isDusk) {
            player = new Dusk(160, 45, playerSpeed, health);
        } else {
            player = new Dawn(160, 45, playerSpeed, health);
        }
        player.setHitbox(5);
        activeObjects.add(player);

        if (isDusk) {
            playerWeapon = new DuskWeapon(attackRange, attackRange, attackDamage, attackCooldown,
                "assets/Weapon/explosionF1.png", "assets/Weapon/explosionF2.png");
        } else {
            playerWeapon = new PlayerWeapon(attackRange, attackRange, attackDamage, attackCooldown,
                "assets/Weapon/explosionF1.png", "assets/Weapon/explosionF2.png");
        }
        activeObjects.add(playerWeapon);

        // 5. Spawn fresh enemies
        spawnEnemies(enemyCount);

        // 6. Send back to start screen
        hasGameStarted      = false;
        hasCharacterSelected = false;
        hasGameEnded        = false;
    }

    private void drawLevelUpScreen() {
        if (damageLevelUp == 1) batch.draw(WeponUpgradeImg,      160,      90 - 45);
        if (damageLevelUp == 2) batch.draw(WeponSpeedUpgradeImg, 160,      90 - 45);
        if (damageLevelUp == 3) batch.draw(WeponSizeUpgradeImg,  160,      90 - 45);
        if (playerLevelUp == 1) batch.draw(healthUpgradeImg,     160 - 90, 90 - 45);
        if (playerLevelUp == 2) batch.draw(SpeedUpgradeImg,      160 - 90, 90 - 45);
    }

    public void appendToFile(int input, String filePath) throws IOException {
        Files.write(
            Paths.get(filePath),
            (input + System.lineSeparator()).getBytes(),
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
        System.out.println("Saved\n");
    }

    public int getHighScore(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        String[] lines = content.split(System.lineSeparator());
        int maxScore = 0;
        for (String line : lines) {
            try {
                int score = Integer.parseInt(line.trim());
                if (score > maxScore) maxScore = score;
            } catch (NumberFormatException e) {
                // Skip non-integer lines
            }
        }
        return maxScore;
    }
}