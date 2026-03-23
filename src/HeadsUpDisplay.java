import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class HeadsUpDisplay{
    private int health;
    private int score;
    private BitmapFont font = new BitmapFont((Gdx.files.internal("myfont.fnt")));

    public HeadsUpDisplay(int health, int score) {
        this.health = health;
        this.score = score;
    }

    public void showHealth(int change, Batch batch) {
        font.draw(batch, "HP: " + health, 0, 0);
    }

    public void showScore(int change, Batch batch) {
        font.draw(batch, "Score: " + score, 0, 0);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}