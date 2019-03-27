package questions;

import java.io.IOException;
import java.io.Serializable;
import java.text.StringCharacterIterator;
import java.util.*;
import java.io.*;
/**
*
*/
public class CreatureTruthTable {

	// TODO generate a legit one
    private static final long serialVersionUID = 7526471155622776147L;
    
    // TODO is there an annotation for serial expected here?
    private int numCreatures;

    private int numAttributes;
    
    boolean [][] table;

    public CreatureTruthTable( int numCreatures, int numAttributes ) {
    	this.numCreatures = numCreatures;
    	this.numAttributes = numAttributes;
    	this.table = new boolean [numCreatures][numAttributes];
    }

    private void writeObject(
      ObjectOutputStream outputStream
    ) throws IOException {
    	PrintWriter pw = new PrintWriter(outputStream, true);
    	pw.write( String.valueOf(serialVersionUID));
    	pw.write("Creature Truth Table");
    	pw.write(numCreatures);
    	pw.write(numAttributes);
    		  
    }

   private void readObject(
     ObjectInputStream inputStream
   ) throws ClassNotFoundException, IOException, AvmException {
     //always perform the default de-serialization first
     if( inputStream.readLong() != serialVersionUID ) throw new AvmException("Input error");
     if( inputStream.readUTF() != "CreatureTruthTable" ) throw new AvmException("Input error");
     this.numCreatures = inputStream.readInt();
     this.numAttributes = inputStream.readInt();
     
  }
    
    public boolean equals(Object other)
    {
       if (other == null) return false;
       if (this.getClass() != other.getClass()) return false;
       CreatureTruthTable o = (CreatureTruthTable)other;
       return this.numCreatures == o.numCreatures
    		   && this.numAttributes == o.numAttributes;
    }
    
    public void write( Writer out ) throws IOException {
		BufferedWriter bufferedOut = new BufferedWriter( out );
		out.write( Integer.toString( numCreatures ) );
		out.write(' ');
		out.write( Integer.toString( numAttributes ) );
		out.write('\n');
		out.flush();
    }
    
	public static CreatureTruthTable read( Reader in ) throws IOException {
		BufferedReader bufferedIn = new BufferedReader( in );
		String line = bufferedIn.readLine(); // <-- read whole line
		StringTokenizer tk = new StringTokenizer(line);
		int numCreatures = Integer.parseInt(tk.nextToken()); // <-- read single word on line and parse to int
		int numAttributes = Integer.parseInt(tk.nextToken());
		return new CreatureTruthTable(numCreatures, numAttributes);
	}
} 
