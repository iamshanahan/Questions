package questions;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import questions.CreatureTruthTable.Attribute;
import questions.CreatureTruthTable.IdentityAttribute;
import questions.CreatureTruthTable.RegularAttribute;

/**
 * For a given truth table and a given set of question lists
 * emits the number of question lists or a particular question list
 * in verbose mode.
 * @author bshanahan
 */ 
public class DisplayQuestionLists {

	public static void main( String [] args ) {
		try {
			if( args.length != 2 && args.length != 3 ) {
				throw new AvmException( "Incorrect number of command-line arguments" );
			}
			Reader cttReader = new FileReader( args[0] );
			Reader listListReader = new FileReader( args[1] );
			if( args.length == 2 ) {
				doMain( cttReader, listListReader, System.out );
			} else {
				doMain( cttReader, listListReader, Integer.parseInt( args[2] ), System.out );
			}
			cttReader.close();
			listListReader.close();
			
		} catch( Exception e ) {
			System.err.println( e.getMessage() );
			System.err.println( "Usage: <program> ctt-file question-list-file" );
			System.err.println( "Or   : <program> ctt-file question-list-file list-number" );
			System.err.println( "Emits to stdout the number of question lists in a file." );
			System.err.println( "If there is only one, also emits that list's questions." );
			System.err.println( "Or emits the enumerated list's questions." );
			System.exit(1);
		}		
	}

	private static void doMain(Reader cttReader, Reader listListReader,
			PrintStream out) throws IOException, AvmException {
		CttAnnotation aCtt = CttIo.readCttAnnotation( cttReader );
		List<List<Attribute>> questionListList = CttIo.readQuestionListList( listListReader, aCtt.getCtt() );
		out.println( "Set contains " + questionListList.size() + " list(s) of questions." );
		if( questionListList.size() > 0 ) {
			out.println( "List-#  Length  #-Identity-Questions" );
			Iterator<List<Attribute>> listIter = questionListList.iterator();
			int listCounter = 0;
			while( listIter.hasNext() ) {
				List<Attribute> questionList = listIter.next();
				out.format(" %5d %7d     %7d%n", listCounter, questionList.size(), ResultSet.numIdentityQuestions( questionList ) );
				listCounter++;
			}
		}
		if( questionListList.size() == 1 ) {
			emitList( 0, aCtt, questionListList, out );
		} else {
			out.println( "Invoke <program> ctt-file question-list-file [0-" + (questionListList.size()-1) + "] to see individual list." );
		}
	}

	private static void doMain(Reader cttReader, Reader listListReader,
			int listNo, PrintStream out) throws IOException, AvmException {
		CttAnnotation aCtt = CttIo.readCttAnnotation( cttReader );
		List<List<Attribute>> questionListList = CttIo.readQuestionListList( listListReader, aCtt.getCtt() );
		emitList( listNo, aCtt, questionListList, out );
	}

	private static void emitList(int listNo, CttAnnotation aCtt, List<List<Attribute>> questionListList, PrintStream out) {
		List<Attribute> questionList = questionListList.get( listNo );
		Iterator<Attribute> questionIter = questionList.iterator();
		while( questionIter.hasNext() ) {
			Attribute attr = questionIter.next();
			if( attr.toString().startsWith( "I" ) ) {
				out.println( "Is it '" + aCtt.getCreatureName( ((IdentityAttribute)attr).getCreatureNum() ) + "'?");
			} else {
				out.println( aCtt.getQuestion( ((RegularAttribute)attr).getQuestionNum() ) + "?");
			}
		}
	}
}
