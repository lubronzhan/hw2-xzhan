/**
 * The file is to calculate and return the precision, recall, f1 score the result.
 * 
 * @author Xiaoran Zhan (xzhan) 
 * 
 * 
 * 
 */


package edu.cmu.deiis.evaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class PrecisionEvaluator {
  // accuracy of our output
  private double precision;
  // recall of our output
  private double recallPer;
  
  private double fscore;
  
  /**
   * Constructor
   * <p>
   * Initialize three attributes. precision, recallPer, fscore  
   */
  public PrecisionEvaluator(){
    this.precision = 0;
    this.recallPer = 0;
    this.fscore = 0.0;
  }

  /**
   * Calculate the Precision and recall and f-1 score
   *
   * @throws IOExcepiton
   *          if cannot open file, the output by us or the sample output.
   */
  public void precisionAndRecall(){
    
    // Read output
    File file = new File("hw2-xzhan.out");
    BufferedReader reader = null;
    
    // count how many lines match.
    int lineMatch = 0;
    
    // number of line in output
    int lineOutNum = 0;
    
    // number of line in sample
    int lineSampleNum = 0;
    // To store each line in map, easier for retrieve and compare
    Map<String,Integer> map = new HashMap();
    
    String tempString = "";
 
    try{
      reader = new BufferedReader(new FileReader(file));
      // Content of each line
      //String tempString = "";
      while ((tempString = reader.readLine()) != null) {
        lineOutNum++;
        map.put(tempString, lineOutNum);
        
        // every time cite readline() will read a line
      }
    }catch(Exception e){
      System.out.println("Not able to read output hw2-xzhan");
    }
    
    
    // Read Sample
    File sample = new File("sample.out");
    BufferedReader samReader = null;
    try{
      samReader = new BufferedReader(new FileReader(sample));
      // Content of each line
      String samString = "";
      while ((samString = samReader.readLine()) != null) {
        lineSampleNum++;
        if(map.get(samString) != null){
          lineMatch++; // number of match
        }
      }
    }catch(Exception e){
      System.out.println("Not able to read sample file");
    }
    
    System.out.println("Hitting number: "+lineMatch);
    System.out.println("Number of sample.out: "+lineSampleNum);
    System.out.println("Number of hw2-xzhan: "+lineOutNum);
    // The Precise
    precision = (double) lineMatch / lineOutNum;
    // The recall
    recallPer = (double) lineMatch / lineSampleNum;
    
    fscore = 2 * (precision * recallPer)/(precision + recallPer);
    
  }
  
  /**
   * Get the precision
   * 
   * @return precision
   */ 
  public double getPercision(){
    return precision;
  }
  /**
   * Get the recall 
   * 
   * @return recallPer
   */ 
  public double getRecall(){
    return recallPer;
  }
  /**
   * Get the f-1 score
   * 
   * @return fscore
   */ 
  public double getFscore(){
    return fscore;
  }
  


}
