import java.io.*;//import input output
import java.util.Random;//import random

public class CGNN implements Serializable
{
   
    static Random generator = new Random();

    int ni;//neuron at input layer
    int nh;//neuron at hidden layer
    int no;//neuron at output layer  

    int s;// totan weights;  
 
    double N,M;//N is the learning rate;M is the momentum factor
   
    double alpha,beta;//N is the learning rate;M is the momentum factor
  
    double[] ai;
    double[] ah;
    double[] ao;
  
    public double[][] wi;
    public double[][] wo;

    public double[] w;//contains one dimensional vectior of weights
    public double[] g;//contains one dimensional vectior of gradient of weights
    public double[] d;//contains one dimensional vectior of directions for weights 
    
    double[][] ci;//gradient at input layer
    double[][] co;//gradient at output layer

    boolean isTest=false; 
      int tpi, fpi, tni, fni;	
    //(R)---->calculate a random number where:  a <= rand < b
    public static double rand(double a, double b)
    { 
       double retval;
    
       retval=(b-a)*generator.nextDouble() + a;
       return retval;
    
    }
 
   public void printmatrix()
   {
      System.out.println("\n\nSynaptic Weight Matrix Input-Hidden layer\n");  
    
      for(int i=0;i<ni;i++)
        {   
           System.out.print(" [");     
            for(int j=0;j<nh;j++)
            {            
               if(wi[i][j]<0)
                 System.out.printf("%1.3f ",wi[i][j]);
               else
                 System.out.printf(" %1.3f ",wi[i][j]); 
                
            }
            System.out.println("]");
        }
      System.out.println("\nSynaptic Weight Matrix Hidden-Output layer\n"); 
      
       for(int j=0;j<nh;j++)
        {   
            System.out.print(" [");    
            for(int k=0;k<no;k++)
            {            
              if(wo[j][k]<0)  
                System.out.printf("%1.3f ",wo[j][k]); 
              else
                System.out.printf(" %1.3f ",wo[j][k]); 
            }
            System.out.println("]");
        }
    
   }
    
   public void twoDmatoneDmat()
   {  
            //System.out.println("\nSynaptic Weight Matrix Input-Hidden layer\n");

             int k=0;

             for(int i=0;i<ni;i++)
             { 
               //System.out.print("["); 
                for(int j=0;j<nh;j++)
                { 
                      
                      w[k] = wi[i][j];
                      //System.out.printf(" %.3f",w[k]);
                      //System.out.print(" ");
                      k++; 
                }

               //System.out.println("]");
             } 
             
             //System.out.println("\nSynaptic Weight Matrix Hidden-Output layer\n");

             for(int i=0;i<nh;i++)
             { 
               //System.out.print("["); 
                for(int j=0;j<no;j++)
                { 
                      w[k] = wo[i][j];
                      //System.out.printf(" %.3f",w[k]);
                      //System.out.print(" ");
                      k++; 
                }

              // System.out.println("]");
             }
       
    }//end 2Dmat_1Dmat

     public void oneDmattwoDmat()
     {
          //System.out.println("\nDisplaying the geneticly searched Synaptic Weights"); 
               
          int k=0;
                      
          for(int i=0;i<ni;i++)
           { 
             //System.out.print("["); 
              for(int j=0;j<nh;j++)
                { 
                    wi[i][j]=w[k];
                    //System.out.printf(" %.3f",wi[i][j]); 
                    k++; 
                }
             //System.out.println("]");
           }
                                
           for(int i=0;i<nh;i++)
           { 
              //System.out.print("["); 
               for(int j=0;j<no;j++)
               { 
                    wo[i][j]= w[k];
                    //System.out.printf(" %.3f",wo[i][j]);
                    k++; 
               }
              //System.out.println("]");
           }
                

     } //end 1D_mat_2Dmat  
    
    //computing mse for a given weight
    public  double errornew(pattern[] patterns)
    { 
    
        double[] res;
        double NN_error=0.0;
        double sse = 0.0;
        double mse = 0.0;
        //System.out.println("\n");  
        for (int p=0;p<patterns.length;p++)
        { 
          NN_error=0.0;
          res=update(patterns[p].input);//---->(2)
          for(int j=0;j<res.length;j++)
          {  
            NN_error = NN_error + 0.5*(( (patterns[p].target[j]-(float)res[j])*(patterns[p].target[j]-(float)res[j]) ));    
          }
          sse += NN_error;
        }
          mse += ((double)1.0/(2*patterns.length))*sse;
        return sse;//mse;  
    }
   
