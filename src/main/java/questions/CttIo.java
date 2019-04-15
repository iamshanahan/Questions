package questions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import questions.CreatureTruthTable.Attribute;

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

	public static void write( Writer out, CttAnnotation annotation ) throws IOException {
		
		write( out, annotation.getCtt() );

		BufferedWriter bufferedOut = new BufferedWriter( out );
		int numCreatures = annotation.getNumCreatures();
		int numQuestions = annotation.getNumQuestions();
		for( int i=0; i<numCreatures; i++ ) {
			bufferedOut.write( annotation.getCreatureName( i ) );
			bufferedOut.write("\n");
		}
		for( int i=0; i< numQuestions; i++ ) {
			bufferedOut.write( annotation.getQuestion( i ) );
			bufferedOut.write("\n");
		}
		bufferedOut.flush();
	}
	
	public static CttAnnotation readCttAnnotation( Reader in ) throws IOException, AvmException {
		BufferedReader bufferedIn = new BufferedReader( in );
		CreatureTruthTable ctt = readCtt( bufferedIn );

		List<String> creatures = new ArrayList<>(ctt.getNumCreatures() );
		for( int i=0; i<ctt.getNumCreatures(); i++ ) {
			String readLine = bufferedIn.readLine();
			if( readLine  == null ) throw new AvmException( "Creature names and question text missing" );
			creatures.add( readLine );
		}
		List<String> questions = new ArrayList<>(ctt.getNumAttributes() );
		for( int i=0; i<ctt.getNumAttributes(); i++ ) {
			String readLine = bufferedIn.readLine();
			if( readLine  == null ) throw new AvmException( "Question text missing" );
			questions.add( readLine );
		}
		return new CttAnnotation( ctt, creatures, questions );
	}
	
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
		bufferedOut.write("EOD\n");
		bufferedOut.flush();
    }
    
	public static CreatureTruthTable readCtt( Reader in ) throws IOException, AvmException {
		return readCtt( new BufferedReader( in ) );
	}
    
	private static CreatureTruthTable readCtt( BufferedReader bufferedIn ) throws IOException, AvmException {
		StringTokenizer tk = new StringTokenizer( bufferedIn.readLine() );
		int numCreatures = Integer.parseInt(tk.nextToken());
		int numAttributes = Integer.parseInt(tk.nextToken());
		if( tk.hasMoreElements() )throw new AvmException("More than 2 entries on first line");
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

	public static Reader read( String ...strings ) throws IOException, AvmException {
		StringBuffer buffer = new StringBuffer();
		for( String string : strings ) {
			buffer.append( string ).append( '\n' );
		}
		return new StringReader( buffer.toString() );
	}


	public static void writeQuestionListList( Writer out, List<List<Attribute>> results) throws IOException {
		for( List<Attribute> result : results ) {
    		for( Attribute attr : result ) {
    			out.write( attr.toString() + ' ' );
    		}
    		out.write('\n');
    	}
	}

	public static List<List<Attribute>> readQuestionListList(Reader in, CreatureTruthTable ctt ) throws IOException, AvmException {
		List<List<Attribute>> resultSet = new ArrayList<>();
		BufferedReader bufferedIn = new BufferedReader( in );
		String line = bufferedIn.readLine();
		while( line != null ) {
			List<Attribute> resultList = new ArrayList<>();
			StringTokenizer tk = new StringTokenizer( line );
			while( tk.hasMoreElements() ) {
				char questionType = tk.nextToken().charAt( 0 );
				int number = Integer.parseInt( tk.nextToken() );
				switch( questionType ) {
				case 'A':
					resultList.add( ctt.regAttr( number ) );
					break;
				case 'I':
					resultList.add( ctt.idAttr( number ) );
				}				
			}
			resultSet.add( resultList );
			line = bufferedIn.readLine();
		}
		return resultSet;
	}

}
