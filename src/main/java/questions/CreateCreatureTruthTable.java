package questions;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Turns input about creatures and questions into a truth table.
 * Annotations (creature names and question text) are appended.
 * Primarily designed for interactive input, but can be tested
 * with an inputStream of Responses that exactly match interactive input.
 * 
 * @author bshanahan
 *
 */
public class CreateCreatureTruthTable {
	
	/**
	 * Executes from the command line.
	 * @param args output file for annotated truth table (required)
	 * @throws IOException
	 */
	public static void main( String [] args ) {
		try {
			if( args.length != 1 ) {
				throw new AvmException( "Incorrect number of command-line arguments" );
			}
			PrintWriter writer = new PrintWriter( args[0] );
			doMain( writer, System.out, System.in );
			writer.flush();
			writer.close();
		} catch( Exception e ) {
			System.err.println( e.getMessage() );
			System.err.println( "Usage: <program> outputfile" );
			System.err.println( "outputfile will contain a creature truth table generated from user input." );
		}
	} 

	/**
	 * Executes on streams and such.
	 * Intention is to be able to write unit tests
	 * @param resultWriter where to write the resulting truth table, typically a file
	 * @param userOut for interactive prompting, typically stdout
	 * @param userIn for user input typically stdin
	 * @throws IOException
	 */
	public static void doMain(PrintWriter resultWriter, PrintStream userOut,
			InputStream userIn) throws IOException {
		CttAnnotation annotation = createCttAndAnnotationFromInput( userOut, userIn );
		CttIo.write( resultWriter, annotation );		
	}

	/**
	 * Asks user for input until a Ctt + annotations can be constructed
	 * @param userOut
	 * @param userIn
	 * @return resulting annotation with Ctt as field.
	 */
	private static CttAnnotation createCttAndAnnotationFromInput(PrintStream userOut, InputStream userIn) {
		Scanner input = new Scanner( userIn );

		userOut.println( "First, let's input all the 'creatures'." );
		List<String> creatures = getAllTheThings( userOut, input, "Creature" );
		userOut.println();
		
		userOut.println( "Next, let's input all the questions.  We'll put the '?' at the end." );
		List<String> questions = getAllTheThings(userOut, input, "Question" );
		userOut.println();
		
		userOut.println( "Finally, let's input the answer to each question for each creature." );
		CreatureTruthTable ctt = new CreatureTruthTable( creatures.size(), questions.size() );
		CttAnnotation annotation = new CttAnnotation( ctt, creatures, questions );
		int creaturePos = 0;
		for( String creature : creatures ) {
			userOut.println( "For '" + creature + "':" );
			int questionPos = 0;
			for( String question : questions ) {
				ctt.set( creaturePos, questionPos, nextResponse( userOut, input, creature, question ) );
				questionPos++;
			}
			creaturePos++;
		}
		return annotation;
	}

	/**
	 * Reusing code for asking creatures and questions.
	 * @param out
	 * @param input
	 * @param thingName
	 * @return
	 */
	private static List<String> getAllTheThings(PrintStream out, Scanner input,
			String thingName ) {
		out.println( "Hit 'enter' when finished." );
		List<String> things = new ArrayList<>();
		while( true ) {
			String thing = getNextThing(out, input, thingName, things.size() + 1 );
			if( "".equals( thing ) ) break;
			things.add( thing );
		}
		return things;
	}

	private static String getNextThing(PrintStream out, Scanner input,
			String thingName, int position ) {
		out.print( thingName + ' ' + position + " : ");
		String thing = input.nextLine();
		if( thing.equals( "" ) ) {
			out.print( "Finished with " + thingName + "s?" );
			if( !yes( input.nextLine() ) ) {
				return getNextThing( out, input, thingName, position );
			}
		}
		return thing;
	}

	/**
	 * Gets a yes or no response.  Keeps asking (recursion) if something else is received.
	 * @param out
	 * @param input
	 * @param creature
	 * @param question
	 * @return
	 */
	private static boolean nextResponse(PrintStream out, Scanner input,
			String creature, String question) {
		out.print( creature + ": " + question + "? (y/n) " );
		String response = input.nextLine();
		if( yes( response ) ) return true;
		if( no( response ) ) return false;
		out.println( "I didn't get that." );
		return nextResponse( out, input, creature, question );
	}

	private static boolean yes(String response) {
		return "y".equalsIgnoreCase( response )
			|| "yes".equalsIgnoreCase( response );
	}

	private static boolean no(String response) {
		return "n".equalsIgnoreCase( response )
				|| "no".equalsIgnoreCase( response );
	}

}
