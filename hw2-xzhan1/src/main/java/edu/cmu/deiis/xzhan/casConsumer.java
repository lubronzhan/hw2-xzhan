/**
 * @author Xiaoran Zhan (xzhan) 
 * 
 * 
 * 
 */

package edu.cmu.deiis.xzhan;
import edu.cmu.deiis.evaluator.PrecisionEvaluator;
import edu.cmu.deiis.types.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceProcessException;

/**
 * A simple CAS consumer that writes the CAS to Text format.
 * <p>
 * This CAS Consumer takes one parameter:
 */
public class casConsumer extends CasConsumer_ImplBase {

  /**
   * Processes the CAS which was populated by the AnalysisEngines. 
   * 
   * both lingpipe and abner <br>
   * 
   * In this case, the CAS is converted into .out and written into the output file.
   * 
   * @param aCAS
   *          a CAS which has been populated by the TAEs
   * 
   * @throws IOExcepiton
   *          if cannot build the file or cannot write to it
   * 
   */
  
  
  public static final String PARA_OUTPUTER = "OutputDirectory";
  
  
  public void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    
    
    
    
    
    FSIndex fs = jcas.getAnnotationIndex(edu.cmu.deiis.types.Annotation.type);
    Iterator iter = fs.iterator(); 
    
    // New a file to store result
    File outFile = null;
    try {
      outFile = new File((String) getConfigParameterValue(PARA_OUTPUTER));
      
      FileWriter fw = new FileWriter(outFile);
      BufferedWriter output = new BufferedWriter(fw);
      // count the space
      int spaceIn = 0; 
      int spaceBefore = 0;
      
      int key = 0;
      int value = 0;
      
      while(iter.hasNext()){
        String result = "";
        edu.cmu.deiis.types.Annotation w = (edu.cmu.deiis.types.Annotation) iter.next();
        
        
        // count the number of space in sentence.
        
     // Eliminate the space before each word

        
        key = w.getBegin();
        value = w.getEnd();
        
        String line = w.getSentence();
        
        String gene = line.substring(key, value);
        String [] wordDivide = gene.split("\\s");
        spaceIn = wordDivide.length - 1;
        
        // and before sentence
        if(key == 0){
          spaceBefore = 0;
        }
        else{
          String [] beforeDivide = line.substring(0, key + 1).split("\\s");
          spaceBefore = beforeDivide.length - 1;
        }
        
        result += w.getId();
        result += "|";
        result += w.getBegin() - spaceBefore;
        result += " ";
        result += w.getEnd() - 1 - spaceIn - spaceBefore;
        result += "|";
        result += gene;
//        result += "\r\n";
//        result += " ";
        System.out.println(result);
        output.write(result);
        output.newLine();
        output.flush();
        
        
        
      }
      
      output.close();
      
      
      // to show the precision, recall, f1 score 
//      PrecisionEvaluator acc = new PrecisionEvaluator();
//      acc.precisionAndRecall();
//      System.out.println("The precise of our system is " + acc.getPercision());
//      System.out.println("The recall of our system is " + acc.getRecall());
//      System.out.println("The f-score of our system is " + acc.getFscore());
//      
    } catch (IOException e) {
      // invalid URL, use default processing below
      e.printStackTrace();
    }

  }
}
