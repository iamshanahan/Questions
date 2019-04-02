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
	// null until currListLength is incremented, then never null
	
	List<T> nextList;

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


// Example Array: 0:A 1:B 2:C length e