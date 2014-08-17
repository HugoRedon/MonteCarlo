package montecarlo;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

/**
 *
 * @author Hugo Redon Rivera
 */
public class MonteCarloView implements Initializable {
    
    Calculator calcula;
    final double P = 220;
    final double  PP = 500;
    final double I0 = 38414573.80;
    final double Interes = 1;
    
    @FXML ScatterChart vpnProbChart;
    @FXML  ScatterChart randomFrequencyChart;
      
     double[][] porcentajesProbP, porcentajesProbPP , porcentajesProbI0, porcentajesProbi,porcentajesProba;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        NumberAxis vpnAxis = new NumberAxis();
//        NumberAxis frequencyAxis = new NumberAxis();
//        randomFrequencyChart = new BarChart<>(vpnAxis,frequencyAxis);
        
        
        calcula = new Calculator();
        porcentajesProbP =new double[][] {
                    {0.7,0.35},
                    {0.8,0.1},
                    {0.9,0.05},
                    {1.0,0.2},
                    {1.1,0.1},
                    {1.2,0.05},
                    {1.3,0.15}
        };
        porcentajesProbPP =new double [][]  {
                    {0.7,0.05 },
                    {0.8,0.1},
                    {0.9,0.1},
                    {1.0,0.2},
                    {1.1,0.15},
                    {1.2,0.3},
                    {1.3,0.1}
        };
         porcentajesProbI0 =new double [][]{
                    {0.8,0.1},
                    {0.9,0.1},
                    {1.0,0.4},
                    {1.1,0.1},
                    {1.2,0.3}
        };
        porcentajesProbi =new double[][]  {
//                    {0.2,1}
                   {0.05,1}
//                    {0.15,0.2}         
        };    
        porcentajesProba = new double[][]{
                    {0.2,0.5},
                    {0.25,0.3},
                    {0.15,0.2} 
        };
        
        vpnsProbs = calcula.calculateVPN(porcentajesProbP, porcentajesProbPP, porcentajesProbI0, porcentajesProbi,porcentajesProba, I0, P, PP, Interes);
        vpnsCumulativeProbs = calcula.getCumulativeProbs(vpnsProbs);
    
        vpnCumulativeProbSerie = getVpnCumulativeProbsSerie(vpnsCumulativeProbs);           
        vpnProbSerie = getVpnProbSerie(vpnsProbs);
    }    
    

    

    
     ArrayList<double[]> vpnsProbs;
     ArrayList<double[]> vpnsCumulativeProbs;
     
     @FXML ToggleButton vpnToggle, vpnCumulativeProbToggle;
     XYChart.Series vpnProbSerie, vpnCumulativeProbSerie;
     
    @FXML
    protected void vpnProbPaint(ActionEvent event){
     
         if(vpnToggle.isSelected()){
             vpnProbChart.getData().add(vpnProbSerie);
         }else{
             vpnProbChart.getData().remove(vpnProbSerie);
        }
          
    }
    @FXML
    protected void vpnCumulativeProbPaint(ActionEvent event){
             
         if(vpnCumulativeProbToggle.isSelected()){
             vpnProbChart.getData().add(vpnCumulativeProbSerie);
         }else{
             vpnProbChart.getData().remove(vpnCumulativeProbSerie);
        }
        
    }
    
  @FXML TextField mediaTextField,deviationTextField,nTextField ;
    
    @FXML
    protected void vpnFrequencyPaint(ActionEvent event){
        double[][] classes = calcula.getVpnClassLowerUpper(vpnsCumulativeProbs);
        double[][] vpnFrequencies = calcula.getVPNFrequency(classes);
        
        randomFrequencyChart.getData().add(getVpnFrequenciesSerie(vpnFrequencies));
        
        double[] mediaDeviationN = calcula.getMediaStandDeviationN(vpnFrequencies);
        
      // Formatter f = new Formatter();
       DecimalFormat df = new DecimalFormat("###, ###, ###, ###.#; -###, ###, ###, ###.#");
      
       
       String media = df.format(mediaDeviationN[0]);
       String dev = df.format(mediaDeviationN[1]);
        
        mediaTextField.setText(media);
        deviationTextField.setText(dev);
        nTextField.setText(String.valueOf(mediaDeviationN[2]));
    }
    
    public  XYChart.Series  getVpnProbSerie(ArrayList<double[]> vpnsProbs){
        
        XYChart.Series vpnSerie = new XYChart.Series<>();       
        for(double[] vpnProb : vpnsProbs ){
            XYChart.Data data =new XYChart.Data<>(vpnProb[0], vpnProb[1]);
            vpnSerie.getData().add(data);
        }
        return vpnSerie;
    }  
    
    public  XYChart.Series  getVpnCumulativeProbsSerie(ArrayList<double[]> vpnsCumulativeProbs){
        
       XYChart.Series  vpnSerie = new XYChart.Series<>();       
       
        for(double[] vpnProb : vpnsCumulativeProbs ){
            System.out.println("vpn = " + vpnProb[0] + "; probAcumulada = " + vpnProb[1] );
            XYChart.Data data =new XYChart.Data<>(vpnProb[0], vpnProb[1]);
            vpnSerie.getData().add(data);
        }
        return vpnSerie;
    }
    
    
    public XYChart.Series getVpnFrequenciesSerie(double[][] vpnFrequencies){
        XYChart.Series vpnFrequencySerie = new XYChart.Series();
        
        for(double[] vpnFreq : vpnFrequencies ){
          
            XYChart.Data data =new XYChart.Data(vpnFreq[0], vpnFreq[1]);
            vpnFrequencySerie.getData().add(data);
        }
        
        
        return vpnFrequencySerie;
    }
    
}



