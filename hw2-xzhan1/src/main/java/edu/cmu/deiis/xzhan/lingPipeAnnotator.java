/**
 * @author Xiaoran Zhan (xzhan) 
 * 
 * 
 * 
 */

package edu.cmu.deiis.xzhan;

import java.io.IOException;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import com.aliasi.chunk.Chunk;

import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.deiis.types.Annotation;




/**
 * using lingpipe HMMChunker model to check whether targets are genetag words.
 * 
 * @confidence: 0.6
 * 
 * @Max_N_Best: 5
 * 
 * 
 */
public class lingPipeAnnotator extends JCasAnnotator_ImplBase {

  /**
   * Use lingpipe process the content of JCas
   * 
   * @throws ClassNotFoundException, IOException
   *      Cannot read the HmmChunker
   * 
   * @see JCasAnnotator_ImplBase#process(JCas)
   */
  
  
  public static final String PARAM_OUTPUTDIR = "modelPath";
  public void process(JCas aJCas) {
    // get document text
    String docText = aJCas.getDocumentText();
    // search for Yorktown room numbers
    

    lingPipeAnnotator lp;
    try{

      // Analyze Line by line
      String [] textStr = docText.split("\n");
      int length = textStr.length;
      // lingpipe
      
      // read trained model 
      
      String inputFile = (String) getContext().getConfigParameterValue(PARAM_OUTPUTDIR);
      
      
      ConfidenceChunker chunker = (ConfidenceChunker) AbstractExternalizable
              .readResourceObject(inputFile);
      // Analyse line info line by line
      for(int i = 0; i < length; i++){
        // Line number;
        // Cut the 
        String id = textStr[i].substring(0, 14);
        // Content after line number
        String line = textStr[i].substring(15, textStr[i].length());
       
        char[] cs = line.toCharArray();
        
        Iterator<Chunk> it
        = chunker.nBestChunks(cs,0,cs.length,5);

        
        // Put each genetag word into annotation
        while(it.hasNext()){// Check whether exist
          // get next Chunk
          Chunk chunk = it.next();
          
          // keep result with confidence >= 0.2
          double confidence = Math.pow(2.0,chunk.score());
          if( confidence < 0.6) break;
     
          int key = chunk.start();
          int value = chunk.end();      
          
          
          
          // build annotation and add to each instance of WordAnalyser
          Annotation annotation = new Annotation(aJCas);

          // value : the index after the word
          annotation.setBegin(key);
          annotation.setEnd(value);
          annotation.setId(id);
          annotation.setSentence(line);
          annotation.setCasProcessorId("LingPipe");
          annotation.setConfidence(confidence);
          annotation.addToIndexes();
      

        }
  

        }  
      }catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ClassNotFoundException e) { // Hmm model
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  
  }

}
