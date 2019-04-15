package questions;

import java.util.Iterator;
import java.util.List;

import questions.CreatureTruthTable.Attribute;

public class ResultSet {
	public static int numIdentityQuestions( List<Attribute> questionList ) {
		int idQuestionCount = 0;
		Iterator<Attribute> iter = questionList.iterator();
		while( iter.hasNext() ) {
			Attribute attr = iter.next();
			if( attr.toString().startsWith( "I" ) ) idQuestionCount++;
		}
		return idQuestionCount;
	}
}
