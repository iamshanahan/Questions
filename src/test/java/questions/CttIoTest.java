package questions;

import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class CttIoTest {

    @Test public void testRoundTrip() {
 
        try {   
        	CreatureTruthTable ctt = new CreatureTruthTable(3,2);
        	ctt.set(0, 0, true);
        	ctt.set(1, 1, true);
        	StringWriter writer = new StringWriter();
        	CttIo.write(writer, ctt);
        	writer.flush();
        	String cttString = writer.toString();
        	
        	StringReader reader = new StringReader( cttString );
        	CreatureTruthTable ctt2 = CttIo.read( reader );
            writer.close();
            reader.close();
            assertEquals(ctt, ctt2);
        } catch(Exception ex) { 
        	fail();
		} 
    }
    

    @Test public void testRoundTripViaFile() {
    	CreatureTruthTable ctt = new CreatureTruthTable(3,2);
    	ctt.set(0, 0, true);
    	ctt.set(1, 1, true);
 
        try {   
            String filename = "unitTestFileCct.ser";
            FileWriter fileWriter = new FileWriter(filename);
            CttIo.write(fileWriter, ctt);
            fileWriter.flush();
            fileWriter.close();
            
            FileReader fileReader = new FileReader(filename);
            CreatureTruthTable ctt2 = CttIo.read(fileReader);
            fileReader.close();
            assertEquals(ctt, ctt2);
        } catch( Exception ex) { 
        	fail();
        }   
    }
    
    @Test public void testErroneousInput() {
    	// Too few attributes
        try {   
        	CttIo.read( new StringReader(
        			"2 3\n" +
        			"1 1\n" +
        			"1 1\n" +
        			"EOD") );
        	fail();
        } catch(Exception ex) {
        	// OK
		} 

    	// Too few creatures
        try {   
        	CttIo.read( new StringReader(
        			"3 2\n" +
        			"1 1\n" +
        			"1 1\n" +
        			"EOD" ) );
        	fail();
        } catch(Exception ex) {
        	// OK
		} 
        
    	// Too many attributes
        try {   
        	CttIo.read( new StringReader(
        			"3 2\n" +
        			"1 1 1\n" +
        			"1 1 1\n"  +
        			"1 1 1\n" +
        			"EOD" ) );
        	fail();
        } catch(Exception ex) {
        	// OK
		} 
        
    	// Too many creatures
        try {   
        	CttIo.read( new StringReader(
        			"2 3\n" +
        			"1 1 1\n" +
        			"1 1 1\n"  +
        			"1 1 1\n" +
        			"EOD" ) );
        	fail();
        } catch(Exception ex) {
        	// OK
		}   
        
    	// Invalid attribute -- only 1 and 0 allowed;
        try {   
        	CttIo.read( new StringReader(
        			"3 2\n" +
        			"1 1\n" +
        			"1 1\n"  +
        			"1 2\n" ) );
        	fail();
        } catch(Exception ex) {
        	// OK
		} 
    }
}
