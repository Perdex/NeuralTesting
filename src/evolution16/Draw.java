package evolution16;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class Draw extends JPanel{
    
    private final Evolution16 main;
    
    private long time;
    public Creature selected = null;
    
    
    public Draw(Evolution16 e){
        super();
        
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent ev){
                switch(ev.getKeyCode()){
                    case KeyEvent.VK_SPACE:
                        e.toggleSpeed();
                        break;
                }
            }
        });
        setFocusable(true);
        requestFocus();
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent ev){
                requestFocus();
            }
            @Override
            public void mouseClicked(MouseEvent ev){
                boolean flag = false;
                for(int i = 0; i < e.getCreatures().size(); i++)
                    if(e.getCreatures().get(i).contains(ev.getPoint())){
                        selected = e.getCreatures().get(i);
                        flag = true;
                        break;
                    }
                
                if(!flag)
                    selected = null;
            }
        });
        
        JButton addButton = new JButton("Add 10");
        addButton.addActionListener((ActionEvent ev)->{
            e.add();
        });
        add(addButton);
        
        main = e;
    }//Draw
    
    @Override
    public void paint(Graphics g){
        time = System.currentTimeMillis();
        
        
        try{
            g.setColor(new Color(250, 250, 250));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            ArrayList<Creature> creatures = main.getCreatures();
            
            for(int i = 0; i < creatures.size(); i++){
                try{
                    boolean b = false;
                    if(selected != null)
                        b = creatures.get(i) == selected;
                    
                    creatures.get(i).draw(g, b);
                }catch(Exception e){
                    System.out.println(e + " at draw creature");
                }
            }
            
            
            g.setColor(Color.black);
            g.drawString(Long.toString(Evolution16.cycletime) + "ms max", 2, 15);
            
            double avgg = 0, avgc = 0;
            for(int i = 0; i < creatures.size(); i++){
                avgg += (double)creatures.get(i).getGen() / creatures.size();
                avgc += (double)creatures.get(i).getChrom() / creatures.size();
            }
            
            for(int i = 0; i < main.getFoods().size(); i++){
                Food f = main.getFoods().get(i);
                f.draw(g);
            }
            
            String fastModeOn = "(OFF)";
            if(main.isFast())
                fastModeOn = "(ON)";
            g.drawString("Press space to toggle fast mode " + fastModeOn, 5, getHeight() - 38);
            
            g.setFont(new Font("arial", 0, 17));
            g.drawString("Gen max/min/avg: " + Creature.topGen + " / " + Creature.leastGen + " / " + 
                    (double)((int)(10 * avgg))/10, 5, getHeight() - 20);
            
            g.drawString("Avg chromosomes: " + (double)((int)(10 * avgc))/10, 5, getHeight() - 5);
            
            if(selected != null)
                drawNetwork(g);
            
            
        }catch(Exception e){
            System.out.println(e + " at draw");
            e.printStackTrace(System.err);
        }
        
        paintComponents(g);
        
        long newTime = System.currentTimeMillis();
        if(newTime - time < 30){
            try{
                Thread.sleep(30 - newTime + time);
            }catch(InterruptedException e){System.out.println("WTF?!?!?");}
        }
        
        
        
        repaint();
        
    }//paint
    
    
    private void drawNetwork(Graphics g){
        int w = getWidth();
        g.setColor(Color.white);
        g.fillRect(w - 400, -1, w, 280);
        g.setColor(Color.black);
        g.drawRect(w - 400, -1, w, 280);

        int c;
        ArrayList<Neuron> neurons = selected.getNeurons();
        for(int i = 0; i < neurons.size(); i++){

            for(int j = 0; j < neurons.get(i).inputWeights.length; j++){
                c = Math.max(128 - (int)(neurons.get(i).inputWeights[j] * 128), 0);
                c = Math.min(c, 255);
                g.setColor(new Color(c, c, c));
                g.drawLine(w - 190, 30 + i * 260 / neurons.size(), w - 340, 30 + j * 240 / neurons.get(i).inputWeights.length);
            }

            for(int j = 0; j < 2; j++){
                c = Math.max(128 - (int)(neurons.get(i).outputWeights[j] * 128), 0);
                c = Math.min(c, 255);
                g.setColor(new Color(c, c, c));
                g.drawLine(w - 190, 30 + i * 260 / neurons.size(), w - 40, 100 + j * 100);
            }

            c = Math.max(128 - (int)(neurons.get(i).value * 128), 0);
            c = Math.min(c, 255);
            g.setColor(new Color(c, c, c));
            g.fillOval(w - 200, 20 + i * 260 / neurons.size(), 20, 20);
            g.setColor(Color.black);
            g.drawOval(w - 200, 20 + i * 260 / neurons.size(), 20, 20);
        }

        for(int i = 0; i < selected.getInput().length; i++){
            c = Math.max(128 - (int)(selected.getInput()[i] * 128), 0);
            c = Math.min(c, 255);
            g.setColor(new Color(c, c, c));
            g.fillOval(w - 350, 20 + i * 240 / selected.getInput().length, 20, 20);
            g.setColor(Color.black);
            g.drawOval(w - 350, 20 + i * 240 / selected.getInput().length, 20, 20);
        }
        for(int i = 0; i < 2; i++){
            c = Math.max(128 - (int)(selected.getOutput()[i] * 128), 0);
            c = Math.min(c, 255);
            g.setColor(new Color(c, c, c));
            g.fillOval(w - 50, 90 + 100 * i, 20, 20);
            g.setColor(Color.black);
            g.drawOval(w - 50, 90 + 100 * i, 20, 20);
        }
        
        g.drawString(Integer.toString(selected.getFood()), getWidth() - 80, 20);
    }//drawNetwork
    
}
