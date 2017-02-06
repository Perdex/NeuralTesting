package evolution16;

import java.awt.Color;
import java.awt.Graphics;



public class Food {

    private static final int foodSize = 100, radius = 8;
    
    private final int x, y;

    public Food(double x, double y){
        this.x = (int)x;
        this.y = (int)y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    
    public int getRadius(){
        return radius;
    }
    
    public int getFood(){
        return foodSize;
    }
    
    public void draw(Graphics g){
        g.setColor(Color.gray);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }
    
    public boolean contains(int x, int y){
        return Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2) <= radius * radius;
    }
    
}
