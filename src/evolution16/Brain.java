package evolution16;

import java.util.ArrayList;

public class Brain {
    
    
    private final ArrayList<Neuron> neurons;
    private double[] input = new double[12], output;
    
    public Brain(){
        neurons = new ArrayList();
        int n = (int)(Math.random() * 4) + 4;
        for(int i = 0; i < n; i++){
            neurons.add(new Neuron());
        }
    }//Brain
    
    public Brain(Brain b){
        neurons = new ArrayList();
        for(Neuron n: b.neurons){
            if(Math.random() < 0.98)
                neurons.add(new Neuron(n));
            
        }
        
    }//Brain
    
    public double[] getIn(){
        return input;
    }//getIn
    public double[] getOut(){
        return output;
    }//getIn
    
    public double[] act(Creature c, ArrayList<Creature> creatures, ArrayList<Food> foods, double pace){
        
        input = new double[12];
        
        for(int i = 0; i < creatures.size(); i++){
            Creature cc = creatures.get(i);
            
            double dist = Math.hypot(c.getX() - cc.getX(), c.getY() - cc.getY()) - 0.001;//prevent /0
            if(dist < (c.getSize() + cc.getSize()) * 3  && cc != c){
                
                double dir = Math.acos((cc.getX() - c.getX()) / dist) - c.getRot();
                if(cc.getY() < c.getY())
                    dir *= -1;
                
                
                dir += Math.PI / 4;
                
                
                dir += Math.PI * 4;
                dir %= Math.PI * 2;
                
                input[(int)(3 * dir / Math.PI)] += 5/dist;
            }
        }
        for(int i = 0; i < foods.size(); i++){
            Food f = foods.get(i);
            
            double dist = Math.hypot(c.getX() - f.getX(), c.getY() - f.getY()) - 0.001;//prevent /0
            if(dist < (c.getSize() + f.getRadius()) * 3){
                
                double dir = Math.acos((f.getX() - c.getX()) / dist) - c.getRot();
                if(f.getY() < c.getY())
                    dir *= -1;
                
                
                dir += Math.PI / 4;
                
                
                dir += Math.PI * 4;
                dir %= Math.PI * 2;
                
                input[6 + (int)(3 * dir / Math.PI)] += 5/dist;
            }
        }
        
        output = new double[2];
        
        for(Neuron n: neurons)
            n.act(pace, input, output);
        
        output[0] = f(output[0]);
        output[1] = f(output[1]) / 4;
        
        return output;
    }//act
    
    
    public int size(){
        return neurons.size();
    }//size
    public ArrayList<Neuron> getNeurons(){
        return neurons;
    }//getNeurons
    
    
    static double f(double x){
        return (1 - Math.exp(-2*x)) / (1 + Math.exp(-2*x));
    }//f
    
    
}
