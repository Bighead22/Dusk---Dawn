import com.badlogic.gdx.Gdx;

public class Enemy extends GameObject {

    private int speed;
    private float walkAnimationSpeed = 1f;
    private int health;
    private int attackDamage;
    private float attackTimer = 0;
    private float animationTimer = 0;
    private float attackspeed = 25f;
    private String frame1;
    private String frame2;
    private String frame3;
    private String frame4;

    public Enemy(int x, int y, int width, int height, String f1, String f2, String f3, String f4, int speed, int health, int attackDamage) {
        super(x, y, width, height, f1);
        this.speed = speed;
        this.health = health;
        this.attackDamage = attackDamage;
        this.frame1 = f1;
        this.frame2 = f2;
        this.frame3 = f3;
        this.frame4 = f4;
        this.walkAnimationSpeed = 1.0f / (float) speed;
    }


    public void walkAnimation(String f1, String f2) {
        animationTimer += Gdx.graphics.getDeltaTime(); // Use animationTimer

        if (animationTimer < walkAnimationSpeed) {
            setTexture(f1);
        } else if (animationTimer < walkAnimationSpeed * 2) {
            setTexture(f2);
        } else {
            animationTimer = 0;
        }
    }


    public int getHealth(){
        return health;
    }
    public void setHealth(int health){
        this.health = health;
    }
    public int getSpeed(){
        return speed;
    }
    public void setSpeed(int speed){
        this.speed = speed;
    }
    public int getAttackDamage() {
        return attackDamage;
    }
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
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
        String f1 = this.frame1;
        String f2 = this.frame2;
        String f3 = this.frame3;
        String f4 = this.frame4;

        if(getX()<targetX){
            setX(getX() + (speed/2 * deltaTime));
            walkAnimation(f1, f2);
        } else
        if(getX()>targetX){
            setX(getX() - (speed/2 * deltaTime));
            walkAnimation(f3, f4);
        }
        if(getY()>targetY){
            setY(getY() - (speed/2 * deltaTime));
        }
        if(getY()<targetY){
            setY(getY() + (speed/2 * deltaTime));
        }
         
    }
    public void attack(Player target){
        attackTimer += Gdx.graphics.getDeltaTime();
        if (attackTimer >= this.attackspeed) {
            if (this.getHitbox().overlaps(target.getHitbox())) {
                target.setHealth(target.getHealth() - this.attackDamage);
                System.out.println("Enemy attacked! Player health: " + target.getHealth());
                attackTimer = 0;
            }
        }

    }
}