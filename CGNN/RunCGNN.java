import java.io.*;//import input output
public class RunCGNN
{

 public static void main(String args[])
 {
         
         String yes = "N"; 
         do
         {
              System.out.print("\n --------->Welcome to the System for Neural Network<-----------\n");
            try
            {          
             
               int P,In,Hi,Ot,k, ITR; 

 
               BufferedReader br  =  new BufferedReader(new InputStreamReader(System.in));
              
               System.out.print("\n Enter the no. Input node   :");
               In=Integer.parseInt(br.readLine());
               System.out.print("\n Enter the no. Hidden node  :");
               Hi=Integer.parseInt(br.readLine());
               System.out.print("\n Enter the no. Output node  :");
               Ot=Integer.parseInt(br.readLine());
  
               System.out.print("\n Enter the no. Input Pattern:");
               P=Integer.parseInt(br.readLine());

               InputStreamReader in=new InputStreamReader(System.in);
               BufferedReader ibr=new BufferedReader(in);

               System.out.print("\n Enter The File Name with extention for Input Value:");
               String FILE_IN=ibr.readLine();
               FILE_IN = FILE_IN + ".txt";
               //String FILE_IN="InNode1.txt";
               
               System.out.print("\n Enter The File Name with extention for Target Value:");
               String FILE_TR=ibr.readLine();
               FILE_TR = FILE_TR + ".txt";
               //String FILE_TR="TrNode1.txt";

               FileReader  fread_in = new FileReader(FILE_IN);
               BufferedReader br_in = new BufferedReader(fread_in);
                
               FileReader  fread_tr = new FileReader(FILE_TR);
               BufferedReader br_tr = new BufferedReader(fread_tr);

   
              CGNN.pattern[] pat= new CGNN.pattern[P];//--->(P) Write 4 for XOR pattern
              CGNN net = new CGNN(In, Hi, Ot);//--->(C)no of neuon at Input, Hidden and Output Layer
		
              double InP[] = new double[In];
              double TrP[] = new double[Ot];
              
              k=0;       
              while(k<P)
              {
               
                 double InP1[]=new double[In];
                 double TrP1[]=new double[Ot];

                System.out.println("\n\n Values for pattern no.:"+(k+1));

                System.out.println("\n Values for input nodes");  
                for(int i=0;i<In;i++)
                { 
                  InP1[i]=(float)Float.parseFloat(br_in.readLine());
                  System.out.printf("\n Input node[%d]:%.3f",(i+1),InP1[i]); 
                } 

                System.out.println("\n\n Values for target output");  
                for(int i=0;i<Ot;i++)
                { 
                  TrP1[i]=(float)Float.parseFloat(br_tr.readLine());
                  System.out.printf("\n Trget output[%d]:%.3f",(i+1),TrP1[i]); 
                } 
                pat[k] = new CGNN.pattern(InP1,TrP1);
                k++; 
              }


             /* pat[0] = new CGNN.pattern(new double[]{0,0},new double[]{0});
	        pat[1] = new CGNN.pattern(new double[]{1,0},new double[]{1});
	        pat[2] = new CGNN.pattern(new double[]{0,1},new double[]{1});
	        pat[3] = new CGNN.pattern(new double[]{1,1},new double[]{-1});*/

      	    System.out.println("\n\n Running BPN Network of Size "+In+"-"+Hi+"-"+Ot);

               System.out.print("\n Enter the Max No. of Iteration: ");
               ITR =Integer.parseInt(br.readLine());

	        //# train it with some patterns
                long startTime = System.currentTimeMillis();
 
	           net.train(pat,ITR,0.5,0.1);//---->(1)
         
                long endTime = System.currentTimeMillis();
                System.out.println("Training time :"+ (endTime-startTime)+"milisecond");

	        net.test(pat);//---->(T)
              net.printmatrix() ;
	      
             
              System.out.print("\n\nPress \" Y \" to run a test pattern :");
              yes = br.readLine();  
              if(yes.equals("Y")||yes.equals("y"))
              {
                do
                {
                  System.out.println("\nEnter values for "+In+" input nodes");  
                  for(int i=0;i<In;i++)
                  { 
                    System.out.print("\n Value for Input node["+(i+1)+"]:");
                    InP[i]=(float)Float.parseFloat(br.readLine());
                  } 
                  for(int i=0;i<Ot;i++)
                  { 
                     TrP[i]=-10000.0;
                  } 

                  pat[0] = new CGNN.pattern(InP,TrP);
                  net.isTest=true;
	            net.test(pat);//---->(T)
                  net.isTest=false;

                  System.out.print("\n\nPress \" Y \" to run another test pattern :");
                  yes = br.readLine();  
                }
                while(yes.equals("Y")||yes.equals("y"));
  
              }
               
              System.out.print("\n\nPress \" Y \" to Contineue for another Network Structure:");
              yes = br.readLine();
             }
             catch(Exception e)
             {
                System.out.println("Errpr "+e);
                System.exit(0); 
             }
             
         }
         while(yes.equals("Y")||yes.equals("y"));
  
 }
 
}