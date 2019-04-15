
package questions;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import static questions.CreatureTruthTable.Attribute;

/**
 * For a given input CreatureTruthTable (as represented by a Reader)
 * Finds the set of lists of questions that are sufficient to distinguish
 * among all creatures but are as short as possible.  They must all be the
 * same, minimal length.
 * @author bshanahan
 *
 */
public class FindMinimalLists {
	
	/**
	 * Executes on java objects
	 * @param ctt
	 * @return set of minimal lists of questions
	 * @throws AvmException
	 */
    public static List<List<Attribute>> minimalLists( CreatureTruthTable ctt ) throws AvmException {
		List<List<Attribute>> resultSet = new ArrayList<>();

		Attribute allAttributes[] = 
				new Attribute[ctt.getNumAttributes() + ctt.getNumCreatures()];
		for( int i=0; i<ctt.getNumAttributes(); i++ ) {
			allAttributes[i] = ctt.regAttr( i );
		}
		for( int i=0; i<ctt.getNumCreatures(); i++ ) {
			allAttributes[i+ctt.getNumAttributes()] = ctt.idAttr( i );
		}
		int minimalSuccessfulListLength = Integer.MAX_VALUE;
    	ArrayProgressiveSubsetIterator<Attribute> iter =
    			new ArrayProgressiveSubsetIterator<>( allAttributes );
    			
    	while( iter.hasNext() ) {
    		List<Attribute> list = iter.next();
    		// Short circuit.  Depends on iterator being non-decreasing
    		if(  list.size() > minimalSuccessfulListLength ) break;
    		if( ctt.listDifferentiatesAmongAll( list ) ) {
    			resultSet.add( list );
    			minimalSuccessfulListLength = list.size();
    		}
    	}
    	return resultSet;
    }

    /**
     * Executes on 'streams'.  Intended to be callable from unit tests.
     * @param reader
     * @param writer
     * @throws AvmException 
     * @throws IOException 
     */
	private static void doMain(Reader reader, Writer writer) throws IOException, AvmException {
    	CreatureTruthTable ctt = CttIo.readCtt( reader );
    	List<List<Attribute>> results = minimalLists( ctt );
    	CttIo.writeQuestionListList(writer, results);
		
	}
    
    /**
     * Executes from the command line
     * Configures input and output streams and defers to doMain
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
    	try {
    		if( args.length != 2 ) {
    			throw new AvmException( "Incorrect number of command-line arguments" );
    		}
			Reader reader = new FileReader( args[0] );
			Writer writer = new PrintWriter( args[1] );
			System.out.println( "Processing..." );
			doMain( reader, writer );
			reader.close();
			writer.flush();
			writer.close();
			System.out.println( "...done" );
		} catch( Exception e ) {
			System.err.println( e.getMessage() );
			System.err.println( "Usage: <program> inputfile outputfile" );
			System.err.println( "inputfile is a 'creature truth table'." );
			System.err.println( "outputfile will contain the set of minimal lists of questions for inputfile." );
			System.exit(1);
		}
    }
}
