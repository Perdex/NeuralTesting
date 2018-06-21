package evolution16;



public class Neuron{
        
    final double[] inputWeights = new double[12], outputWeights = new double[2];
    double value = 0;

    Neuron(){
        for(int i = 0; i < inputWeights.length; i++){
            inputWeights[i] = Math.pow(Math.random() * 4 - 2, 3);
        }
        for(int i = 0; i < outputWeights.length; i++){
            outputWeights[i] = Math.pow(Math.random() * 4 - 2, 3);
        }
    }//neuron

    Neuron(Neuron n){
        for(int i = 0; i < inputWeights.length; i++){
            inputWeights[i] = n.inputWeights[i];
            // Every now and then, modify the weight
            if(Math.random() < 0.2)
                inputWeights[i] += Math.pow(Math.random() * 4 - 2, 3);
        }
        for(int i = 0; i < outputWeights.length; i++){
            outputWeights[i] = n.outputWeights[i];
            // Every now and then, modify the weight
            if(Math.random() < 0.2)
                outputWeights[i] += Math.pow(Math.random() * 4 - 2, 3);
        }
    }//neuron

    void act(double pace, double[] input, double[] output){

        value = 0;
        for(int i = 0; i < inputWeights.length; i++){
            value += input[i] * inputWeights[i];
        }
        value = Brain.f(value);

        output[0] = value * outputWeights[0];
        output[1] = value * outputWeights[1];

    }//neuron.act

        
        
        
    }//neuron
