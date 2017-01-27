package evolution16;

import java.util.ArrayList;

public class Brain {
    
    private final ArrayList<neuron> neurons;
    private double[] input = new double[6], output;
    
    public Brain(){
        neurons = new ArrayList();
        int n = (int)(Math.random() * 4) + 4;
        for(int i = 0; i < n; i++){
            neurons.add(new neuron());
        }
    }//Brain
    
    public Brain(Brain b){
        neurons = new ArrayList();
        for(neuron n: b.neurons){
            if(Math.random() < 0.98)
                neurons.add(new neuron(n));
            
        }
        
    }//Brain
    
    public double[] getIn(){
        return input;
    }//getIn
    public double[] getOut(){
        return output;
    }//getIn
    
    public double[] act(Creature c, ArrayList<Creature> creatures, double pace){
        
        input = new double[6];
        
        for(int i = 0; i < creatures.size(); i++){
            Creature cc = creatures.get(i);
            
            double dist = Math.hypot(c.x - cc.x, c.y - cc.y) - 0.001;//prevent /0
            if(dist < (c.size + cc.size) * 3  && cc != c){
                
                double dir = Math.acos((cc.x - c.x) / dist) - c.rot;
                if(cc.y < c.y){
                    dir *= -1;
                }
                
                dir += Math.PI / 4;
                
                
                dir += Math.PI * 4;
                dir %= Math.PI * 2;
                
                input[(int)(3 * dir / Math.PI)] += 5/dist;
            }
        }
        
        output = new double[2];
        
        for(neuron n: neurons)
            n.act(pace);
        
        output[0] = f(output[0]);
        output[1] = f(output[1]) / 4;
        
        return output;
    }//act
    
    
    public int size(){
        return neurons.size();
    }//size
    public ArrayList<neuron> getNeurons(){
        return neurons;
    }//getNeurons
    
    public class neuron{
        
        final double[] inputWeights = new double[6], outputWeights = new double[2];
        double value = 0;
        
        neuron(){
            for(int i = 0; i < inputWeights.length; i++){
                inputWeights[i] = Math.pow(Math.random() * 4 - 2, 3);
            }
            for(int i = 0; i < outputWeights.length; i++){
                outputWeights[i] = Math.pow(Math.random() * 4 - 2, 3);
            }
        }//neuron
        
        neuron(neuron n){
            for(int i = 0; i < inputWeights.length; i++){
                inputWeights[i] = n.inputWeights[i];
                if(Math.random() < 0.2)
                    inputWeights[i] += Math.pow(Math.random() * 4 - 2, 3);
            }
            for(int i = 0; i < outputWeights.length; i++){
                outputWeights[i] = n.outputWeights[i];
                if(Math.random() < 0.2)
                    outputWeights[i] += Math.pow(Math.random() * 4 - 2, 3);
            }
        }//neuron
        
        void act(double pace){
            
            value = 0;
            for(int i = 0; i < inputWeights.length; i++){
                value += input[i] * inputWeights[i];
            }
            value = f(value);
            
            output[0] = value * outputWeights[0];
            output[1] = value * outputWeights[1];
            
        }//neuron.act
        
        
    }//neuron
    
    private double f(double x){
        return (1 - Math.exp(-2*x)) / (1 + Math.exp(-2*x));
    }//f
    
}
