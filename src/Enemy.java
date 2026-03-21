public class Enemy extends GameObject {

    private int speed;
    private int health;

    public Enemy(int x, int y, int width, int height, String imagePath, int speed, int health) {
        super(x, y, width, height, imagePath);
        this.speed = speed;
        this.health = health;
    }
    public int getHealth(){
        return health;
    }
    public void setHealth(int health){
        this.health = health;
    }
}