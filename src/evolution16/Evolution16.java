package evolution16;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Evolution16 extends Thread{
    
    private final ArrayList<Creature> creatures = new ArrayList();
    private final ArrayList<Food> foods = new ArrayList();
    
    public static long cycletime = 0;
    
    private int pace = 1;
    
    public ArrayList<Creature> getCreatures(){
        return creatures;
    }//getCreatures
    public ArrayList<Food> getFoods(){
        return foods;
    }//getCreatures
    
    private long time;
    @Override
    public void run(){
        
        for(int i = 0; i < 5; i++)
            add();
        addFood(100);
        
        while(true){
            time = System.currentTimeMillis();
            
            Creature.leastGen = Creature.topGen;
            //make all creatures act
            for(int i = 0; i < creatures.size(); i++){
                try{
                    boolean kill = creatures.get(i).act((double)pace / 4, creatures, foods, panel);
                    if(kill)
                        creatures.remove(i);
                }catch(Exception e){
                    System.out.println(e + " at run");
                }
            }
            //spawn new food
            if(Math.random() < 0.1)
                addFood(1);
            
            
            long newTime = System.currentTimeMillis();
            if(Math.random() < 0.03)
                cycletime = newTime - time;
            else
                cycletime = Math.max(cycletime, newTime - time);
            
            if(newTime - time < 15 && pace != 2){
                try{
                    Thread.sleep(15 - newTime + time);
                }catch(InterruptedException e){System.out.println("WTF?!?!?");}
            }
        }
        
    }//run
    
    public void add(){
        for(int i = 0; i < 10; i++)
            creatures.add(new Creature(Math.random() * panel.getWidth(), Math.random() * panel.getHeight()));
    }//add
    private void addFood(int amount){
        for(int i = 0; i < amount; i++){
            foods.add(new Food(Math.random() * panel.getWidth(), Math.random() * panel.getHeight()));
        }
    }
    public void remove(Creature c){
        creatures.remove(c);
    }//remove
    public void toggleSpeed(){
        pace = 6 - pace;
    }//toggleSpeed
    public boolean isFast(){
        return pace != 1;
    }//isFast
    
    private static Draw panel;
    public static void main(String[] args){
        JFrame frame = new JFrame("So many bananas!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Evolution16 main = new Evolution16();
        
        panel = new Draw(main);
        
        frame.add(panel);
        
        int w = 1000, h = 600;
        //position frame to center of screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((int)(screen.getWidth() / 2 - w / 2), 
            (int)(screen.getHeight() / 2 - h / 2), w, h);
        
        
        frame.setVisible(true);
        
        main.start();
        
    }//main

}
