package questions;

import java.util.List;

/**
 * Holds the creature names and questions for a CreatureTruthTable.
 * 
 * @author bshanahan
 *
 */
public class CttAnnotation {

	private final CreatureTruthTable ctt;
	private String[] creatures;
	private String[] questions;
	
	
	public CttAnnotation( CreatureTruthTable ctt, List<String> creatures, List<String> questions ) {
		assert( ctt.getNumCreatures() == creatures.size() );
		assert( ctt.getNumAttributes() == questions.size() );
		this.ctt = ctt;
		this.creatures = new String[ creatures.size() ];
		this.creatures = creatures.toArray( this.creatures );
		this.questions = new String[ questions.size() ];
		this.questions = questions.toArray( this.questions );
	}
	
	public CreatureTruthTable getCtt() {
		return ctt;
	}
	
	public String getCreatureName( int creatureNum ) {
		return creatures[ creatureNum ];
	}

	public String getQuestion( int questionNum ) {
		return questions[ questionNum ];
	}
	
	public int getNumCreatures() {
		return creatures.length;
	}
	
	public int getNumQuestions() {
		return questions.length;
	}
}
