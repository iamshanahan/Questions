package questions;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import questions.CreatureTruthTable.Attribute;
import questions.CreatureTruthTable.IdentityAttribute;
import questions.CreatureTruthTable.RegularAttribute;

public class QuizMe {

	public static void main( String [] args ) {
		try {
			if( args.length != 2 && args.length != 3 ) {
				throw new AvmException( "Incorrect number of command-line arguments" );
			}
			Reader cttReader = new FileReader( args[0] );
			Reader listListReader = new FileReader( args[1] );
			if( args.length == 2 ) {
				doMain( cttReader, listListReader, System.out, System.in );
			} else {
				doMain( cttReader, listListReader, Integer.parseInt( args[2] ), System.out, System.in );
			}
			cttReader.close();
			listListReader.close();
			
		} catch( Exception e ) {
			System.err.println( e.getMessage() );
			System.err.println( "Usage: <program> ctt-file question-list-file" );
			System.err.println( "Or   : <program> ctt-file question-list-file list-number" );
			System.err.println( "Asks user the questions in question list <list-number>" );
			System.err.println( "from <question-list-file>, then identifies the 'creature'." );
			System.err.println( "If <question-list-file> only contains one list, then" );
			System.err.println( "<list-number> may be omitted." );
			System.exit(1);
		}		
	}

	private static void doMain(Reader cttReader, Reader listListReader,
			PrintStream out, InputStream in) throws IOException, AvmException {
		CttAnnotation aCtt = CttIo.readCttAnnotation( cttReader );
		List<List<Attribute>> questionListList = CttIo.readQuestionListList( listListReader, aCtt.getCtt() );
		if( questionListList.size() != 1 ) {
			throw new AvmException( "Multiple question lists found. Add [0-" + (questionListList.size()-1) + "] to invocation." );
		}
		identifyCreature( 0, aCtt, questionListList, out, in );
	}

	private static void doMain(Reader cttReader, Reader listListReader,
			int listNo, PrintStream out, InputStream in) throws IOException, AvmException {
		CttAnnotation aCtt = CttIo.readCttAnnotation( cttReader );
		List<List<Attribute>> questionListList = CttIo.readQuestionListList( listListReader, aCtt.getCtt() );
		identifyCreature( listNo, aCtt, questionListList, out, in );
	}

	private static void identifyCreature(int listNo, CttAnnotation aCtt, List<List<Attribute>> questionListList, PrintStream out, InputStream in) throws AvmException {
		Scanner input = new Scanner( in );
		List<Attribute> questionList = questionListList.get( listNo );
		List<Boolean> answers = new ArrayList<>( questionList.size() );
		Iterator<Attribute> questionIter = questionList.iterator();
		while( questionIter.hasNext() ) {
			Attribute attr = questionIter.next();
			if( attr.toString().startsWith( "I" ) ) {
				out.println( "Is it '" + aCtt.getCreatureName( ((IdentityAttribute)attr).getCreatureNum() ) + "'?");
			} else {
				out.println( aCtt.getQuestion( ((RegularAttribute)attr).getQuestionNum() ) + "?");
			}
			answers.add( nextResponse( out, input, aCtt, attr ) );
		}
		List<Integer> creatures = findMatch( aCtt.getCtt(), questionList, answers );
		for( Integer creature : creatures ) {
			out.println( aCtt.getCreatureName( creature ) );
		}
	}

	private static List<Integer> findMatch(CreatureTruthTable ctt,
			List<Attribute> questionList, List<Boolean> answers) throws AvmException {
		List<Integer> results = new ArrayList<>();
		for( int creature = 0; creature < ctt.getNumCreatures(); creature++ ) {
			if( matches( ctt, questionList, answers, creature) ) {
				results.add( creature );
			}
		}
		// TODO Auto-generated method stub
		return results;
	}

	private static boolean matches(CreatureTruthTable ctt,
			List<Attribute> questionList, List<Boolean> answers, int creature) throws AvmException {
		Iterator<Attribute> attrIter = questionList.iterator();
		Iterator<Boolean> answerIter = answers.iterator();
		while( attrIter.hasNext() ) {
			Attribute attr= attrIter.next();
			boolean answer = answerIter.next();
			if( attr.get( creature ) != answer ) return false;
			
		}
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Gets a yes or no response.  Keeps asking (recursion) if something else is received.
	 * 
	 * @param out
	 * @param input
	 * @param aCtt
	 * @param attr
	 * @return
	 * @throws AvmException
	 */
	private static boolean nextResponse( PrintStream out, Scanner input, CttAnnotation aCtt, Attribute attr ) throws AvmException {
		String response = input.nextLine();
		if( yes( response ) ) return true;
		if( no( response ) ) return false;
		for( int i=0; i<aCtt.getNumCreatures(); i++ ) {
			System.out.println( aCtt.getCreatureName( i ) + ": "
		+ ( attr.get( i ) ? "Yes" : "No" ) ); 
		}
		
		return nextResponse( out, input, aCtt, attr );
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

