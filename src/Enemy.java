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
    public void getkilled() {
        if (Enemy.this.getHealth() <= 0) {
            Enemy.this.setX(Integer.MAX_VALUE); // Move off-screen
            Enemy.this.setY(Integer.MAX_VALUE);
        }
    }
    
    public void move(double deltaTime, Player target){
        //double distance = Math.sqrt(Math.pow(Math.abs(targetY-getY()), 2.0)+Math.pow(Math.abs(targetX-getX()), 2.0));

        double targetX = target.getX();
        double targetY = target.getY();

        if(getX()<targetX){
            setX(getX() + (speed/2 * deltaTime));
        }
        if(getY()<targetY){
            setY(getY() + (speed/2 * deltaTime));
        }
        if(getX()>targetX){
            setX(getX() - (speed/2 * deltaTime));
        }
        if(getY()>targetY){
            setY(getY() - (speed/2 * deltaTime));
        }
         
    }
}