    //(T)--->Print output
    public  void test(pattern[] patterns,double th)
    { 
        double[] res;
		double error = 0.0;
		tpi = fpi =  tni =  fni = 0;
        //System.out.println("\n");  
        for (int p=0;p<patterns.length;p++)
        { 
          res=update(patterns[p].input);//---->(2)
		  int yes = 0;
          for(int j=0;j<res.length;j++)
		  {
		     
		     error = error + 0.5 * (patterns[p].target[j]-res[j])*(patterns[p].target[j]-res[j]);
             
			 /*int cat = res[j]<0.5?0:1;
			 if((int)patterns[p].target[j] == cat)
			 {
			   yes++;
			   System.out.print("Correct"+res.length+": "+yes);			   
			 }*/
			 
			 if((patterns[p].target[j] - res[j])<th)
			 {
			   tpi++;
			   //System.out.print("Correct");//+res.length+": "+yes);			   
			 }

			 
			 //System.out.printf("\n Pat [%d]  Target = %1.3f  Output [%d] = %1.3f %d ",p,patterns[p].target[j],j,res[j],tpi);
          }	
          /*if(yes == res.length)			 
             tpi++; */		  
        }
		double acc = (double)tpi/patterns.length;
		System.out.printf("\nError %1.3f and Correct %d among %d i.e Accuracy is %1.3f \n",error,tpi,patterns.length,acc);
   }
    //(T)--->Print output
    public  void test(pattern[] patterns)
    { 
       int pattern_length;
 
       if(isTest)
         pattern_length = 1;// no of pattern is 1 when test is performing
       else
         pattern_length = patterns.length;

        double[] res;
        //System.out.println("\n");  
        for (int p=0;p<pattern_length;p++)
        { 
          res=update(patterns[p].input);//---->(2)
          for(int j=0;j<res.length;j++)
          {
            if(isTest)
              System.out.printf("\n Input [%d] = %1.3f of Pat [%d]  Output [%d] = %1.3f",j,ai[j],p,j,res[j]);
            else
             System.out.printf("\n Input [%d] = %1.3f of Pat [%d]  Taeget = %1.3f  Output [%d] = %1.3f",j,ai[j],p,patterns[p].target[j],j,res[j]);
          }
        }
   }
    
    //(5)---->derivative of our sigmoid function
    public static double dsigmoid(double y)
    {
 	return 1.0-y*y;
    }

//(4a)---->Gradient calculation
    public  double gradient(double[] targets)
    {   
        int i,j,k;
    	double error,change;
      

        //calculate error terms for output
        double[] output_deltas = new double[no];
        for(k=0;k<no;k++)
        {        
            error = targets[k]-ao[k];
            output_deltas[k] = dsigmoid(ao[k]) * error;//---->(5)
	}
		
        //calculate error terms for hidden
        double [] hidden_deltas = new double[nh];  
        for(j=0;j<nh;j++)
        {        
            error = 0.0;
            for(k=0;k<no;k++){ error = error + output_deltas[k]*wo[j][k]; }
            hidden_deltas[j] = dsigmoid(ah[j]) * error;//---->(5)
        }
        
        
        //System.out.println("------->");
        
        int l = 0; 

        // computing gradient of input weights
        for(i=0;i<ni;i++)
        {        
            for(j=0;j<nh;j++)
            {            
                change = hidden_deltas[j]*ai[i];
                ci[i][j] = change;
                g[l] = g[l] + change;
                //System.out.printf(" %.3f",w[l]);
                 
                l++; 
            }
        } 
	
	//computing gradient of output weights
        for(j=0;j<nh;j++)
        {       
            for(k=0;k<no;k++)
            {            
                change = output_deltas[k]*ah[j];
                co[j][k] = change;//gradient at outputlayer
                g[l] = g[l] + change;
		//System.out.printf(" %.3f",w[l]); // checking the correctness of gradient to weight association
                l++;
            }
        }

       
         
        // calculate error
        error = 0.0;
        for(k=0;k<no;k++) { error = error + 0.5 * (targets[k]-ao[k])*(targets[k]-ao[k]);}// please correct this (targets[k]-ao[k])*(targets[k]-ao[k]) must be x^2
        
        return error;
     }

 
    //(t)---->tanh function
    public static double tanh(double x)
    { 

       return (Math.exp(x)- Math.exp(-x)) / (Math.exp(x)+Math.exp(-x));
 
    }

	
    //(3)---->our sigmoid function, tanh is a little nicer than the standard 1/(1+e^-x)
    public static double sigmoid(double x)
    {
 	return tanh(x);//---->(t)
    }


    //(2)---->Activation of neurons	
    public  double[] update(double[] inputs)
    { 
      int i,j;
      double sum;
    
        // input activations
        for (i=0;i<ni-1;i++){ai[i] = inputs[i];}
        
          
        //hidden activations
        for (j=0;j<nh;j++)
        {
            sum = 0.0;
            for (i=0;i<ni;i++){ sum = sum + (ai[i] * wi[i][j]) ;}
            ah[j] = sigmoid(sum);//---->(3)
	}
		
        //output activations        
        for (j=0;j<no;j++)
        {        
            sum = 0.0;
            for(i=0;i<nh;i++){ sum = sum + (ah[i] * wo[i][j]) ;}
            ao[j] = sigmoid(sum);//---->(3)
        }
        
        return ao;
    }
    
 
  
