package questions;

import java.util.*;

/**
 * The workhorse class for this project.  CreatureTruthTable represents a table of whether the answer is yes or no
 * to the question "Does creature #c have attribute #a?".  Creature names and attribute descriptions are omitted
 * because they don't affect the algorithm of interests.  
 * 
 * You have to know the dimensions at construction, but you can fill in the individual boolean values afterward.
 * 
 * An individual creature or attribute can be thought of as a list of booleans; a creature is a row of booleans 
 * and an attribute is a column of booleans.  As such they only exist in reference to a given table.  Creatures
 * have no behaviors so they are represented by row number.  There are two kinds of attributes, though, so they 
 * implement a common interface.
 * 
 * This class can tell whether a list of attributes can distinguish between any two creatures.  It can also tell
 * whether a list can separate all the creatures in the table.  So one can try to write clever search algorithms for
 * sufficient lists and call this class to check them.
 * 
 * @author bshanahan
 *
 */
public class CreatureTruthTable {
    
    private int numCreatures;

    private int numAttributes;

	private boolean [][] table;
    // so that dimensions can't be changed

    public CreatureTruthTable( int numCreatures, int numAttributes ) {
    	this.numCreatures = numCreatures;
    	this.numAttributes = numAttributes;
    	this.table = new boolean [numCreatures][numAttributes];
    }
    
    public int getNumCreatures() {
    	return numCreatures;
    }
    
    public int getNumAttributes() {
		return numAttributes;
	}
    
    public void set( int creature, int attribute, boolean doesCreatureHaveAttribute ) {
    	table[creature][attribute] = doesCreatureHaveAttribute;
    }
    
    public boolean get( int creature, int attribute ) {
    	return table[creature][attribute];
    }

    /**
     * Can you tell these two creatures apart based on this list of attributes?  A naive implementation.
     * 
     * @param list
     * @param creature1
     * @param creature2
     * @return
     * @throws AvmException
     */
    public boolean listDifferentiatesBetweenTwo( List<Attribute> list, int creature1, int creature2 ) throws AvmException {
    	Iterator<Attribute> iter = list.iterator();
    	while( iter.hasNext() ) {
    		Attribute attr = iter.next();
    		// if we find one difference, then the list differentiates
    		if( attr.get(creature1) != attr.get(creature2) ) return true;
    	}
    	// we found no differences between the two creatures in this list of attributes
    	return false;
    }
    
    /**
     * Can you use this list of attributes to uniquely identify every creature in the table?  A naive implementation.
     * @param list
     * @return
     * @throws AvmException
     */
    public boolean listDifferentiatesAmongAll( List<Attribute> list) throws AvmException {
    	// compare each pair of creatures
    	// from first to second-to-last
    	for( int creature1=0; creature1<numCreatures-1; creature1++) {
    		// from one more than selected creature to last
    		for( int creature2=creature1+1; creature2< numCreatures; creature2++) {
    			// if any two creatures can't be differentiated, the list fails
    			if( ! listDifferentiatesBetweenTwo(list, creature1, creature2) ) return false;
    		}
    	}
    	// every creature-pair was differentiated, so the list succeeds
    	return true;
    }
    
    /**
     * This interface defines the singular ability to say whether something is true or false about a creature.
     */
    public interface Attribute {
    	boolean get( int creature ) throws AvmException;
    }
    
    /**
     * The kind of attribute, initialized by a column number, that yields the value of that column for a given creature.
     *
     */
    public class RegularAttribute implements Attribute {
    	
    	int attrNum;
    	
    	public RegularAttribute( int attrNum ) {
    		this.attrNum = attrNum;
    	}
    	
    	public boolean get( int creature ) {
    		return CreatureTruthTable.this.table[creature][attrNum];
    	}
    }
    
    /**
     * The kind of attribute, initialized by creature, that yields whether or not a given creature is that creature.
     */
    public class IdentityAttribute implements Attribute {
    	
    	int identityCreature;
    	
    	public IdentityAttribute( int identityCreature ) throws AvmException {
    		// I'm putting validation in this one but not the other
    		// Because the other will throw an exception when queried
    		// In the case of out of range creature or attribute
    		// But this one doesn't query the table
    		if( identityCreature < 0 || identityCreature >= CreatureTruthTable.this.numCreatures )
    			throw new AvmException(
    					"Out of range creature " + 
    							identityCreature + 
    					". Valid range 0-" + 
    					CreatureTruthTable.this.numCreatures );
    		this.identityCreature = identityCreature;
    	}
    	
    	public boolean get( int inputCreature ) throws AvmException {
    		if( inputCreature < 0 || inputCreature >= CreatureTruthTable.this.numCreatures )
    			throw new AvmException(
    					"Out of range creature " + 
    					inputCreature + 
    					". Valid range 0-" + 
    					CreatureTruthTable.this.numCreatures );
    		return identityCreature == inputCreature;
    	}
    }

    /**
     * Allows for compactness in calling code
     * @param creature
     * @return
     * @throws AvmException
     */
    public Attribute idAttr( int creature ) throws AvmException {
    	return new IdentityAttribute( creature );
    }

    /**
     * Allows for compactness in calling code
     * @param creature
     * @return
     * @throws AvmException
     */
    public Attribute regAttr( int attribute ) throws AvmException {
    	return new RegularAttribute( attribute );
    }

    
	// equals
    public boolean equals(Object other)
    {
       if (other == null) return false;
       if (this.getClass() != other.getClass()) return false;
       CreatureTruthTable o = (CreatureTruthTable)other;
       if( this.numCreatures != o.numCreatures ) return false;
       if( this.numAttributes != o.numAttributes ) return false;
       for( int creature = 0; creature < numCreatures; creature++ )
    	   for( int attribute = 0; attribute < numAttributes; attribute++ )
    		   if( table[creature][attribute] != o.table[creature][attribute] )
    			   return false;
       return true;
    }
} 
 