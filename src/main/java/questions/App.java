
package questions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static questions.CreatureTruthTable.Attribute;

public class App {
	
    
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
    	ArrayProgressiveSubsetIterator<Attribute> iter =
    			new ArrayProgressiveSubsetIterator<>( allAttributes );
    	while( iter.hasNext() ) {
    		List<Attribute> list = iter.next();
    		if( ctt.listDifferentiatesAmongAll( list ) ) {
    			resultSet.add( list );
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
    }
}
