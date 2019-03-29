package questions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * Serialization concern for CreatureTruthTable.  Implementation a bit unsophisticated but simple text format is nice.
 * EXAMPLE
 * 4 5
 * 1 0 1 0 1
 * 1 1 1 1 1
 * 1 0 0 0 1
 * 1 0 1 0 1
 * EOD
 * (ignored)
 * END EXAMPLE
 * 
 * First row of buffer must be num-creatures, space, num-attributes.  This is so reader doesn't have to read whole thing to
 * initialize.  It is an error to have the wrong number of columns, rows, missing end-of-data marker, or invalid values
 * in the table.
 * 
 * @author bshanahan
 *
 */
public class CttIo {
    // Serialization
    public static void write( Writer out, CreatureTruthTable ctt ) throws IOException {
		BufferedWriter bufferedOut = new BufferedWriter( out );
		int numCreatures = ctt.getNumCreatures();
		int numAttributes = ctt.getNumAttributes();
		bufferedOut.write( Integer.toString( numCreatures) );
		bufferedOut.write(' ');
		bufferedOut.write( Integer.toString( numAttributes ) );
		bufferedOut.write('\n');
		for( int i=0; i<numCreatures; i++ ) {
			for( int j=0; j< numAttributes; j++ ) {
				bufferedOut.write( ctt.get(i, j) ? '1' : '0' );
				bufferedOut.write(' ');
			}
			bufferedOut.write('\n');
		}
		bufferedOut.write("EOD");
		bufferedOut.flush();
    }
    
	public static CreatureTruthTable read( Reader in ) throws IOException, AvmException {
		BufferedReader bufferedIn = new BufferedReader( in );
		StringTokenizer tk = new StringTokenizer( bufferedIn.readLine() );
		int numCreatures = Integer.parseInt(tk.nextToken());
		int numAttributes = Integer.parseInt(tk.nextToken());
		CreatureTruthTable creatureTruthTable = new CreatureTruthTable(numCreatures, numAttributes);
		for( int i=0; i<numCreatures; i++ ) {
			tk = new StringTokenizer( bufferedIn.readLine() );
			for( int j=0; j<numAttributes; j++ ) {
				boolean value;
				switch( Integer.parseInt( tk.nextToken() ) ) {
				case 1:
					value = true;
					break;
				case 0:
					value = false;
					break;
				default:
					throw new AvmException("Invalid boolean table value");
				}
				creatureTruthTable.set(i, j, value);
			}
			if( tk.hasMoreElements() )throw new AvmException("Too many attributes");
			
		}
		tk = new StringTokenizer( bufferedIn.readLine() );
		if( !tk.hasMoreElements() ) throw new AvmException("EOD marker expected");
		if( !tk.nextToken().equals("EOD") ) throw new AvmException("EOD marker expected--too many creatures or extra data");
		
		return creatureTruthTable;
	}

}
