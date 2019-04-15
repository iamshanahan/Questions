
package questions;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import static questions.CreatureTruthTable.Attribute;

import org.junit.Test;
import static org.junit.Assert.*;

public class MinimalListFinderTest {
    @Test public void testStub() {
        FindMinimalLists classUnderTest = new FindMinimalLists();
    }
    
    @Test public void testZeroCreatures() throws IOException, AvmException {
    	CreatureTruthTable ctt = 
        	CttIo.readCtt( new StringReader(
        			"0 0\n" +
        			"EOD") );
    	// We expect one result, the empty set
    	// Sigh...https://www.ibm.com/developerworks/library/j-jtp02216/
    	List<List<Attribute>> expectedResult = new ArrayList<List<Attribute>>();
    	expectedResult.add( new ArrayList<Attribute>() );
    	List<List<Attribute>> observedResult = FindMinimalLists.minimalLists( ctt );
    	assertEquals( expectedResult, observedResult );
    }
    
    @Test public void testOneCreature() throws IOException, AvmException {
    	CreatureTruthTable ctt = 
        	CttIo.readCtt( new StringReader(
        			"1 4\n" +
        			"0 1 1 0\n" +
        			"EOD") );
    	// We only expect the empty set, even if creature has attributes
    	List<List<Attribute>> expectedResult = new ArrayList<List<Attribute>>();
    	expectedResult.add( new ArrayList<Attribute>() );
    	List<List<Attribute>> observedResult = FindMinimalLists.minimalLists( ctt );
    	assertEquals( expectedResult, observedResult );
    }
}
