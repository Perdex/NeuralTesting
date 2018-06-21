package evolution16;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;

public class Creature{
    
    private static final int killSpeed = 20;
    public static int topGen = 0, leastGen = 0;
    
    private double speed, normalSpeed, x, y, rot;
    private int food = 800;
    private final int generation, size;
    private final Brain brain;
    
    private int r, g, b;
    
    public Creature(double x, double y){
        this.x = x;
        this.y = y;
        size = (int)(Math.random() * 6 + 6);
        normalSpeed = Math.random() * 2;
        speed = normalSpeed;
        rot = Math.random() * 2 * Math.PI;
        generation = 1;
        
        r = (int)(Math.random() * 256);
        g = (int)(Math.random() * 256);
        b = (int)(Math.random() * 256);
        
        brain = new Brain();
        
    }//Creature
    
    // Create a mutated creature
    private Creature(Creature c){
        // Position behind the parent, facing away
        x = c.x - Math.cos(c.rot) * c.size * 2 * (1 + Math.random() * 0.2);
        y = c.y - Math.sin(c.rot) * c.size * 2 * (1 + Math.random() * 0.2);
        rot = (c.rot + Math.PI) % (Math.PI * 2);
        
        // Update gen
        generation = c.generation + 1;
        topGen = Math.max(topGen, generation);
        
        // Vary the creature size
        int size = c.size;
        double d = sq(Math.random() * 3);
        if(Math.random() < 0.5)
            size += d;
        else
            size -= d;
        
        size = Math.max(size, 6);
        size = Math.min(size, 25);
        
        this.size = size;
        
        // Vary the "idle" speed
        normalSpeed = c.normalSpeed;
        d = sq(Math.random() * 3);
        if(Math.random() < 0.5)
            normalSpeed += d;
        else
            normalSpeed -= d;
        
        // Limit the speed to sensible values
        normalSpeed = Math.min(normalSpeed, 2);
        normalSpeed = Math.max(normalSpeed, -2);
        
        speed = normalSpeed;
        
        // Slightly change the color
        r = c.r;
        g = c.g;
        b = c.b;
        if(Math.random() < 0.5){
            r += Math.random() * 40 - 20;
            r = Math.max(r, 0);
            r = Math.min(r, 255);
        }
        if(Math.random() < 0.5){
            g += Math.random() * 40 - 20;
            g = Math.max(g, 0);
            g = Math.min(g, 255);
        }
        if(Math.random() < 0.5){
            b += Math.random() * 40 - 20;
            b = Math.max(b, 0);
            b = Math.min(b, 255);
        }
        
        brain = new Brain(c.brain);
        
        
    }//Creature from another
    
    private int sq(double d){
        return (int)(d*d);
    }//sq

    public double getRot(){
        return rot;
    }

    public int getSize(){
        return size;
    }
    public int getGen(){
        return generation;
    }//getGen   
    public int getChrom(){
        return brain.size();
    }//getGen
    public ArrayList<Neuron> getNeurons(){
        return brain.getNeurons();
    }//getNeurons
    public double[] getInput(){
        return brain.getIn();
    }//getInput
    public double[] getOutput(){
        return brain.getOut();
    }//getInput
    
    public boolean act(double pace, ArrayList<Creature> cc, ArrayList<Food> foods, JPanel p){
        //boolean tells if this should be removed
        
        //check age
        food--;
        if(food < 0)
            return true;
        
        double[] out = brain.act(this, cc, foods, pace);
        
        rot += out[0] * pace * 2; // Magical!
        speed += out[1] * pace * 2;
        
        x += pace * speed * Math.cos(rot);
        y += pace * speed * Math.sin(rot);
        
        speed += pace * 0.02 * (normalSpeed - speed);
        
        //bounce from walls
        if(x < size){
            rot += Math.PI / 2;
            rot *= -1;
            rot -= Math.PI / 2;
            x = size;
        }if(x > p.getWidth() - size){
            rot += Math.PI / 2;
            rot *= -1;
            rot -= Math.PI / 2;
            x = p.getWidth() - size;
        }
        
        if(y < size){
            rot *= -1;
            y = size;
        }if(y > p.getHeight() - size){
            rot *= -1;
            y = p.getHeight() - size;
        }

        rot %= Math.PI * 2;
        
        // Eat all foods that the snout touches
        for(int i = 0; i < foods.size(); i++){
            Food f = foods.get(i);
            if(f.contains(getPointX(), getPointY())){
                eat(f.getFood());
                foods.remove(i);
            }
        }
        
        // MASSACRE the other creatures
        for(Creature c : cc){
            if(c == this)
                continue;
            
            // Check if killing the other
            if(Math.hypot(c.x - getPointX(), c.y - getPointY()) < c.size){
                // Eat up the opponent
                c.food -= killSpeed;
                food += killSpeed;
            }
            
            // Prevent overlap
            double dist = Math.hypot(x - c.x, y - c.y) - 0.01;//prevent /0
            if(dist < size + c.size){
                double toMove = dist - size - c.size;
                
                x += toMove * (c.x - x) / dist;
                y += toMove * (c.y - y) / dist;
            }
        }
        
        // Randomly reproduce if well eaten
        if(food > 400 && Math.random() < 2e-6 * food * pace){
            // Create the child
            cc.add(new Creature(this));
            
            // Surely childbirth is tough.. at least 400 tough!
            food -= 400;
        }
        
        // Update statistic
        leastGen = Math.min(leastGen, generation);
        
        return false;
    }//act
    
    public boolean contains(Point p){
        return Math.hypot(p.x - x, p.y - y) < size + 5;
    }//contains
    
    public void draw(Graphics g, boolean selected){
        
        g.setColor(Color.black);
        g.drawLine((int)x, (int)y, getPointX(), getPointY());
        
        g.setColor(new Color(r, this.g, b));
        g.fillOval((int)(x - size), (int)(y - size), 2 * size, 2 * size);
        
        if(selected)
            g.setColor(Color.red);
        else
            g.setColor(Color.black);
        g.drawOval((int)(x - size), (int)(y - size), 2 * size, 2 * size);
        
        
    }//draw
    
    public void eat(int amount){
        food += amount;
    }
    
    public void setX(double x){
        this.x = x;
    }//setX
    public double getX(){
        return x;
    }//getX
    public void setY(double y){
        this.y = y;
    }//setY
    public double getY(){
        return y;
    }//getY
    
    public int getFood(){
        return food;
    }
    
    private int getPointX(){
        return (int)(Math.cos(rot) * 2 * size + x);
    }//getPoint
    private int getPointY(){
        return (int)(Math.sin(rot) * 2 * size + y);
    }//getPoint
    
}
