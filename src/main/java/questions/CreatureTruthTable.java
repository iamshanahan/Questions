package questions;

import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.*;
import java.io.*;
/**
*
*/
public class CreatureTruthTable implements Serializable {

    private static final long serialVersionUID = 7526471155622776147L;
// TODO is there an annotation for serial expected here?
    private int numCreatures;

    private int numAttributes;

    public CreatureTruthTable( int numCreatures, int numAttributes ) {
    }

   private void readObject(
     ObjectInputStream inputStream
   ) throws ClassNotFoundException, IOException {
     //always perform the default de-serialization first
     inputStream.defaultReadObject();
  }

    private void writeObject(
      ObjectOutputStream outputStream
    ) throws IOException {
      outputStream.defaultWriteObject();
    }
} 
