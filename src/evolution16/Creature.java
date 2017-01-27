package evolution16;

import java.awt.*;
import java.util.*;
import javax.swing.JPanel;

public class Creature{
    
    public static int topGen = 0, leastGen = 0;
    
    private double speed, normalSpeed;
    double x, y, rot;
    private int food = 0, lifeTime = 3000, generation;
    int size;
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
    
    private Creature(Creature c){
        x = c.x - Math.cos(c.rot) * c.size * 2 * (1 + Math.random() * 0.2);
        y = c.y - Math.sin(c.rot) * c.size * 2 * (1 + Math.random() * 0.2);
        rot = (c.rot + Math.PI) % (Math.PI * 2);
        generation = c.generation + 1;
        topGen = Math.max(topGen, generation);
        
        size = c.size;
        double d = sq(Math.random() * 3);
        if(Math.random() < 0.5)
            size += d;
        else
            size -= d;
        
        size = Math.max(size, 6);
        size = Math.min(size, 25);
        
        
        normalSpeed = c.normalSpeed;
        d = sq(Math.random() * 3);
        if(Math.random() < 0.5)
            normalSpeed += d;
        else
            normalSpeed -= d;
        
        normalSpeed = Math.min(normalSpeed, 2);
        normalSpeed = Math.max(normalSpeed, -2);
        
        speed = normalSpeed;
        
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
    
    public int getGen(){
        return generation;
    }//getGen   
    public int getChrom(){
        return brain.size();
    }//getGen
    public ArrayList<Brain.neuron> getNeurons(){
        return brain.getNeurons();
    }//getNeurons
    public double[] getInput(){
        return brain.getIn();
    }//getInput
    public double[] getOutput(){
        return brain.getOut();
    }//getInput
    
    public Creature act(double pace, ArrayList<Creature> cc, JPanel p){
        
        //do whatever needs to be done
        
        
        //check age
        lifeTime--;
        if(lifeTime < 0)
            return this;
        
        double[] out = brain.act(this, cc, pace);
        
        rot += out[0] * pace * 2;
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
        
        Creature kill = null;
        for(int i = 0; i < cc.size(); i++){
            Creature c = cc.get(i);
            
            if(c == this)
                continue;
            
            //check if killing the other
            if(Math.hypot(c.x - getPointX(), c.y - getPointY()) < c.size){
                kill = c;
                food++;
            }
            
            //remove overlap
            double dist = Math.hypot(x - c.x, y - c.y) - 0.01;//prevent /0
            if(dist < size + c.size){
                double toMove = dist - size - c.size;
                
                x += toMove * (c.x - x) / dist;
                y += toMove * (c.y - y) / dist;
            }
        }
        
        if(Math.random() < 0.005 * (1 + food) * pace){
            cc.add(new Creature(this));
            
            food /= 2;
        }
        
        
        leastGen = Math.min(leastGen, generation);
        
        return kill;
    }//act
    
    public boolean contains(Point p){
        return Math.hypot(p.x - x, p.y - y) < size;
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
    
    public void setX(double x){this.x = x;}//setX
    public double getX(){return x;}//getX
    
    public void setY(double y){this.y = y;}//setY
    public double getY(){return y;}//getY
    
    private int getPointX(){
        return (int)(Math.cos(rot) * 2 * size + x);
    }//getPoint
    private int getPointY(){
        return (int)(Math.sin(rot) * 2 * size + y);
    }//getPoint
    
}
