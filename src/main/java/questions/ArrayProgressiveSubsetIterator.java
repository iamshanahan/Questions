package questions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * For a given array of T, allows iteration over all subsets, starting with the empty set, and working its way to the
 * entire array.  The iterator returns each subset as a List of array entries.
 * 
 * Most documented implementations of subset iterator use binary logic.  I wanted one that went from smallest
 * to largest so that I can short-circuit the search for sufficiently distinguishing lists.
 * 
 * Could be generalized further to work on or return other collections.
 * 
 * @author bshanahan
 *
 * @param <T>
 */
public class ArrayProgressiveSubsetIterator<T> implements Iterator<List<T>> {
	/* Implementation notes
	 * The outer (main) iterator stores the inputed array.  Its job is to keep track of what subset size we're on.
	 * The outer iterator holds the first of a linked list of iterators (RecursiveArrayIterator).  The number of
	 * these iterators is equal to the subset size, and each one adds one array entry to a subset.  Each iterator
	 * ranges between one more than the current position of the previous iterator and the end of the array minus
	 * the number of iterators after it.  It's simpler than it sounds.
	 * 
	 * If any iterator can move, than hasNext is true.
	 * 
	 * Example:
	 * If we're in the part where we want size-3 subsets of 123456, the results would be:
	 * Itr1 Itr2 Itr3
	 * 1	2	3
	 * 1	2	4
	 * 1	2	5
	 * 1	2	6
	 * 1	3	4
	 * 1	3	5
	 * 1	3	6
	 * 1	4	5
	 * 1	4	6
	 * 1	5	6
	 * 2	3	4
	 * 2	3	5
	 * 2	3	6
	 * 2	4	5
	 * 2	4	6
	 * 2	5	6
	 * 3	4	5
	 * 3	4	6
	 * 3	5	6
	 * 4	5	6
	 * 1	2	3
	 * 1	2	3
	 * 1	2	3
	 */

	private final T[] array;
	
	int nextListLength;

	private RecursiveArrayIterator iter;
	// null until nextListLength is incremented, then never null
	
	List<T> nextList;

	/**
	 * The containing class's responsibilities include
	 *    * Holding onto the input array
	 *    * Keeping track of the current length of subsets we're iterating through
	 *    * Each time next is called, initializing the return List
	 *    * Holding onto the root iterator
	 * 
	 * The first time ArrayProgressive is called, it returns the empty set.
	 * The second time it starts a leaf iterator that runs from 0 to length - 1.
	 * After that is done, it starts a branch iterator that runs from 0 to length - 2.
	 * That in turn immediately starts a leaf iterator that runs from 1 to length - 1.
	 * Once that leaf iterator has run its course, the branch iterator increments to 
	 * 1 and starts a leaf iterator that runs from 2 to length -1.
	 * Each time the length of subsets increases, we add one more iterator to the chain.
	 * Finally the number of iterators equals the length of the input array, nobody has any
	 * room to move, one result is returned, and hasNext becomes false.
	 * @author bshanahan
	 *
	 */
	public ArrayProgressiveSubsetIterator( T[] array ) {
		this.array = array;
		nextListLength = 0;
	}

	@Override
	public boolean hasNext() {
		return nextListLength == 0
				|| nextListLength < array.length
				|| nextListLength == array.length && iter.hasNext();
	}

	@Override
	public List<T> next() {
		nextList = new ArrayList<>( nextListLength );
		if( nextListLength == 0 ) {
			nextListLength++;
			if( nextListLength <= array.length ) {
				iter = new LeafIterator( 0 );
			}
		} else {
			iter.next();
			if( !iter.hasNext() ) {
				nextListLength++;
				if( nextListLength <= array.length ) {
					iter = new BranchIterator(0, nextListLength);
				}
			}
		}
		return nextList;
	}
	
	/**
	 * The logic about what to do for next and hasNext is totally different
	 * depending on whether there's another iterator after you on the chain.
	 * So to avoid a bunch of 'if iter == null ... else' code, they are separate
	 * types.
	 * 
	 * Since the enclosing class contains the ultimate result array, these iterators
	 * each just add their value to that result and return null.
	 * 
	 * @author bshanahan
	 *
	 */
	private interface RecursiveArrayIterator extends Iterator<Void> {
		
	}

	private class LeafIterator implements RecursiveArrayIterator {

		private int position;

    	boolean hasNext;
		
		public LeafIterator( int position ) {
			this.position = position;
			hasNext = true;
		}
		
		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Void next() {
			nextList.add( array[position] );
			if( position + 1 == array.length ) {
				hasNext = false;
			} else {
				position++;
			}
			return null;
		}
	}
	
	private class BranchIterator implements RecursiveArrayIterator {

		private int position;

    	private final int numRemainingEntries;
    	
    	boolean hasNext;
    	
    	private RecursiveArrayIterator nextIter;
		
		public BranchIterator( int position, int numRequestedEntries ) {
			this.position = position;
			this.numRemainingEntries = numRequestedEntries - 1;
			hasNext = true;
			createNextBranch();
		}

		private void createNextBranch() {
			if( numRemainingEntries > 1 ) {
				nextIter = new BranchIterator( position + 1, numRemainingEntries ) ;
			} else {
				nextIter = new LeafIterator( position + 1 );
			}
		}
		
		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Void next() {
			nextList.add( array[position] );
			nextIter.next();
			if( !nextIter.hasNext() ) {
				if( position + 1 + numRemainingEntries >= array.length ) {
					hasNext = false;
				} else {
					position++;
					createNextBranch();
				};
			}
			return null;
		}
	}
}
