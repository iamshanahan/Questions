package questions;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CreatureTruthTableTest {

    @Test public void testRangeExceptions() throws IOException, AvmException {
    	CreatureTruthTable ctt = CttIo.read( new StringReader(
        			"2 2\n" +
        			"1 1\n" +
        			"1 1\n" +
        			"EOD" ) );
    	
    	// constructor attribute too low
    	try {
    		ctt.idAttr( -1 );
    		fail();
    	} catch( AvmException e ) {
    		//expected
    	}
    	
    	// constructor attribute too high
    	try {
    		ctt.idAttr( 2 );
    		fail();
    	} catch( AvmException e ) {
    		//expected
    	}
    	
    	CreatureTruthTable.Attribute attr = ctt.idAttr( 1 );
    	
    	// query attribute too low
    	try {
    		attr.get(-1);
    		fail();
    	} catch( AvmException e ) {
    		//expected
    	}

    	// query attribute too high
    	try {
    		attr.get(2);
    		fail();
    	} catch( AvmException e ) {
    		//expected
    	}
    }
    // helper method
    static CreatureTruthTable load( String ... line ) throws IOException, AvmException {
    	String table = "";
    	for(int i=0; i<line.length; i++) {
    		table += line[i] + '\n';
    	}
    	return CttIo.read(new StringReader(table) );
    	
    }

    // helper method
    static List<CreatureTruthTable.Attribute> asList(CreatureTruthTable.Attribute ... attr ) { 
    	return new ArrayList<>(Arrays.asList( attr ) );
    }

    @Test public void testTrivialCases() throws IOException, AvmException {
    	
    	// Empty creature list requires no questions
    	CreatureTruthTable ctt = load(
        			"0 2",
        			"EOD" );
    	assertTrue(
    	ctt.listDifferentiatesAmongAll( asList(/*empty*/) )
    	);

    	// A single creature requires no questions
    	ctt = load( "1 2",
        			"1 1",
        			"EOD" );
    	assertTrue(
    	ctt.listDifferentiatesAmongAll( asList(/*empty*/) )
    	);
    }

    @Test public void testSingleAttributeCases() throws IOException, AvmException {
    	// A very small table
    	CreatureTruthTable ctt = load(
        			"2 2",
        			"1 1",
        			"1 0",
        			"EOD" );
    	
    	// Either identity should suffice
    	assertTrue(
    	ctt.listDifferentiatesAmongAll(
    			asList(ctt.idAttr(0) ) )
    	);
    	assertTrue(
    	ctt.listDifferentiatesAmongAll(
    			asList(ctt.idAttr(1) ) )
    	);
    	
    	// First column attribute should not suffice
    	assertFalse(
    	ctt.listDifferentiatesAmongAll(
    			asList(ctt.regAttr(0) ) )
    	);
    	
    	// Second column attribute should suffice
    	assertTrue(
    	ctt.listDifferentiatesAmongAll(
    			asList(ctt.regAttr(1) ) )
    	); 	
    }
    

    @Test public void testTwoPlusAttributes() throws IOException, AvmException {
    	// A small table
    	CreatureTruthTable ctt = load(
        			"4 3",
        			"1 1 1",
        			"1 1 0",
        			"1 0 1",
        			"1 0 0",
        			"EOD" );
    	
    	// Last two columns should suffice
    	assertTrue(
    	ctt.listDifferentiatesAmongAll(
    			asList( ctt.regAttr(1), ctt.regAttr(2) ) )
    	);
    	
    	// First and last column don't
    	assertFalse(
    	ctt.listDifferentiatesAmongAll(
    			asList( ctt.regAttr(0), ctt.regAttr(2) ) )
    	);
    	
    	// Any 3 IDs should work
    	assertTrue(
    	ctt.listDifferentiatesAmongAll(
    			asList( ctt.idAttr(0), ctt.idAttr(2), ctt.idAttr(3) ) )
    	);
    	
    	// But 2 shouldn't
    	assertFalse(
    	ctt.listDifferentiatesAmongAll(
    			asList( ctt.idAttr(2), ctt.idAttr(3) ) )
    	);
    	
    	// The right mixture should work
    	assertTrue(
    	ctt.listDifferentiatesAmongAll(
    			asList(ctt.regAttr(1), ctt.idAttr(1), ctt.idAttr(3) ) )
    	); 	
    }

}