    //(1)---->Train Network
    public  void train(pattern[] patterns,int iterations, double Lr,double  Momenta)
    {   
        System.out.println("Training started");
        N=Lr;// N: learning rate 0.5 good
        M=Momenta;// M: momentum factor 0.1 good
       
        
	//oneDmattwoDmat();
        //System.out.println("CHK"); 
        
        //step 1
        int k = 1,n = 100;
        twoDmatoneDmat();//initialize w
        //System.out.println();
        oneDmattwoDmat();//initialize w
 	//System.out.println();
  
        int i,p;
        double error = 0.0;
        double mse = 0.0,sse = 0.0;
	   
        
        for(i=0;i<iterations;i++)
        {

            //Computing gradient
            for(int j=0;j<s;j++){g[j] = 0.0;}
            error = 0.0;
            int N = patterns.length;
            for(p=0;p<N;p++)
            {
                update(patterns[p].input);//----->(2)  
                error += gradient(patterns[p].target);//---->(4a)       
            }
            //System.out.println("Testing SSE "+errornew(patterns)+" = "+error);
            
            mse = (double)1.0/(2*N)*error;//this is the mse of the network  
            sse = error;

            //computing gradient
            for(int j=0; j<s;j++)
	    {g[j] = ((double)1.0/N)*g[j];}

            
            //updating direction
             if(k == 1)
             { 
               for(int j=0; j<s;j++)
                {d[j] = -g[j];}
             } 
             else
             {
               for(int j=0; j<s;j++)
                {d[j] = -g[j] + beta*d[j];}//momentum factor
             }
            
            
            //holding w[i] intact & temprorarily operating on temp               
	    double[] temp = new double[s];
	    for(int j=0; j<s;j++){temp[j] = w[j];}
             
            double alpha1 = 0.5;
            double ro = rand(0.0, 9.0);
            double c = rand(0.0, 9.0);
            
            alpha = alpha1;//set alpha equal to alpha1

            int testtry = 0;
            while(true)
            {
              //System.out.println("Searching alpha");
              //update weights
              for(int j=0; j<s;j++)
              {w[j] = w[j] - alpha*d[j];}            
              oneDmattwoDmat();//new updated weight
              
              double mse1 = 0.0,mse2 = 0.0, gTd = 0.0;
	      mse1 = errornew(patterns);
              
              for(int j=0; j<s;j++)//computing gTd
              {gTd = gTd + g[j]*d[j];}            

              mse2 = sse;//mse ; //+ c*alpha*gTd;   
              
              //System.out.println("mse1 : "+mse1+ " mse2 :"+mse2); 

              if(mse1 <= mse2)
              {
                //System.out.println("alpha found");
                break;
              } 
   
              alpha = ro*alpha;
              
              //if(testtry>1000)break;
              //testtry++;
            }

            if(k>n)
              k =1;
            else
              k++;
 
            if (i%100 == 0)
               System.out.printf("\n%1.5f",sse);
            if(error<0.00004)break;     
              
         }
        System.out.printf("\nSTOP at itration[%d] error: %1.3f \n",i,error);
     }
    
  

    //(c)---->Cunstructor
    CGNN(int ini,int inh, int ino)
    {   
        
        int i,j;
         
        //# number of input, hidden, and output nodes
           
        ni = ini + 1; // +1 for bias node
        nh = inh;
        no = ino;

        ai= new double[ni];
        ah= new double[nh];
        ao= new double[no];
        
        
        N=0.5;// N: learning rate 0.5 good;
        M=0.1;// M: momentum factor 0.1 good

        alpha = 0.5;
        beta = 0.1;
        
        
        //# init activations for nodes
        for(i=0;i<ni;i++){ai[i] = 1.0;}
        for(i=0;i<nh;i++){ah[i] = 1.0;}
        for(i=0;i<no;i++){ao[i] = 1.0;}
       
       
        
        // create weights
        wi = new double[ni][nh];  
        wo = new double[nh][no];  
        
        // set them to random vaules
        for (i=0;i<ni;i++)
          {  for (j=0;j<nh;j++)wi[i][j] = rand(-0.5, 0.5); } //---->(R)   
  
        for (i=0;i<nh;i++)
          {  for (j=0;j<no;j++)wo[i][j] = rand(-0.5, 0.5); } //---->(R)

        s = ni*nh+nh*no;

        w = new double[s];
        for(i=0;i<s;i++){w[i] = 0.0;}
        g = new double[s];
        for(i=0;i<s;i++){g[i] = 0.0;}
	d = new double[s];
        for(i=0;i<s;i++){d[i] = 0.0;}
	

        // last change in weights for momentum   
        ci = new double[ni][nh]; 
        co = new double[nh][no];   
    }

    
    //(P)--->Defining the pattern for training
    public static class pattern
    {
       double[] input;
       double[] target;
   
       pattern(double[] in,double[] out)
       {
         input=in;
         target=out;
       }
   
     }  

}
