package questions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArrayProgressiveSubsetIteratorTest {
	
	// Helper
    public void assertResultSet( List<List<String>> expected, ArrayProgressiveSubsetIterator<String> iterObserved ) {
    	for( List<String> expectedList : expected ) {
    		assertTrue( iterObserved.hasNext() );
    		List<String> nextObserved = iterObserved.next();
			assertEquals( expectedList, nextObserved );
    	}
    	assertFalse( iterObserved.hasNext() );
    }
	
    @Test public void testEmptyArrayIteration() {
    	String emptyArray[] = new String[0];
    	ArrayProgressiveSubsetIterator<String> iter = new ArrayProgressiveSubsetIterator<>(emptyArray);
    	// result should be just empty set
    	assertResultSet( Arrays.<List<String>>asList(
    			new ArrayList<String>()
    			), iter );

    }
    
    @Test public void testOneElementIteration() {
    	String oneElementArray[] = new String[]{ "Singularity" };	
    	ArrayProgressiveSubsetIterator<String> iter = new ArrayProgressiveSubsetIterator<>(oneElementArray);
    	// result should be empty set, one element set
    	assertResultSet( Arrays.<List<String>>asList(
    			new ArrayList<String>(),
    			Arrays.asList( "Singularity" )
    			), iter );
    }

    
    @Test public void testTwoElementIteration() {
    	String twoElementArray[] = new String[]{ "Thing 1","Thing 2"};	
    	ArrayProgressiveSubsetIterator<String> iter = new ArrayProgressiveSubsetIterator<>(twoElementArray);

    	assertResultSet( Arrays.<List<String>>asList(
    			new ArrayList<String>(),
    			Arrays.asList("Thing 1"),
    			Arrays.asList("Thing 2"),
    			Arrays.asList("Thing 1", "Thing 2")
    			), iter );
    }

    
    @Test public void testThreeElementIteration() {
    	// Sigh.  The hasNext/next logic is so touchy, I really want to be sure.
    	String twoElementArray[] = new String[]{ "Neo", "Morpheus", "Trinity" };	
    	ArrayProgressiveSubsetIterator<String> iter = new ArrayProgressiveSubsetIterator<>(twoElementArray);
    	// result should be empty set, one element set
    	assertResultSet( Arrays.asList(
    			new ArrayList<String>(),
    			Arrays.asList("Neo"),
    			Arrays.asList("Morpheus"),
    			Arrays.asList("Trinity"),
    			Arrays.asList("Neo", "Morpheus"),
    			Arrays.asList("Neo", "Trinity"),
    			Arrays.asList("Morpheus", "Trinity"),
    			Arrays.asList("Neo", "Morpheus", "Trinity")
    			), iter );
    }

}
