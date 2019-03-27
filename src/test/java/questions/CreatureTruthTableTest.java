package questions;

import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatureTruthTableTest {

    @Test public void testSerialization() {
 
        try {   
        	CreatureTruthTable ctt = new CreatureTruthTable(3,2);
        	StringWriter writer = new StringWriter();
        	ctt.write(writer);
        	writer.flush();
        	String cttString = writer.toString();
        	
        	StringReader reader = new StringReader( cttString );
        	CreatureTruthTable ctt2 = CreatureTruthTable.read( reader );
            writer.close();
            reader.close();
            assertEquals(ctt, ctt2);
        } catch(Exception ex) { 
        	fail();
		} 
    }
    

    @Test public void testSerializationToFile() {
    	CreatureTruthTable ctt = new CreatureTruthTable(3,2);
 
        try {   
            String filename = "unitTestFileCct.ser";
            FileWriter fileWriter = new FileWriter(filename);
            ctt.write(fileWriter);
            fileWriter.flush();
            fileWriter.close();
            
            FileReader fileReader = new FileReader(filename);
            CreatureTruthTable ctt2 = CreatureTruthTable.read(fileReader);
            fileReader.close();
            assertEquals(ctt, ctt2);
        } catch(IOException ex) { 
        	fail();
        }   
    }
}
