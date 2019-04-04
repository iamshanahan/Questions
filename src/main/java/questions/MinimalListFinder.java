
package questions;

import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static questions.CreatureTruthTable.Attribute;

public class MinimalListFinder {
	
    
    public static Set<List<Attribute>> minimalLists( CreatureTruthTable ctt ) throws AvmException {
		Set<List<Attribute>> resultSet = new HashSet<>();

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

    public static void main(String[] args) throws Exception {
    	if( args.length != 1 ) {
    		System.out.println( "Usage: <this> inputfilename" );
    		System.exit(1);
    	}
    	CreatureTruthTable ctt = CttIo.read( new FileReader( args[0] ) );
    	Set<List<Attribute>> results = minimalLists( ctt );
    	emitAsTable(System.out, results);
    }

	public static void emitAsTable( PrintStream out, Set<List<Attribute>> results) {
		for( List<Attribute> result : results ) {
    		for( Attribute attr : result ) {
    			out.print( attr.toString() + ' ' );
    		}
    		out.println();
    	}
	}
}
