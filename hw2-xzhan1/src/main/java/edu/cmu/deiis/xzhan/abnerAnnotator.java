package edu.cmu.deiis.xzhan;

import java.io.File;
import java.util.Iterator;

import abner.*;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Annotation;
import edu.cmu.deiis.types.GeneAnnotation;

/**
 * using abner to check the word extracted by lingpipe is viable or not, 
 * or doesn't belong to Gene and protein. 
 * 
 * <p>
 * If abner reject one result, then we also check the confidence of lingpipe. 
 * If it is below 0.7, then we abandon it.
 * 
 * 
 */
public class abnerAnnotator extends JCasAnnotator_ImplBase {

  public static Tagger tag = new Tagger();


  /**
   * Read JCas process by lingpipe, check wether result is right or not
   * 
   * Delete those abner think is wrong and with low confidence, also delete RNA 
   * 
   * 
   * @see JCasAnnotator_ImplBase#process(JCas)
   */
  public void process(JCas aJCas) {
    
    FSIndex lingpipe = aJCas.getAnnotationIndex(Annotation.type);
    Iterator iter = lingpipe.iterator();   
    while (iter.hasNext()) {
      Annotation lprocessed = (Annotation)iter.next();
      // original line
      String line = lprocessed.getSentence();
      
      
      boolean abandon = false;
      
      int start = lprocessed.getBegin();
      int end = lprocessed.getEnd();
      
      String[][] ents = tag.getEntities(line.substring(start, end));
      
      double confidenceLingpipe = lprocessed.getConfidence();
      
      // if abner reject and the confidence of lingpipe is below. drop this one
      if(ents[0].length == 0 && confidenceLingpipe < 0.65){
      
        lprocessed.removeFromIndexes();
        break;
      }
      
      // check wether RNA exist
      if(ents[0].length > 0){
        for (int i=0; i<ents[0].length; i++) {
            if(ents[0][i].equals("RNA")){
              abandon = true; 
              break;
            }
            
        }
      }
      
      
      // if contain RNA
      if(abandon == true){
        lprocessed.removeFromIndexes();
     
      }
      
      
      
      
    }
    
    
  
  }

}
