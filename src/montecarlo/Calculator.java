package montecarlo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Hugo Redon Rivera
 */
public class Calculator {
    
    public double getG(double p){
        return 78750 * p  + 16925014.16;
    }
    public double getV(double pp){
        return 75000 * pp + 650729.63;
    }
    public double getA(double I0, double n){
        return I0 / n;
    }

    public double getC(double n, double i){
        double c =0;
        for(int t =1; t <= n; t++){
            c += 1 / Math.pow(1 + i , t);
        }
        
        return c;
    }
    
    public double getVPN(double p, double pp , double I0, double n, double i,double a){
        
        double A = getA(I0,n);
        double V = getV(pp);
        double G = getG(p);
        
      //  double a = 0.3;
   
        return - I0 + ((V- G- A)*(1 -a )+ A) *  getC(n, i);
    }
    
    public double[] getRandomNumbers(int n){
        double[] randoms = new double[n];
        for(int i = 0; i < n ; i++){
            randoms[i ] = Math.random();
        }
        return randoms;
    }
    
        public ArrayList<double[]> calculateVPN(
                double[][] porcentajesProbP,double[][] porcentajesProbPP,double[][] porcentajesProbI0,double[][] porcentajesProbi,double[][] porcentajesProba,
                   double I0, double P,double PP, double Interes
                ){
        ArrayList<double[]> vpns = new ArrayList<>();
        
         for(double[] porcProbP: porcentajesProbP){
            for(double[] porcProbPP: porcentajesProbPP){
                for(double[] porcProbI0: porcentajesProbI0){
                    for(double[] porcProbi: porcentajesProbi){
                        for(double[] porcProba: porcentajesProba){
                        
                            double io = I0 * porcProbI0[0];
                            double p = P  * porcProbP[0];
                            double pp = PP  * porcProbPP[0];
                            double i = Interes * porcProbi[0];
                            double a = Interes * porcProba[0]; 

                            double p1 = porcProbI0[1];
                            double p2 = porcProbP[1];
                            double p3 = porcProbPP[1];
                            double p4 = porcProbi[1];
                            double p5 = porcProba[1];

                            double vpn =  getVPN(p, pp, io, 10, i,a);
                            double probabilidadVPN = p1 * p2 * p3 * p4 * p5;
                            
                            double[] vpnProb = {vpn,probabilidadVPN};
                            
                            String combination = "io = " + io +"; p = " + p + "; pp = " + pp + ";  i = " + i + "; vpn = " + vpn + "; prob = "+ probabilidadVPN ;
                            vpns.add(vpnProb);
                            //System.out.println(combination);
                        }
                   }                  
                }
            }
        }// end fors
   
        Collections.sort(vpns , new DoubleArrayComparator());  
        return vpns;
    }
        //every call  is a diferent result because of the random function 
    public double[][] getVPNFrequency(double[][] VpnClassLowerUpper){
            
        int length = VpnClassLowerUpper.length;
        double[] randomNumbers = getRandomNumbers(length);

        double[][] vpnFrequency = new double[length][2];
        
        for(int i = 0 ; i < length; i++){
                double[] vpnClass = VpnClassLowerUpper[i];
                double vpn = vpnClass[0];
                vpnFrequency[i] = new double[]{vpn,0};
               
        }
        
        for(double randomNum : randomNumbers){
            for(int i = 0 ; i < length; i++){
                double[] vpnClass = VpnClassLowerUpper[i];

                double upperBound = vpnClass[1];
                double lowerBound = vpnClass[2];

                if(randomNum >= lowerBound && randomNum < upperBound){
                    vpnFrequency[i][1]++;
                    //System.out.println(vpnFrequency[i][1]);
                }
            }
        
        }
        
        return vpnFrequency;

    }
    
      public ArrayList<double[]> getCumulativeProbs(ArrayList<double[]> vpnsProbs) {
        ArrayList<double[]> cumulative = new ArrayList<>();
        
        double cumulativeVariable = 0;
        for(double[] vpnProb : vpnsProbs){
            double vpn  = vpnProb[0];
            double prop = vpnProb[1];
            
            cumulativeVariable += prop;
            double[] cumulativeArray = {vpn,cumulativeVariable};
            cumulative.add(cumulativeArray);
        }
        return cumulative;
    }
      
      
      public double[][] getVpnClassLowerUpper(ArrayList<double[]> vpnCumulativeProbs){
         // double tolerance = 1e-6;
          double[][] vpnClass = new double[vpnCumulativeProbs.size()/*.length*/][3];
          
          // for i = 0
          double[] vpnProb0 =vpnCumulativeProbs.get(0);// vpnCumulativeProbs[0];
              double vpn0 = vpnProb0[0];             
              double probi0 = vpnProb0[1];
          
          vpnClass[0] = new double[]{vpn0, probi0,0};
          
          for(int i = 1; i < vpnCumulativeProbs.size(); i++){
              //i -1
              double[] vpnProbMinus = vpnCumulativeProbs.get(i-1);//[i-1];
              double probMinus = vpnProbMinus[1];     
              
              //i
              double[] vpnProb = vpnCumulativeProbs.get(i);//[i];
              double vpn = vpnProb[0];             
              double probi = vpnProb[1];
              
              
              // array
              vpnClass[i] = (new double[]{vpn,probi,probMinus  /*- tolerance*/}) ;
          }
          
          return vpnClass;
      }
      
      public double[] getMediaStandDeviationN(double[][] vpnFrequencies){
          double media=0;
          double StandDeviation = 0;
        double varianza =0;
          
          double sumxf = 0;
          double sumfx2 = 0;
          double n = 0;
          
          for(double[] vpnFrequency :vpnFrequencies ){
              double x = vpnFrequency[0];
              double f = vpnFrequency[1];
              
              sumxf += x * f;
              //sumfx2 += f*Math.pow(x ,2);
              sumfx2 += f* Math.pow(x,2);
              n += f;
          }
          
//          for(double[] vpnFrequency: vpnFrequencies){
//              double vpn = vpnFrequency[0];
//              double f = vpnFrequency[1];
//              
//              varianza += (1- (vpn/n)) * f * vpn / n;
//          }
         
          media = sumxf/ n;
          StandDeviation = Math.sqrt((sumfx2 - (Math.pow(sumxf,2) / n))/ n);
          //StandDeviation = (sumfx2- n * Math.pow(media,2))/n ;
         // StandDeviation = Math.sqrt(varianza);
          return new double[]{media,StandDeviation,n};
      }
    
}
 class DoubleArrayComparator implements Comparator<double[]> {

    @Override
    public int compare(double[] vpnProb, double[] vpnProb1) {
      Double  vpn = vpnProb[0];
      double vpn1 = vpnProb1[0];
        
      if(vpn < vpn1) return -1;
      if(vpn.equals( vpn1)) return 0;
     
      return 1;
      
    }
}